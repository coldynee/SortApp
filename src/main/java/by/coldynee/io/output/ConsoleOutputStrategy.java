package by.coldynee.io.output;

import by.coldynee.model.Bus;

import java.util.List;

/**
 * Стратегия вывода данных в консоль.
 */
public class ConsoleOutputStrategy implements OutputStrategy {

    @Override
    public void display(List<Bus> buses) {
        if (buses == null || buses.isEmpty()) {
            System.out.println("Список автобусов пуст");
            return;
        }
        System.out.println("\nСписок автобусов");
        for (int i = 0; i < buses.size(); i++) {
            System.out.println((i + 1) + ". " + buses.get(i));
        }
    }
}