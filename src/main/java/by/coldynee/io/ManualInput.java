package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Реализация {@link InputSource} для ручного ввода данных об автобусах через консоль.
 * <p>
 * Обеспечивает интерактивный пошаговый ввод с валидацией через {@link by.coldynee.model.Bus.BusBuilder}.
 * Пользователь может прервать процесс добавления в любой момент, введя "-1".
 * </p>
 */
public class ManualInput implements InputSource {

    private final Scanner scanner;

    /**
     * Создает экземпляр ручного ввода.
     *
     * @param scanner объект Scanner для чтения ввода из консоли
     */
    public ManualInput(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Запускает процесс интерактивного добавления автобусов.
     *
     * @param count количество автобусов, которые необходимо добавить
     * @return список успешно созданных и валидированных объектов {@link Bus}.
     * Если пользователь ввел "-1", возвращает частично заполненный список.
     */
    @Override
    public List<Bus> load(int count) {
        if (count <= 0) {
            System.out.println("Количество для ручного ввода должно быть больше 0");
            return List.of();
        }

        List<Bus> buses = new ArrayList<>();
        System.out.println("\nДля выхода и сохранения введите '-1' на любом этапе работы");
        return IntStream.range(0, count)
                .mapToObj(i -> readSingleBus(i + 1))
                .takeWhile(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Читает данные одного автобуса из консоли с возможностью повторной попытки при ошибке.
     *
     * @param number порядковый номер автобуса (для отображения пользователю)
     * @return созданный объект Bus, или null, если пользователь ввел команду выхода
     */
    private Bus readSingleBus(int number) {
        System.out.println("\nВвод автобуса № " + number);

        while (true) {
            System.out.print("Введите гос. номер (например, А123ВЕ77): ");
            String stateBusNumber = scanner.nextLine().trim();
            if (isExitCommand(stateBusNumber)) return null;

            System.out.print("Введите модель: ");
            String modelName = scanner.nextLine().trim();
            if (isExitCommand(modelName)) return null;

            System.out.print("Введите пробег (км): ");
            String kilometrageStr = scanner.nextLine().trim();
            if (isExitCommand(kilometrageStr)) return null;

            try {
                int kilometrage = Integer.parseInt(kilometrageStr);

                Bus bus = new Bus.BusBuilder()
                        .setStateBusNumber(stateBusNumber)
                        .setModelName(modelName)
                        .setKilometrage(kilometrage)
                        .build();

                System.out.println("Автобус успешно добавлен!");
                return bus;

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Пробег должен быть целым числом.");
                System.out.println("Попробуйте ввести данные для этого автобуса заново.\n");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка валидации: " + e.getMessage());
                System.out.println("Попробуйте ввести данные для этого автобуса заново.\n");
            }
        }
    }

    /**
     * Проверяет, является ли введенная строка командой выхода.
     *
     * @param input введенная пользователем строка
     * @return true, если строка равна "-1", иначе false
     */
    private boolean isExitCommand(String input) {
        return "-1".equals(input);
    }
}