package by.coldynee.io.loaders;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFileInputTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Загрузка '.csv' с пропуском строки заголовка")
    void loadCsvWithHeader() throws Exception {
        String content = "Госномер,Модель,Пробег\nА111АА77,Volvo,100000\nК222КК77,Mercedes,200000";
        Path filePath = tempDir.resolve("with_header.csv");
        Files.writeString(filePath, content);

        CsvFileInput loader = new CsvFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(2, buses.size());
        assertEquals("А111АА77", buses.get(0).getStateBusNumber());
    }

    @Test
    @DisplayName("Пропуск невалидных объектов")
    void loadMixedCsvData() throws Exception {
        String content = "Госномер,Модель,Пробег\n" +
                "А111АА77,Volvo,100000\n" +
                "А222АА77,BMW,-500\n" +
                "НеверныйФормат\n" +
                "К333КК77,Kia,50000";

        Path filePath = tempDir.resolve("mixed.csv");
        Files.writeString(filePath, content);

        CsvFileInput loader = new CsvFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(2, buses.size());
    }

    @Test
    @DisplayName("Несуществующий файл")
    void loadMissingCsvFile() {
        CsvFileInput loader = new CsvFileInput(tempDir.resolve("missing.csv").toString());
        List<Bus> buses = loader.load(0);
        assertTrue(buses.isEmpty());
    }

    @Test
    @DisplayName("Конструктор: невалидные пути и авто-добавление .csv")
    void constructorValidationAndAutoAppend() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new CsvFileInput(null));
        assertThrows(IllegalArgumentException.class, () -> new CsvFileInput("   "));
        assertThrows(IllegalArgumentException.class, () -> new CsvFileInput(""));

        Path actualFile = tempDir.resolve("auto_append.csv");
        Files.writeString(actualFile, "А111АА77,Volvo,100000");

        String pathWithoutExt = actualFile.toString().substring(0, actualFile.toString().length() - 4);
        CsvFileInput loader = new CsvFileInput(pathWithoutExt);

        assertEquals(1, loader.load(0).size());
    }

    @Test
    @DisplayName("Ограничение количества загружаемых записей (count > 0)")
    void loadWithLimit() throws Exception {
        String content = "А111АА77,Volvo,100000\nК222КК77,Mercedes,200000\nЕ333ЕЕ77,BMW,300000";
        Path filePath = tempDir.resolve("limit.csv");
        Files.writeString(filePath, content);

        CsvFileInput loader = new CsvFileInput(filePath.toString());
        assertEquals(2, loader.load(2).size());
    }

    @Test
    @DisplayName("Пропуск пустых строк в файле")
    void skipsEmptyLines() throws Exception {
        String content = "А111АА77,Volvo,100000\n\n\nК222КК77,Mercedes,200000";
        Path filePath = tempDir.resolve("empty_lines.csv");
        Files.writeString(filePath, content);

        CsvFileInput loader = new CsvFileInput(filePath.toString());
        assertEquals(2, loader.load(0).size());
    }

    @Test
    @DisplayName("Перехват NumberFormatException (не число в пробеге)")
    void catchNumberFormatException() throws Exception {
        String content = "А111АА77,Volvo,abc";
        Path filePath = tempDir.resolve("nan.csv");
        Files.writeString(filePath, content);

        CsvFileInput loader = new CsvFileInput(filePath.toString());
        assertEquals(0, loader.load(0).size());
    }

    @Test
    @DisplayName("Перехват IOException")
    void catchIOException() throws Exception {
        Path fakeDir = tempDir.resolve("fake_directory.csv");
        Files.createDirectory(fakeDir);

        CsvFileInput loader = new CsvFileInput(fakeDir.toString());
        assertTrue(loader.load(0).isEmpty());
    }
}