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
    @DisplayName("Сохранение: имя файла становится пустым после извлечения")
    void saveHandlesEmptyFileNameAfterTrim() {
        OutputStrategy strategy = new TxtFileOutputStrategy();
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/ ", false));
    }

    @Test
    @DisplayName("Сохранение: путь к папке, имя которой выглядит как расширение")
    void saveHandlesExtensionOnlyInFolder() {
        OutputStrategy strategy = new TxtFileOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/.csv", false));
    }

    @Test
    @DisplayName("Сохранение: родительская папка не существует (покрывает проверку !Files.exists) ")
    void saveHandlesMissingParentDirectory() {
        OutputStrategy strategy = new TxtFileOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "non_existent_dir_999_xyz/file.csv", false));
    }
    @Test
    @DisplayName("Сохранение: полное покрытие всех веток проверки разделителей (||)")
    void saveCoversAllSeparatorBranches() {
        OutputStrategy strategy = new TxtFileOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "a/", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "a\\", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "a" + java.io.File.separator, false));
    }

    @Test
    @DisplayName("Сохранение: полное покрытие веток проверки имени файла (&&)")
    void saveCoversFileNameBranches() {
        OutputStrategy strategy = new TxtFileOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/   ", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/.hidden.txt", false));
    }

    @Test
    @DisplayName("Сохранение: полное покрытие веток проверки родительской директории (&&)")
    void saveCoversParentDirectoryBranches() {
        OutputStrategy strategy = new TxtFileOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "non_existent_dir_12345/file.txt", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), tempDir.resolve("valid_file.txt").toString(), false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "just_a_file.txt", false));
    }

    @Test
    @DisplayName("Корректная обработка общего IOException (попытка записи в файл, который является директорией)")
    void catchGenericIOException() throws Exception {
        OutputStrategy strategy = new TxtFileOutputStrategy();

        Path fakeFileAsDir = tempDir.resolve("fake_file_instead_of_dir");
        Files.writeString(fakeFileAsDir, "I am a file, not a directory");

        String pathInsideFakeFile = fakeFileAsDir.toString() + "/inside.csv";

        assertThrows(RuntimeException.class, () -> strategy.save(List.of(bus1), pathInsideFakeFile, false));
    }
}