package by.coldynee.io.loaders;

import by.coldynee.io.InputSource;
import by.coldynee.model.Bus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация {@link InputSource} для загрузки данных об автобусах из CSV-файла.
 * <p>
 * Ожидаемый формат файла: каждая строка содержит данные об одном автобусе,
 * разделенные запятой (,) в порядке: ГосНомер,Модель,Пробег.
 * Первая строка может быть заголовком (будет пропущена автоматически, если не содержит ни одной цифры).
 * Пустые строки игнорируются.
 * </p>
 * <p>
 * <b>Важно:</b> Данная реализация использует простое разделение по запятой.
 * Если название модели содержит запятую (например, "Volvo, модель X"), строка будет считана некорректно.
 * Для сложных CSV с кавычками рекомендуется использовать библиотеки вроде OpenCSV.
 * </p>
 */
public class CsvFileInput implements InputSource {
    private final String filePath;

    /**
     * Создает загрузчик для указанного файла.
     * <p>
     * Если переданное имя файла не заканчивается на ".csv", расширение добавляется автоматически
     * для удобства пользователя.
     * </p>
     *
     * @param filePath имя или относительный/абсолютный путь к файлу
     * @throws IllegalArgumentException если путь null или состоит только из пробелов
     */
    public CsvFileInput(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }
        String cleanPath = filePath.trim();
        if (!cleanPath.toLowerCase().endsWith(".csv")) {
            cleanPath += ".csv";
        }
        this.filePath = cleanPath;
    }

    /**
     * Читает файл и создает список валидных автобусов.
     * <p>
     * Метод устойчив к ошибкам: невалидные строки пропускаются с выводом предупреждения в {@link System#err},
     * а процесс чтения продолжается до конца файла или достижения лимита.
     * </p>
     *
     * @param count максимальное количество автобусов для загрузки. Если count <= 0, загружаются все строки.
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
            List<String> lines = Files.readAllLines(path);
            System.out.println("\nЧтение из файла " + filePath + ", Обнаружено строк: " + lines.size());
            int countToRead = (count <= 0) ? lines.size() : Math.min(lines.size(), count);

            for (int i = 0; i < countToRead; i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    continue;
                }

                //Пропуск строки заголовка
                if (i == 0 && !line.matches(".*\\d+.*")) {
                    continue;
                }

                try {
                    String[] parts = line.split(",");

                    if (parts.length != 3) {
                        System.err.println("Обнаружен неверный формат на строке " + (i+1) + " (ожидается 3 поля через ','), строка пропущена");
                        continue;
                    }

                    String stateBusNumber = parts[0].trim();
                    String modelName = parts[1].trim();
                    String kilometrageStr = parts[2].trim();

                    Bus bus = new Bus.BusBuilder()
                            .setStateBusNumber(stateBusNumber)
                            .setModelName(modelName)
                            .setKilometrage(Integer.parseInt(kilometrageStr))
                            .build();

                    buses.add(bus);
                } catch (NumberFormatException e) {
                    System.err.println("Обнаружен неверно указанный пробег на строке " + (i+1) +  ", должно быть целое число, строка пропущена");
                } catch (IllegalArgumentException e) {
                    System.err.println("Обнаружена ошибка валидации на строке " + (i+1) + ", " + e.getMessage() + ", строка пропущена");
                } catch (Exception e) {
                    System.err.println("Обнаружена неизвестная ошибка на строке " + (i+1) + ", строка пропущена");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла" + e.getMessage());
        }
        System.out.println("Загрузка из " + filePath + " завершена! \nУспешно загружено: " + buses.size());
        return buses;
    }
}
