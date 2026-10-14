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

        if (cleanPath.endsWith("/") || cleanPath.endsWith("\\") || cleanPath.endsWith(java.io.File.separator)) {
            System.out.println("Ошибка: Вы указали путь к папке, а не к файлу. Добавьте имя файла.");
            return;
        }

        String fileName;
        int lastSlashIndex = Math.max(cleanPath.lastIndexOf('/'), cleanPath.lastIndexOf('\\'));

        if (lastSlashIndex == -1) {
            fileName = cleanPath;
        } else {
            fileName = cleanPath.substring(lastSlashIndex + 1);
        }
        if (fileName.trim().isEmpty()) {
            System.out.println("Ошибка: Имя файла не может быть пустым.");
            return;
        }
        fileName = fileName.trim();
        if (fileName.startsWith(".") && fileName.indexOf('.', 1) == -1) {
            System.out.println("Ошибка: Имя файла не может состоять только из расширения.");
            return;
        }

        try {
            Path path = Path.of(normalizePath(cleanPath, ".csv"));

            if (path.getParent() != null && !Files.exists(path.getParent())) {
                System.out.println("Ошибка: Папка '" + path.getParent().toAbsolutePath() + "' не существует.");
                return;
            }

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
                System.out.println("Обнаружено неверное разрешение файла, изменено на '" + expectedExtension + "'");
                return cleanPath.substring(0, lastDot) + expectedExtension;
            }
        }

        return cleanPath + expectedExtension;
    }
}