package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Абстрактная базовая стратегия сортировки списка автобусов.
 * <p>
 * Реализует паттерн <b>Template Method</b>:
 * финальные методы {@link #sort(List, Comparator)} и {@link #sortByEvenKilometrage(List)}
 * обеспечивают базовую валидацию входных данных (защита от null), а затем делегируют
 * логику сортировки защищенному абстрактному методу {@link #sortList(List, Comparator)},
 * который должны реализовать классы-наследники.
 * </p>
 */
public abstract class AbstractBusSortingStrategy implements SortingStrategy {

    /**
     * Сортирует переданный список автобусов согласно заданному компаратору.
     * <p>
     * Метод является финальным и гарантирует проверку входных данных на null
     * перед вызовом алгоритма сортировки.
     * </p>
     *
     * @param buses      список автобусов для сортировки (изменяется внутри)
     * @param comparator компаратор, определяющий порядок элементов
     * @throws NullPointerException если список или компаратор равны null
     */
    @Override
    public final void sort(List<Bus> buses, Comparator<Bus> comparator) {
        Objects.requireNonNull(buses, "Список автобусов не должен быть null");
        Objects.requireNonNull(comparator, "Компаратор не должен быть null");
        sortList(buses, comparator);
    }

    /**
     * Специфичная операция: находит все автобусы с четным значением пробега,
     * сортирует только их между собой по возрастанию пробега, и возвращает
     * на их исходные позиции в общем списке.
     * <p>
     * Остальные автобусы (с нечетным пробегом) остаются на своих местах и не меняют порядок.
     * </p>
     *
     * @param buses исходный список автобусов (изменяется на месте)
     * @throws NullPointerException если список или любой из его элементов равен null
     */
    @Override
    public final void sortByEvenKilometrage(List<Bus> buses) {
        Objects.requireNonNull(buses, "Список автобусов не должен быть null");

        List<Integer> evenPositions = new ArrayList<>();
        List<Bus> evenBuses = new ArrayList<>();

        for (int i = 0; i < buses.size(); i++) {
            Bus bus = Objects.requireNonNull(buses.get(i), "Список не должен содержать null");
            if (bus.getKilometrage() % 2 == 0) {
                evenPositions.add(i);
                evenBuses.add(bus);
            }
        }
        sortList(evenBuses, Comparator.comparingInt(Bus::getKilometrage));

        for (int i = 0; i < evenPositions.size(); i++) {
            buses.set(evenPositions.get(i), evenBuses.get(i));
        }
    }

    /**
     * Абстрактный метод, реализующий конкретный алгоритм сортировки.
     * <p>
     * Вызывается из финальных методов после прохождения всех проверок валидности.
     * </p>
     *
     * @param buses      список для сортировки
     * @param comparator компаратор для сравнения элементов
     */
    protected abstract void sortList(List<Bus> buses, Comparator<Bus> comparator);
}
