package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Реализация {@link InputSource} для ручного ввода данных об автобусах через консоль.
 * <p>
 * Обеспечивает интерактивный пошаговый ввод с валидацией через {@link by.coldynee.model.Bus.BusBuilder}.
 * Пользователь может прервать процесс добавления в любой момент, введя "-1".
 * </p>
 */
public class ManualInput implements InputSource {

    private final Scanner scanner;
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
        List<Bus> buses = new ArrayList<>();
        System.out.println("\nДля выхода введите '-1' на любом этапе работы");

        for (int i = 0; i < count; i++) {

            System.out.println("\nВвод автобуса № " + (i + 1) );
            while (true) {
                System.out.print("Введите гос. номер (например, А123ВЕ77): ");
                String stateBusNumber = scanner.nextLine().trim();
                if (isExitCommand(stateBusNumber)) { return buses; }

                System.out.print("Введите модель: ");
                String modelName = scanner.nextLine().trim();
                if (isExitCommand(modelName)) { return buses; }

                System.out.print("Введите пробег (км): ");
                String kilometrageStr = scanner.nextLine().trim();
                if (isExitCommand(kilometrageStr)) { return buses; }

                try {
                    int kilometrage = Integer.parseInt(kilometrageStr);

                    Bus bus = new Bus.BusBuilder()
                            .setStateBusNumber(stateBusNumber)
                            .setModelName(modelName)
                            .setKilometrage(kilometrage)
                            .build();

                    buses.add(bus);
                    System.out.println("Автобус успешно добавлен!");

                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: Пробег должен быть целым числом.");
                    System.out.println("Попробуйте ввести данные для этого автобуса заново.\n");
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Ошибка валидации: " + e.getMessage());
                    System.out.println("Попробуйте ввести данные для этого автобуса заново.\n");
                }
            }
        }
        return buses;
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