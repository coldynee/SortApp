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

        if (filePath == null || filePath.isBlank()) {
            System.out.println("Ошибка: Имя файла не может быть пустым.");
            return;
        }

        String cleanPath = filePath.trim();


        try {
            String fileName = Path.of(cleanPath).getFileName().toString().trim();
            if (fileName.equalsIgnoreCase(".txt") ||
                    fileName.equalsIgnoreCase(".csv") ||
                    fileName.equalsIgnoreCase(".json")) {
                System.out.println("Ошибка: Имя файла не может состоять только из расширения.");
                return;
            }
            Path path = Path.of(normalizePath(filePath, ".csv"));

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

            if (path.getParent() != null && !Files.exists(path.getParent())) {
                System.out.println("Ошибка: Папка '" + path.getParent().toAbsolutePath() + "' не существует.");
                return;
            }

            Files.write(path, lines, options);
            System.out.println("Успешно сохранено " + buses.size() + " автобусов в '.csv': " + path.toAbsolutePath());

        } catch (java.nio.file.AccessDeniedException e) {
            System.out.println("Ошибка доступа: Нет прав на запись в указанную папку.");
        } catch (java.nio.file.InvalidPathException e) {
            System.out.println("Ошибка: Указан некорректный путь к файлу.");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
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
            if (currentExt.equalsIgnoreCase(".txt") || currentExt.equalsIgnoreCase(".csv") || currentExt.equalsIgnoreCase(".json")) {
                System.out.println("Обнаружено неверное разрешение файла, изменено");
                return cleanPath.substring(0, lastDot) + expectedExtension;
            }
        }

        return cleanPath + expectedExtension;
    }
}