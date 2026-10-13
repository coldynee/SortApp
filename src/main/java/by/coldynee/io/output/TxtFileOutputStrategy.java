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

        if (filePath == null || filePath.isBlank()) {
            System.err.println("Ошибка: Имя файла не может быть пустым.");
            return;
        }

        String cleanPath = filePath.trim();

        if (cleanPath.equals(".txt") || cleanPath.equals(".csv") || cleanPath.equals(".json")) {
            System.err.println("Ошибка: Имя файла не может состоять только из расширения.");
            return;
        }

        Path path = Path.of(normalizePath(filePath, ".txt"));

        try {
            List<String> lines = buses.stream()
                    .map(bus -> String.format("%s;%s;%d",
                            bus.getStateBusNumber(),
                            bus.getModelName(),
                            bus.getKilometrage()))
                    .collect(Collectors.toList());

            StandardOpenOption[] options = append
                    ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE}
                    : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};

            Files.write(path, lines, options);
            System.out.println("Успешно сохранено " + buses.size() + " автобусов в '.txt': " + path.toAbsolutePath());

        } catch (java.nio.file.AccessDeniedException e) {
            System.err.println("Ошибка доступа: Нет прав на запись в указанную папку.");
        } catch (java.nio.file.InvalidPathException e) {
            System.err.println("Ошибка: Указан некорректный путь к файлу.");
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private String normalizePath(String filePath, String expectedExtension) {
        String cleanPath = filePath.trim();

        if (cleanPath.toLowerCase().endsWith(expectedExtension)) {
            return cleanPath;
        }

        int lastDot = cleanPath.lastIndexOf('.');
        if (lastDot > 0) {
            String currentExt = cleanPath.substring(lastDot).toLowerCase();
            if (currentExt.equals(".txt") || currentExt.equals(".csv") || currentExt.equals(".json")) {
                System.out.println("Обнаружено неверное разрешение файла, изменено");
                return cleanPath.substring(0, lastDot) + expectedExtension;
            }
        }

        return cleanPath + expectedExtension;
    }
}