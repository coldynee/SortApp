package by.coldynee.io.loaders;

import by.coldynee.io.InputSource;
import by.coldynee.model.Bus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Реализация {@link InputSource} для загрузки данных об автобусах из JSON-файла.
 * <p>
 * Ожидаемый формат файла: JSON-массив объектов, где каждый объект содержит поля:
 * "stateBusNumber" (String), "modelName" (String), "kilometrage" (int).
 * </p>
 * <p>
 * Метод устойчив к ошибкам: невалидные объекты в массиве пропускаются с выводом
 * предупреждения, а процесс чтения продолжается. Используется библиотека Jackson.
 * </p>
 */
public class JsonFileInput implements InputSource {
    private final String filePath;
    private final ObjectMapper objectMapper;

    /**
     * Создает загрузчик для указанного JSON-файла.
     * <p>
     * Если переданное имя файла не заканчивается на ".json", расширение добавляется автоматически.
     * </p>
     *
     * @param filePath имя или относительный/абсолютный путь к файлу
     * @throws IllegalArgumentException если путь null или состоит только из пробелов
     */
    public JsonFileInput(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }
        String cleanPath = filePath.trim();
        if (!cleanPath.toLowerCase().endsWith(".json")) {
            cleanPath += ".json";
        }
        this.filePath = cleanPath;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Читает JSON-файл и создает список валидных автобусов.
     *
     * @param count максимальное количество автобусов для загрузки. Если count <= 0, загружаются все.
     * @return список успешно созданных объектов {@link Bus}
     */
    @Override
    public List<Bus> load(int count) {
        List<Bus> buses = new ArrayList<>();
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            System.err.println("\nОшибка: файл не найден (" + path.toAbsolutePath() + ")");
            return buses;
        }
        try {
            JsonNode rootNode = objectMapper.readTree(path.toFile());

            if (!rootNode.isArray()) {
                System.err.println("Ошибка: Ожидался JSON-массив [...], но получен другой тип данных.");
                return buses;
            }

            System.out.println("\nЧтение из файла " + filePath);
            AtomicInteger index = new AtomicInteger(0);

            return StreamSupport.stream(rootNode.spliterator(), false)
                    .map(node -> new Object() {
                        JsonNode data = node;
                        int num = index.incrementAndGet();
                    })
                    .limit(count > 0 ? count : Long.MAX_VALUE)
                    .map(obj -> parseJsonNode(obj.data, obj.num))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException | java.io.UncheckedIOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Парсит один JSON-объект в Bus.
     * Возвращает null, если объект невалиден, и выводит ошибку с его номером.
     *
     * @param node JSON-узел с данными
     * @param num  порядковый номер объекта в массиве (начиная с 1)
     * @return объект Bus или null
     */
    private Bus parseJsonNode(JsonNode node, int num) {
        try {
            String stateBusNumber = node.has("stateBusNumber") ? node.get("stateBusNumber").asText() : "";
            String modelName = node.has("modelName") ? node.get("modelName").asText() : "";
            String kilometrageStr = node.has("kilometrage") ? node.get("kilometrage").asText() : "";

            return new Bus.BusBuilder()
                    .setStateBusNumber(stateBusNumber)
                    .setModelName(modelName)
                    .setKilometrage(Integer.parseInt(kilometrageStr))
                    .build();

        } catch (NumberFormatException e) {
            System.err.println("Обнаружен неверный пробег у элемента" + num + ", (должен быть целым числом)");
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации у элемента " + num + ": " + e.getMessage());
            return null;
        }
    }
}
