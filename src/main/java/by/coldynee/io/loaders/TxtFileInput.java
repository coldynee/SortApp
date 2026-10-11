package by.coldynee.io.loaders;

import by.coldynee.io.InputSource;
import by.coldynee.model.Bus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация {@link InputSource} для загрузки данных об автобусах из текстового файла.
 * <p>
 * Ожидаемый формат файла: каждая строка содержит данные об одном автобусе,
 * разделенные точкой с запятой (;) в порядке: ГосНомер;Модель;Пробег.
 * Пустые строки и строки, начинающиеся с '#' или '/', игнорируются как комментарии.
 * </p>
 */
public class TxtFileInput implements InputSource {

    private final String filePath;

    /**
     * Создает загрузчик для указанного файла.
     * <p>
     * Если переданное имя файла не заканчивается на ".txt", расширение добавляется автоматически
     * для удобства пользователя.
     * </p>
     *
     * @param filePath имя или относительный/абсолютный путь к файлу
     * @throws IllegalArgumentException если путь null или состоит только из пробелов
     */
    public TxtFileInput(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }
        String cleanPath = filePath.trim();
        if (!cleanPath.toLowerCase().endsWith(".txt")) {
            cleanPath += ".txt";
        }
        this.filePath = cleanPath;
    }

    @Override
    public List<Bus> load(int count) {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            System.err.println("\nОшибка: файл не найден по пути: " + path.toAbsolutePath());
            return List.of();
        }

        try (Stream<String> lines = Files.lines(path)) {
            System.out.println("\nЧтение из файла " + path.toAbsolutePath());

            AtomicInteger lineNumber = new AtomicInteger(0);
            return lines
                    .map(line -> new Object() {
                        String text = line.trim();
                        int num = lineNumber.incrementAndGet();
                    })
                    .filter(obj -> !obj.text.isEmpty() && !obj.text.startsWith("#") && !obj.text.startsWith("/"))
                    .limit(count > 0 ? count : Long.MAX_VALUE)
                    .map(obj -> parseLine(obj.text, obj.num))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException | java.io.UncheckedIOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Парсит одну строку txt-файла в объект Bus.
     * Возвращает null, если строка невалидна, и выводит ошибку с номером строки.
     *
     * @param line       содержимое строки
     * @param num истинный номер строки в файле
     * @return объект Bus или null
     */
    private Bus parseLine(String line, int num) {
        try {
            String[] parts = line.split(";");
            if (parts.length != 3) {
                System.err.println("Обнаружен неверный формат на строке " + num + ", (ожидается 3 поля через ';'): " + line);
                return null;
            }

            return new Bus.BusBuilder()
                    .setStateBusNumber(parts[0].trim())
                    .setModelName(parts[1].trim())
                    .setKilometrage(Integer.parseInt(parts[2].trim()))
                    .build();

        } catch (NumberFormatException e) {
            System.err.println("Обнаружен неверный пробег на строке" + num + ", (должен быть целым числом)");
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации на строке " + num + " (" + line + "): " + e.getMessage());
            return null;
        }
    }
}
