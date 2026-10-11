package by.coldynee.io.output;

import by.coldynee.model.Bus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Стратегия записи данных в CSV-файл (.csv).
 * Использует Stream API для маппинга полей через разделитель-запятую.
 */
public class CsvFileOutputStrategy implements OutputStrategy {

    @Override
    public void save(List<Bus> buses, String filePath, boolean append) {
        if (buses == null || buses.isEmpty()) {
            System.out.println("Список пуст, сохранение отменено.");
            return;
        }

        Path path = Path.of(filePath.endsWith(".csv") ? filePath : filePath + ".csv");

        try {
            List<String> lines = buses.stream()
                    .map(bus -> String.format("%s,%s,%d",
                            bus.getStateBusNumber(),
                            bus.getModelName(),
                            bus.getKilometrage()))
                    .collect(Collectors.toList());

            if (!append && !Files.exists(path)) {
                lines.add(0, "ГосНомер,Модель,Пробег");
            }

            StandardOpenOption[] options = append
                    ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE}
                    : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};

            Files.write(path, lines, options);
            System.out.println("Успешно сохранено " + buses.size() + " автобусов в '.csv': " + path.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Ошибка записи в '.csv' файл: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}