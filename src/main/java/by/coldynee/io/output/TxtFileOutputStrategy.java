package by.coldynee.io.output;

import by.coldynee.model.Bus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Стратегия записи данных в текстовый файл (.txt).
 * Использует Stream API для преобразования объектов в строки.
 */
public class TxtFileOutputStrategy implements OutputStrategy {

    @Override
    public void save(List<Bus> buses, String filePath, boolean append) {
        if (buses == null || buses.isEmpty()) {
            System.out.println("Список пуст, сохранение отменено.");
            return;
        }

        Path path = Path.of(filePath.endsWith(".txt") ? filePath : filePath + ".txt");

        try {
            List<String> lines = buses.stream()
                    .map(Bus::toString)
                    .collect(Collectors.toList());

            StandardOpenOption[] options = append
                    ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE}
                    : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};

            Files.write(path, lines, options);
            System.out.println("Успешно сохранено " + buses.size() + " автобусов в '.txt': " + path.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Ошибка записи в '.txt' файл: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}