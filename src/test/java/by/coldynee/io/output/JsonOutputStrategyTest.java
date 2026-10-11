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
    @DisplayName("Перехват IOException")
    void catchIOException() throws Exception {
        Path fakeFileDir = tempDir.resolve("fake_directory.json");
        Files.createDirectory(fakeFileDir);

        JsonOutputStrategy strategy = new JsonOutputStrategy();
        assertThrows(RuntimeException.class, () -> strategy.save(List.of(bus1), fakeFileDir.toString(), false));
    }
}