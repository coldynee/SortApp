package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.Comparator;
import java.util.List;

//Сортировка вставками.
public class InsertionSortStrategy extends AbstractBusSortingStrategy {

    @Override
    public String getName() {
        return "Сортировка вставками";
    }

    @Override
    protected void sortList(List<Bus> buses, Comparator<Bus> comparator) {
        for (int i = 1; i < buses.size(); i++) {
            Bus current = buses.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(buses.get(j), current) > 0) {
                buses.set(j + 1, buses.get(j));
                j--;
            }

            buses.set(j + 1, current);
        }
    }
}
