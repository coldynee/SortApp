package by.coldynee.io.output;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TxtFileOutputStrategyTest {

    @TempDir
    Path tempDir;

    private final Bus bus1 = new Bus.BusBuilder().setStateBusNumber("А111АА77").setModelName("Volvo").setKilometrage(100000).build();
    private final Bus bus2 = new Bus.BusBuilder().setStateBusNumber("К222КК77").setModelName("Mercedes").setKilometrage(200000).build();

    @Test
    @DisplayName("Сохранение null или пустого списка")
    void saveNullOrEmptyList() {
        Path filePath = tempDir.resolve("empty.txt");
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        strategy.save(null, filePath.toString(), false);
        strategy.save(List.of(), filePath.toString(), false);

        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("Сохранение с авто-добавлением расширения .txt")
    void saveWithAutoExtension() throws Exception {
        Path filePathWithoutExt = tempDir.resolve("auto_append");
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        strategy.save(List.of(bus1), filePathWithoutExt.toString(), false);

        Path expectedPath = tempDir.resolve("auto_append.txt");
        assertTrue(Files.exists(expectedPath));
        String content = Files.readString(expectedPath);
        assertTrue(content.contains("А111АА77;Volvo;100000"));
    }

    @Test
    @DisplayName("Сохранение в TXT: перезапись файла")
    void saveOverwrite() throws Exception {
        Path filePath = tempDir.resolve("test.txt");
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);

        String content = Files.readString(filePath);
        assertTrue(content.contains("А111АА77;Volvo;100000"));
        assertFalse(content.contains("К222КК77"));
    }

    @Test
    @DisplayName("Сохранение с null или пустым путем к файлу")
    void saveWithNullOrEmptyFilePath() {
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), null, false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "   ", false));
    }

    @Test
    @DisplayName("Сохранение в TXT: режим добавления (append)")
    void saveAppend() throws Exception {
        Path filePath = tempDir.resolve("test.txt");
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);
        strategy.save(List.of(bus2), filePath.toString(), true);

        String content = Files.readString(filePath);
        assertTrue(content.contains("А111АА77;Volvo;100000"));
        assertTrue(content.contains("К222КК77;Mercedes;200000"));

        assertEquals(2, Files.readAllLines(filePath).size());
    }

    @Test
    @DisplayName("Сохранение в TXT: пустой или null список не создает файл")
    void saveEmptyOrNullList() {
        Path filePath = tempDir.resolve("empty.txt");
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        strategy.save(List.of(), filePath.toString(), false);
        strategy.save(null, filePath.toString(), false);

        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("Сохранение с путем, состоящим только из расширения (покрытие всех веток ||)")
    void saveWithExtensionOnlyPath() {
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".txt", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".csv", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".json", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), " .TXT ", false));

        assertFalse(Files.exists(tempDir.resolve(".txt")));
        assertFalse(Files.exists(tempDir.resolve(".csv")));
        assertFalse(Files.exists(tempDir.resolve(".json")));
    }

    @Test
    @DisplayName("Автоматическая замена неверного расширения (покрытие всех веток || в normalizePath)")
    void normalizePathReplacesAllWrongExtensions() throws Exception {
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        strategy.save(List.of(bus1), tempDir.resolve("data.csv").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data.txt")));
        assertFalse(Files.exists(tempDir.resolve("data.csv")));

        strategy.save(List.of(bus1), tempDir.resolve("data2.json").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data2.txt")));
        assertFalse(Files.exists(tempDir.resolve("data2.json")));

        strategy.save(List.of(bus1), tempDir.resolve("data3.txt").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data3.txt")));
    }

    @Test
    @DisplayName("Корректная обработка InvalidPathException (символ null в пути)")
    void catchInvalidPathException() {
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();
        String invalidPath = "invalid\u0000file.txt";

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), invalidPath, false));
    }

    @Test
    @DisplayName("Корректная обработка AccessDeniedException (файл только для чтения)")
    void catchAccessDeniedException() throws Exception {
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();
        Path readOnlyFile = tempDir.resolve("readonly.txt");

        Files.writeString(readOnlyFile, "initial data");
        readOnlyFile.toFile().setReadOnly();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), readOnlyFile.toString(), true));

        readOnlyFile.toFile().setWritable(true);
    }

    @Test
    @DisplayName("Корректная обработка общего IOException (несуществующая родительская директория)")
    void catchGenericIOException() {
        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();
        String pathWithMissingParent = "non_existent_dir_999_xyz/file.txt";

        assertThrows(RuntimeException.class, () -> strategy.save(List.of(bus1), pathWithMissingParent, false));
    }
}