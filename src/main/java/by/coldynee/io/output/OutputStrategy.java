package by.coldynee.io.output;

import by.coldynee.model.Bus;

import java.util.List;

/**
 * Стратегия вывода данных об автобусах.
 */
public interface OutputStrategy {
    /**
     * Выводит список автобусов.
     *
     * @param buses список для вывода
     */
    void display(List<Bus> buses);
}