package by.coldynee.io;

import by.coldynee.model.Bus;
import by.coldynee.validation.BusValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManualInput implements InputSource {

    @Override
    public List<Bus> load(int count) {
        Scanner scanner = new Scanner(System.in);
        List<Bus> buses = new ArrayList<>();
        int kilometrage = 0;
        String model = "";
        int route = 0;

        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Ввод автобуса № " + (i + 1) + " ---");
            while (true) {
                try {
                    scanner.nextLine();
                    System.out.print("Введите километраж: ");
                    kilometrage = scanner.nextInt();
                    BusValidator.validateKilometrage(kilometrage);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Попробуйте снова");
                    scanner.nextLine();
                }
            }

            while (true) {
                try {
                    System.out.print("Введите Модель: ");
                    model = scanner.nextLine();
                    BusValidator.validateModelName(model);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Попробуйте снова");
                    scanner.nextLine();
                }
            }
            while (true) {
                try {
                    System.out.print("Введите номер пути: ");
                    route = scanner.nextInt();
                    BusValidator.validateRouteNumber(route);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Попробуйте снова");
                    scanner.nextLine();
                }
            }
            Bus bus = new Bus.BusBuilder()
                    .setKilometrage(kilometrage)
                    .setModelName(model)
                    .setRouteNumber(route)
                    .build();
            buses.add(bus);
        }
        return buses;
    }
}