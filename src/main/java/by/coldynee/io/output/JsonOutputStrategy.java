package by.coldynee.io.output;

import by.coldynee.model.Bus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Стратегия записи данных в JSON-файл (.json).
 * <p>
 * ВАЖНО: JSON не поддерживает прямую дозапись (append) на уровень байтов,
 * так как это нарушит структуру массива [...].
 * Поэтому режим "append" реализуется логически: читаем существующий список,
 * добавляем новые элементы и перезаписываем файл.
 * </p>
 */
public class JsonOutputStrategy implements OutputStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void save(List<Bus> buses, String filePath, boolean append) {
        if (buses == null || buses.isEmpty()) {
            System.out.println("Список пуст, сохранение отменено.");
            return;
        }

        Path path = Path.of(filePath.endsWith(".json") ? filePath : filePath + ".json");
        List<Bus> finalListToSave = new ArrayList<>(buses);

        try {
            if (append && Files.exists(path)) {
                CollectionType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Bus.class);
                List<Bus> existingBuses = objectMapper.readValue(path.toFile(), listType);
                finalListToSave.addAll(0, existingBuses);
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), finalListToSave);

            System.out.println("Успешно сохранено " + buses.size() + " автобусов в '.json': " + path.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Ошибка записи в '.json' файл: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
