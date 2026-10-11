package by.coldynee.io.output;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFileOutputStrategyTest {

    @TempDir
    Path tempDir;

    private final Bus bus1 = new Bus.BusBuilder().setStateBusNumber("А111АА77").setModelName("Volvo").setKilometrage(100000).build();
    private final Bus bus2 = new Bus.BusBuilder().setStateBusNumber("К222КК77").setModelName("Mercedes").setKilometrage(200000).build();

    @Test
    @DisplayName("Сохранение null или пустого списка")
    void saveNullOrEmptyList() {
        Path filePath = tempDir.resolve("empty.csv");
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        strategy.save(null, filePath.toString(), false);
        strategy.save(List.of(), filePath.toString(), false);

        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("Сохранение с авто-добавлением расширения .csv ")
    void saveWithAutoExtension() throws Exception {
        Path filePathWithoutExt = tempDir.resolve("auto_append");
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        strategy.save(List.of(bus1), filePathWithoutExt.toString(), false);

        Path expectedPath = tempDir.resolve("auto_append.csv");
        assertTrue(Files.exists(expectedPath));
        String content = Files.readString(expectedPath);
        assertTrue(content.startsWith("ГосНомер,Модель,Пробег"));
    }

    @Test
    @DisplayName("Создание нового файла с заголовком")
    void saveNewFileWithHeader() throws Exception {
        Path filePath = tempDir.resolve("new.csv");
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);

        String content = Files.readString(filePath);
        assertTrue(content.startsWith("ГосНомер,Модель,Пробег"));
    }

    @Test
    @DisplayName("Сохранение в CSV: создание нового файла с заголовком")
    void saveOverwriteWithHeader() throws Exception {
        Path filePath = tempDir.resolve("test.csv");
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);

        String content = Files.readString(filePath);
        assertTrue(content.startsWith("ГосНомер,Модель,Пробег"));
        assertTrue(content.contains("А111АА77,Volvo,100000"));
    }

    @Test
    @DisplayName("Сохранение в CSV: режим добавления (append) без дублирования заголовка")
    void saveAppendNoDuplicateHeader() throws Exception {
        Path filePath = tempDir.resolve("test.csv");
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);
        strategy.save(List.of(bus2), filePath.toString(), true);

        String content = Files.readString(filePath);
        List<String> lines = Files.readAllLines(filePath);

        assertEquals(1, lines.stream().filter(line -> line.equals("ГосНомер,Модель,Пробег")).count());
        assertEquals(3, lines.size());
        assertTrue(content.contains("К222КК77,Mercedes,200000"));
    }

    @Test
    @DisplayName("Сохранение в CSV: пустой список игнорируется")
    void saveEmptyList() {
        Path filePath = tempDir.resolve("empty.csv");
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        strategy.save(List.of(), filePath.toString(), false);
        assertFalse(Files.exists(filePath));
    }
    @Test
    @DisplayName("Перехват IOException")
    void catchIOException() throws Exception {
        Path fakeFileDir = tempDir.resolve("fake_directory.csv");
        Files.createDirectory(fakeFileDir);

        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();
        assertThrows(RuntimeException.class, () -> strategy.save(List.of(bus1), fakeFileDir.toString(), false));
    }
}