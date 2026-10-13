package by.coldynee.io.output;

import by.coldynee.model.Bus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Стратегия записи данных в JSON-файл (.json).
 */
public class JsonOutputStrategy implements OutputStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

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

        Path path = Path.of(normalizePath(filePath, ".json"));

        try {
            if (append && Files.exists(path)) {
                String existingContent = Files.readString(path, StandardCharsets.UTF_8).trim();

                if (existingContent.endsWith("]")) {
                    existingContent = existingContent.substring(0, existingContent.length() - 1).trim();

                    String newContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(buses);

                    newContent = newContent.trim();
                    if (newContent.startsWith("[") && newContent.endsWith("]")) {
                        newContent = newContent.substring(1, newContent.length() - 1).trim();
                    }

                    String finalJson;
                    if (existingContent.endsWith("[")) {
                        finalJson = "[" + newContent + "]";
                    } else {
                        finalJson = existingContent + ",\n  " + newContent + "\n]";
                    }

                    Files.writeString(path, finalJson, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
                    System.out.println("Успешно дозаписано " + buses.size() + " автобусов в '.json': " + path.toAbsolutePath());
                    return;
                }
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), buses);
            System.out.println("Успешно сохранено " + buses.size() + " автобусов в '.json': " + path.toAbsolutePath());

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
