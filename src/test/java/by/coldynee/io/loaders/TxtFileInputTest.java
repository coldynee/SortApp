package by.coldynee.io.loaders;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TxtFileInputTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Загрузка валидных данных из '.txt'")
    void loadValidData() throws Exception {
        String content = "А111АА77;Volvo;100000\nК222КК77;Mercedes;200000";
        Path filePath = tempDir.resolve("valid.txt");
        Files.writeString(filePath, content);

        TxtFileInput loader = new TxtFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(2, buses.size());
        assertEquals("А111АА77", buses.get(0).getStateBusNumber());
        assertEquals("Mercedes", buses.get(1).getModelName());
    }

    @Test
    @DisplayName("Пропуск невалидных строк")
    void loadDataSkipsInvalid() throws Exception {
        String content = "А111АА77;Volvo;100000\n" +
                "Б222ББ77;Bad;100000\n" +
                "А333АА77;Toyota;abc\n" +
                "К444КК77;Kia;50000";

        Path filePath = tempDir.resolve("mixed_data.txt");
        Files.writeString(filePath, content);

        TxtFileInput loader = new TxtFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(2, buses.size());
        assertEquals("А111АА77", buses.get(0).getStateBusNumber());
        assertEquals("К444КК77", buses.get(1).getStateBusNumber());
    }

    @Test
    @DisplayName("Ограничение количества загружаемых записей")
    void loadWithLimit() throws Exception {
        String content = "А111АА77;Volvo;100000\nК222КК77;Mercedes;200000\nЕ333ЕЕ77;BMW;300000";
        Path filePath = tempDir.resolve("limit_data.txt");
        Files.writeString(filePath, content);

        TxtFileInput loader = new TxtFileInput(filePath.toString());
        List<Bus> buses = loader.load(2);

        assertEquals(2, buses.size());
    }

    @Test
    @DisplayName("Обработка несуществующего файла")
    void loadMissingFile() {
        TxtFileInput loader = new TxtFileInput(tempDir.resolve("non_exists").toString());
        List<Bus> buses = loader.load(0);

        assertTrue(buses.isEmpty());
    }

    @Test
    @DisplayName("Конструктор: авто-добавление .txt и валидация путей")
    void constructorValidationAndAutoAppend() throws Exception {
        Path actualFile = tempDir.resolve("auto_append.txt");
        Files.writeString(actualFile, "А111АА77;Volvo;100000");
        String pathWithoutExtension = actualFile.toString().substring(0, actualFile.toString().length() - 4);
        TxtFileInput loader = new TxtFileInput(pathWithoutExtension);
        assertEquals(1, loader.load(0).size());

        assertThrows(IllegalArgumentException.class, () -> new TxtFileInput(null));
        assertThrows(IllegalArgumentException.class, () -> new TxtFileInput("   "));
        assertThrows(IllegalArgumentException.class, () -> new TxtFileInput(""));
    }

    @Test
    @DisplayName("Пропуск строк с неверным количеством полей")
    void skipsLinesWithWrongColumnCount() throws Exception {
        String content = "А111АА77;Volvo\n" +
                "А111АА77;Volvo;100000;лишнее_поле\n" +
                "К222КК77;Mercedes;200000";
        Path filePath = tempDir.resolve("wrong_columns.txt");
        Files.writeString(filePath, content);

        TxtFileInput loader = new TxtFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(1, buses.size());
    }

    @Test
    @DisplayName("Пропуск пустых строк и комментариев (# и /)")
    void skipsEmptyAndCommentLines() throws Exception {
        String content = "# Это комментарий\n" +
                "\n" +
                "/ Это тоже комментарий\n" +
                "А111АА77;Volvo;100000";
        Path filePath = tempDir.resolve("comments.txt");
        Files.writeString(filePath, content);

        TxtFileInput loader = new TxtFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(1, buses.size());
        assertEquals("А111АА77", buses.get(0).getStateBusNumber());
    }



    @Test
    @DisplayName("Перехват IOException")
    void catchIOException() throws Exception {
        Path fakeFileDir = tempDir.resolve("fake_directory.txt");
        Files.createDirectory(fakeFileDir);

        TxtFileInput loader = new TxtFileInput(fakeFileDir.toString());

        List<Bus> buses = loader.load(0);

        assertTrue(buses.isEmpty());
    }
}