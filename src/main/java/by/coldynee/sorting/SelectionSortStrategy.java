package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.Comparator;
import java.util.List;

// Сортировка выбором
public class SelectionSortStrategy extends AbstractBusSortingStrategy {

    @Override
    public String getName() {
        return "Сортировка выбором";
    }

    @Override
    protected void sortList(List<Bus> buses, Comparator<Bus> comparator) {
        for (int i = 0; i < buses.size() - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < buses.size(); j++) {
                if (comparator.compare(buses.get(j), buses.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                Bus temp = buses.get(i);
                buses.set(i, buses.get(minIndex));
                buses.set(minIndex, temp);
            }
        }
    }
}
