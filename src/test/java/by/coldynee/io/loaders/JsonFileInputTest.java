package by.coldynee.io.loaders;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileInputTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Загрузка валидного '.json' массива")
    void loadValidJson() throws Exception {
        String content = "[\n" +
                "  {\"stateBusNumber\": \"А111АА77\", \"modelName\": \"Volvo\", \"kilometrage\": 100000},\n" +
                "  {\"stateBusNumber\": \"К222КК77\", \"modelName\": \"Mercedes\", \"kilometrage\": 200000}\n" +
                "]";
        Path filePath = tempDir.resolve("valid.json");
        Files.writeString(filePath, content);

        JsonFileInput loader = new JsonFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(2, buses.size());
        assertEquals("Volvo", buses.get(0).getModelName());
    }

    @Test
    @DisplayName("Пропуск невалидных '.json' объектов")
    void loadMixedJsonData() throws Exception {
        String content = "[\n" +
                "  {\"stateBusNumber\": \"А111АА77\", \"modelName\": \"Volvo\", \"kilometrage\": 100000},\n" +
                "  {\"stateBusNumber\": \"Б222ББ77\", \"modelName\": \"Bad\", \"kilometrage\": 100000},\n" +
                "  {\"stateBusNumber\": \"А333АА77\", \"modelName\": \"Toyota\", \"kilometrage\": \"abc\"}\n" +
                "]";
        Path filePath = tempDir.resolve("mixed_json.json");
        Files.writeString(filePath, content);

        JsonFileInput loader = new JsonFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(1, buses.size());
    }

    @Test
    @DisplayName("Обработка JSON, который не является массивом")
    void loadNonArrayJson() throws Exception {
        String content = "{\"stateBusNumber\": \"А111АА77\", \"modelName\": \"Volvo\", \"kilometrage\": 100000}";
        Path filePath = tempDir.resolve("not_array.json");
        Files.writeString(filePath, content);

        JsonFileInput loader = new JsonFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertTrue(buses.isEmpty());
    }

    @Test
    @DisplayName("Обработка невалидного '.json' синтаксиса")
    void loadInvalidJsonSyntax() throws Exception {
        String content = "[{\"stateBusNumber\": \"А111АА77\", \"modelName\": \"Volvo\"";
        Path filePath = tempDir.resolve("invalid_syntax.json");
        Files.writeString(filePath, content);

        JsonFileInput loader = new JsonFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertTrue(buses.isEmpty());
    }

    @Test
    @DisplayName("Конструктор: невалидные пути (null и blank)")
    void constructorThrowsOnInvalidPath() {
        assertThrows(IllegalArgumentException.class, () -> new JsonFileInput(null));
        assertThrows(IllegalArgumentException.class, () -> new JsonFileInput("   "));
        assertThrows(IllegalArgumentException.class, () -> new JsonFileInput(""));
    }

    @Test
    @DisplayName("Конструктор: авто-добавление расширения .json")
    void constructorAutoAppendsJsonExtension() throws Exception {
        Path actualFile = tempDir.resolve("auto_append.json");
        Files.writeString(actualFile, "[{\"stateBusNumber\": \"А111АА77\", \"modelName\": \"Volvo\", \"kilometrage\": 100000}]");

        String pathWithoutExtension = actualFile.toString().substring(0, actualFile.toString().length() - 5);

        JsonFileInput loader = new JsonFileInput(pathWithoutExtension);
        List<Bus> buses = loader.load(0);

        assertEquals(1, buses.size());
    }

    @Test
    @DisplayName("Обработка несуществующего файла")
    void loadMissingFile() {
        JsonFileInput loader = new JsonFileInput(tempDir.resolve("does_not_exist.json").toString());
        List<Bus> buses = loader.load(0);

        assertTrue(buses.isEmpty());
    }

    @Test
    @DisplayName("Ограничение количества загружаемых объектов (count > 0)")
    void loadWithLimit() throws Exception {
        String content = "[\n" +
                "  {\"stateBusNumber\": \"А111АА77\", \"modelName\": \"Volvo\", \"kilometrage\": 100000},\n" +
                "  {\"stateBusNumber\": \"К222КК77\", \"modelName\": \"Mercedes\", \"kilometrage\": 200000},\n" +
                "  {\"stateBusNumber\": \"Е333ЕЕ77\", \"modelName\": \"BMW\", \"kilometrage\": 300000}\n" +
                "]";
        Path filePath = tempDir.resolve("limit.json");
        Files.writeString(filePath, content);

        JsonFileInput loader = new JsonFileInput(filePath.toString());
        List<Bus> buses = loader.load(2);

        assertEquals(2, buses.size());
    }

    @Test
    @DisplayName("Обработка JSON объектов с отсутствующими полями")
    void loadJsonWithMissingFields() throws Exception {
        String content = "[\n" +
                "  {\"stateBusNumber\": \"А111АА77\", \"modelName\": \"Volvo\", \"kilometrage\": 100000},\n" +
                "  {\"modelName\": \"Mercedes\", \"kilometrage\": 200000},\n" +
                "  {\"stateBusNumber\": \"К333КК77\", \"kilometrage\": 300000},\n" +
                "  {\"stateBusNumber\": \"Е444ЕЕ77\", \"modelName\": \"BMW\"}\n" +
                "]";
        Path filePath = tempDir.resolve("missing_fields.json");
        Files.writeString(filePath, content);

        JsonFileInput loader = new JsonFileInput(filePath.toString());
        List<Bus> buses = loader.load(0);

        assertEquals(1, buses.size());
        assertEquals("А111АА77", buses.get(0).getStateBusNumber());
    }

    @Test
    @DisplayName("Перехват IOException")
    void catchIOException() throws Exception {
        Path fakeFileDir = tempDir.resolve("fake_directory.json");
        Files.createDirectory(fakeFileDir);

        JsonFileInput loader = new JsonFileInput(fakeFileDir.toString());
        List<Bus> buses = loader.load(0);

        assertTrue(buses.isEmpty());
    }
}