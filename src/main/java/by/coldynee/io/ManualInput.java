package by.coldynee.io;

import by.coldynee.model.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManualInput implements InputSource {

    private final Scanner scanner;

    public ManualInput(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Bus> load() {
        System.out.print("Сколько автобусов вы хотите добавить вручную? (по умолчанию 1): ");
        String input = scanner.nextLine().trim();
        int count = 1;

        if (!input.isEmpty()) {
            try {
                count = Integer.parseInt(input);
                if (count <= 0) count = 1;
            } catch (NumberFormatException e) {
                System.out.println("Введено некорректное число, будет запрошен 1 автобус.");
            }
        }
        return load(count);
    }

    @Override
    public List<Bus> load(int count) {
        List<Bus> buses = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Ввод автобуса № " + (i + 1) + " ---");

            while (true) {
                System.out.print("Введите гос. номер (например, А123БЕ77): ");
                String stateBusNumber = scanner.nextLine().trim();

                System.out.print("Введите модель: ");
                String modelName = scanner.nextLine().trim();

                System.out.print("Введите пробег (км): ");
                String kilometrageStr = scanner.nextLine().trim();

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
}