package by.coldynee.io.output;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonOutputStrategyTest {

    @TempDir
    Path tempDir;

    private final Bus bus1 = new Bus.BusBuilder().setStateBusNumber("А111АА77").setModelName("Volvo").setKilometrage(100000).build();
    private final Bus bus2 = new Bus.BusBuilder().setStateBusNumber("К222КК77").setModelName("Mercedes").setKilometrage(200000).build();

    @Test
    @DisplayName("Сохранение null или пустого списка")
    void saveNullOrEmptyList() {
        Path filePath = tempDir.resolve("empty.json");
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(null, filePath.toString(), false);
        strategy.save(List.of(), filePath.toString(), false);

        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("Сохранение с авто-добавлением расширения .json")
    void saveWithAutoExtension() throws Exception {
        Path filePathWithoutExt = tempDir.resolve("auto_append");
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(bus1), filePathWithoutExt.toString(), false);

        Path expectedPath = tempDir.resolve("auto_append.json");
        assertTrue(Files.exists(expectedPath));
        String content = Files.readString(expectedPath);
        assertTrue(content.contains("А111АА77"));
    }

    @Test
    @DisplayName("Дозапись в сущестувющий пустой JSON массив")
    void appendToEmptyJsonArray() throws Exception {
        Path filePath = tempDir.resolve("empty_array.json");
        Files.writeString(filePath, "[]");

        JsonOutputStrategy strategy = new JsonOutputStrategy();
        strategy.save(List.of(bus1), filePath.toString(), true);

        String content = Files.readString(filePath);
        assertTrue(content.contains("А111АА77"));
        assertTrue(content.startsWith("[") && content.endsWith("]"));
    }

    @Test
    @DisplayName("Дозапись, но файл не существует — создаётся новый файл")
    void appendButFileNotExists() throws Exception {
        Path filePath = tempDir.resolve("not_exists.json");
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), true);

        assertTrue(Files.exists(filePath));
        String content = Files.readString(filePath);
        assertTrue(content.contains("А111АА77"));
        assertTrue(content.startsWith("["));
        assertTrue(content.endsWith("]"));
    }

    @Test
    @DisplayName("Дозапись, файл существует, но не заканчивается на ']' — перезапись")
    void appendButFileNotEndsWithBracket() throws Exception {
        Path filePath = tempDir.resolve("broken.json");
        Files.writeString(filePath, "{\"broken\": true}");

        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), true);

        String content = Files.readString(filePath);
        assertTrue(content.contains("А111АА77"));
        assertTrue(content.startsWith("["));
        assertTrue(content.endsWith("]"));
        assertFalse(content.contains("broken"));
    }

    @Test
    @DisplayName("Дозапись в JSON с данными")
    void appendToExistingJson() throws Exception {
        Path filePath = tempDir.resolve("data.json");
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);
        strategy.save(List.of(bus2), filePath.toString(), true);

        String content = Files.readString(filePath);
        assertTrue(content.contains("А111АА77"));
        assertTrue(content.contains("К222КК77"));
    }

    @Test
    @DisplayName("Сохранение в JSON: корректный массив при перезаписи")
    void saveOverwrite() throws Exception {
        Path filePath = tempDir.resolve("test.json");
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);

        String content = Files.readString(filePath);
        assertTrue(content.startsWith("["));
        assertTrue(content.endsWith("]"));
        assertTrue(content.contains("А111АА77"));
    }

    @Test
    @DisplayName("Сохранение в JSON: режим добавления сохраняет валидность JSON-массива")
    void saveAppendValidJson() throws Exception {
        Path filePath = tempDir.resolve("test.json");
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(bus1), filePath.toString(), false);
        strategy.save(List.of(bus2), filePath.toString(), true);

        String content = Files.readString(filePath);

        assertTrue(content.startsWith("["), "JSON должен начинаться с '['");
        assertTrue(content.endsWith("]"), "JSON должен заканчиваться на ']'");

        assertTrue(content.contains("А111АА77"));
        assertTrue(content.contains("К222КК77"));

        assertEquals(0, content.indexOf("["), "Открывающая скобка '[' должна быть в самом начале (индекс 0)");
        assertEquals(0, content.lastIndexOf("["), "Открывающая скобка '[' должна встречаться только один раз во всем файле");
        assertEquals(content.length() - 1, content.lastIndexOf("]"), "Закрывающая скобка ']' должна быть только в самом конце файла");
    }

    @Test
    @DisplayName("Сохранение в JSON: пустой список игнорируется")
    void saveEmptyList() {
        Path filePath = tempDir.resolve("empty.json");
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(), filePath.toString(), false);
        assertFalse(Files.exists(filePath));
    }

    @Test
    @DisplayName("Сохранение с null или пустым путем к файлу")
    void saveWithNullOrEmptyFilePath() {
        JsonOutputStrategy strategy = new JsonOutputStrategy();
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), null, false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "   ", false));
    }

    @Test
    @DisplayName("Сохранение с путем, состоящим только из расширения (покрытие всех веток ||)")
    void saveWithExtensionOnlyPath() {
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".json", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".txt", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), ".csv", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), " .JSON ", false));

        assertFalse(Files.exists(tempDir.resolve(".json")));
        assertFalse(Files.exists(tempDir.resolve(".txt")));
        assertFalse(Files.exists(tempDir.resolve(".csv")));
    }

    @Test
    @DisplayName("Автоматическая замена неверного расширения (покрытие всех веток || в normalizePath)")
    void normalizePathReplacesAllWrongExtensions() throws Exception {
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        strategy.save(List.of(bus1), tempDir.resolve("data.txt").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data.json")));
        assertFalse(Files.exists(tempDir.resolve("data.txt")));

        strategy.save(List.of(bus1), tempDir.resolve("data2.csv").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data2.json")));
        assertFalse(Files.exists(tempDir.resolve("data2.csv")));

        strategy.save(List.of(bus1), tempDir.resolve("data3.json").toString(), false);
        assertTrue(Files.exists(tempDir.resolve("data3.json")));
    }

    @Test
    @DisplayName("Корректная обработка InvalidPathException (символ null в пути)")
    void catchInvalidPathException() {
        JsonOutputStrategy strategy = new JsonOutputStrategy();

        String invalidPath = "invalid\u0000file.json";

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), invalidPath, false));
    }

    @Test
    @DisplayName("Корректная обработка AccessDeniedException (файл только для чтения)")
    void catchAccessDeniedException() throws Exception {
        JsonOutputStrategy strategy = new JsonOutputStrategy();
        Path readOnlyFile = tempDir.resolve("readonly.json");

        Files.writeString(readOnlyFile, "[]");
        readOnlyFile.toFile().setReadOnly();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), readOnlyFile.toString(), true));

        readOnlyFile.toFile().setWritable(true);
    }

    @Test
    @DisplayName("Сохранение: имя файла становится пустым после извлечения")
    void saveHandlesEmptyFileNameAfterTrim() {
        OutputStrategy strategy = new JsonOutputStrategy();
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/ ", false));
    }

    @Test
    @DisplayName("Сохранение: путь к папке, имя которой выглядит как расширение")
    void saveHandlesExtensionOnlyInFolder() {
        OutputStrategy strategy = new JsonOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/.csv", false));
    }

    @Test
    @DisplayName("Сохранение: родительская папка не существует (покрывает проверку !Files.exists) ")
    void saveHandlesMissingParentDirectory() {
        OutputStrategy strategy = new JsonOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "non_existent_dir_999_xyz/file.csv", false));
    }

    @Test
    @DisplayName("Сохранение: полное покрытие всех веток проверки разделителей (||)")
    void saveCoversAllSeparatorBranches() {
        OutputStrategy strategy = new JsonOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "a/", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "a\\", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "a" + java.io.File.separator, false));
    }

    @Test
    @DisplayName("Сохранение: полное покрытие веток проверки имени файла (&&)")
    void saveCoversFileNameBranches() {
        OutputStrategy strategy = new JsonOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/   ", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "folder/.hidden.txt", false));
    }

    @Test
    @DisplayName("Сохранение: полное покрытие веток проверки родительской директории (&&)")
    void saveCoversParentDirectoryBranches() {
        OutputStrategy strategy = new JsonOutputStrategy();

        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "non_existent_dir_12345/file.txt", false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), tempDir.resolve("valid_file.txt").toString(), false));
        assertDoesNotThrow(() -> strategy.save(List.of(bus1), "just_a_file.txt", false));
    }

    @Test
    @DisplayName("Корректная обработка общего IOException (попытка записи в файл, который является директорией)")
    void catchGenericIOException() throws Exception {
        OutputStrategy strategy = new JsonOutputStrategy();

        Path fakeFileAsDir = tempDir.resolve("fake_file_instead_of_dir");
        Files.writeString(fakeFileAsDir, "I am a file, not a directory");

        String pathInsideFakeFile = fakeFileAsDir.toString() + "/inside.csv";

        assertThrows(RuntimeException.class, () -> strategy.save(List.of(bus1), pathInsideFakeFile, false));
    }
}