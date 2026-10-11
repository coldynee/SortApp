package by.coldynee.io.output;

import by.coldynee.model.Bus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Стратегия записи данных в текстовый файл.
 * <p>
 * Поддерживает режим добавления данных.
 * </p>
 */
public class FileOutputStrategy implements OutputStrategy {

    private final String filePath;
    private final boolean appendMode;

    /**
     * Создает стратегию записи в файл.
     *
     * @param filePath   путь к файлу
     * @param appendMode true — дозапись в конец файла, false — перезапись
     */
    public FileOutputStrategy(String filePath, boolean appendMode) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }
        this.filePath = filePath.endsWith(".txt") ? filePath : filePath + ".txt";
        this.appendMode = appendMode;
    }

    @Override
    public void display(List<Bus> buses) {
        if (buses == null || buses.isEmpty()) {
            System.out.println("Список автобусов пуст, сохранение отменено");
            return;
        }

        Path path = Path.of(filePath);

        try {
            List<String> lines = buses.stream()
                    .map(Bus::toString)
                    .collect(Collectors.toList());

            if (appendMode) {
                Files.write(path, lines,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND,
                        StandardOpenOption.WRITE);
                System.out.println("Дописано " + buses.size() + " автобусов в файл: " + path.toAbsolutePath());
            } else {
                Files.write(path, lines);
                System.out.println("Сохранено " + buses.size() + " автобусов в файл: " + path.toAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}