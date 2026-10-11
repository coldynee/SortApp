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
    @DisplayName("Перехват IOException (попытка записи в директорию)")
    void catchIOException() throws Exception {
        Path fakeFileDir = tempDir.resolve("fake_directory.txt");
        Files.createDirectory(fakeFileDir);

        TxtFileOutputStrategy strategy = new TxtFileOutputStrategy();

        assertThrows(RuntimeException.class, () -> strategy.save(List.of(bus1), fakeFileDir.toString(), false));
    }
}