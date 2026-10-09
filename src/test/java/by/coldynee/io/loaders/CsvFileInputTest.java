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
}