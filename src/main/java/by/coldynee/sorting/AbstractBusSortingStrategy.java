package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBusSortingStrategy implements SortingStrategy {

    @Override
    public final void sort(List<Bus> buses, Comparator<Bus> comparator) {
        Objects.requireNonNull(buses, "Список автобусов не должен быть null");
        Objects.requireNonNull(comparator, "Компаратор не должен быть null");
        sortList(buses, comparator);
    }

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
    protected abstract void sortList(List<Bus> buses, Comparator<Bus> comparator);
}
