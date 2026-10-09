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

            System.out.println("\nЧтение из файла " + filePath + ", Обнаружено объектов: " + rootNode.size());
            int countToRead = (count <= 0) ? rootNode.size() : Math.min(rootNode.size(), count);

            for (int i = 0; i < countToRead; i++) {
                JsonNode node = rootNode.get(i);

                try {
                    String stateBusNumber = node.has("stateBusNumber") ? node.get("stateBusNumber").asText() : "";
                    String modelName = node.has("modelName") ? node.get("modelName").asText() : "";
                    String kilometrageStr = node.has("kilometrage") ? node.get("kilometrage").asText() : "";
                    int kilometrage = Integer.parseInt(kilometrageStr);

                    Bus bus = new Bus.BusBuilder()
                            .setStateBusNumber(stateBusNumber)
                            .setModelName(modelName)
                            .setKilometrage(kilometrage)
                            .build();

                    buses.add(bus);
                } catch (NumberFormatException e) {
                    System.err.println("Обнаружен неверно указанный пробег в объекте №" + (i+1) +  ", должно быть целое число, объект пропущен");
                } catch (IllegalArgumentException e) {
                    System.err.println("Обнаружена ошибка валидации в объекте №" + (i+1) + ", " + e.getMessage() + ", объект пропущен");
                } catch (Exception e) {
                    System.err.println("Обнаружена неизвестная ошибка в объекте №" + (i+1) + ", объект пропущен");
                }
            }

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            System.err.println("Ошибка: Файл не является валидным JSON - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }

        return buses;

    }
}
