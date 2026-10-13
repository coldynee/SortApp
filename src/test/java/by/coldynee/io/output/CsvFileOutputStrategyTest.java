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
    @DisplayName("Сохранение с null или пустым путем к файлу")
    void saveWithNullOrEmptyFilePath() {
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), null, false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "   ", false));
    }

    @Test
    @DisplayName("Сохранение с путем, состоящим только из расширения (покрытие всех веток ||)")
    void saveWithExtensionOnlyPath() {
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".csv", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".txt", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".json", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), " .CSV ", false));

        assertFalse(Files.exists(tempDir.resolve(".csv")));
        assertFalse(Files.exists(tempDir.resolve(".txt")));
        assertFalse(Files.exists(tempDir.resolve(".json")));
    }


    @Test
    @DisplayName("Корректная обработка AccessDeniedException (файл только для чтения)")
    void catchAccessDeniedException() throws Exception {
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();
        Path readOnlyFile = tempDir.resolve("readonly.csv");

        Files.writeString(readOnlyFile, "initial data");
        readOnlyFile.toFile().setReadOnly();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), readOnlyFile.toString(), false));

        readOnlyFile.toFile().setWritable(true);
    }

    @Test
    @DisplayName("Автоматическая замена неверного расширения (покрытие всех веток ||)")
    void normalizePathReplacesAllWrongExtensions() throws Exception {
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        strategy.save(List.of(bus1), tempDir.resolve("data.txt").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data.csv")));
        assertFalse(Files.exists(tempDir.resolve("data.txt")));

        strategy.save(List.of(bus1), tempDir.resolve("data2.json").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data2.csv")));
        assertFalse(Files.exists(tempDir.resolve("data2.json")));

        strategy.save(List.of(bus1), tempDir.resolve("data3.csv").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data3.csv")));
    }

    @Test
    @DisplayName("Корректная обработка InvalidPathException (символ null в пути)")
    void catchInvalidPathException() {
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        String invalidPath = "invalid\u0000file.csv";

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), invalidPath, false));
    }

    @Test
    @DisplayName("Корректная обработка общего IOException (несуществующая родительская директория)")
    void catchGenericIOException() {
        CsvFileOutputStrategy strategy = new CsvFileOutputStrategy();

        String pathWithMissingParent = "non_existent_dir_999_xyz/file.csv";

        assertThrows(RuntimeException.class, () -> strategy.save(List.of(bus1), pathWithMissingParent, false));
    }
}