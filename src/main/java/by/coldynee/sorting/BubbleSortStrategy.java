package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.Comparator;
import java.util.List;

// Сортировка пузырьком
public class BubbleSortStrategy extends AbstractBusSortingStrategy {

    @Override
    public String getName() {
        return "Сортировка пузырьком";
    }

    @Override
    protected void sortList(List<Bus> buses, Comparator<Bus> comparator) {
        for (int end = buses.size() - 1; end > 0; end--) {
            boolean swapped = false;

            for (int i = 0; i < end; i++) {
                if (comparator.compare(buses.get(i), buses.get(i + 1)) > 0) {
                    Bus temp = buses.get(i);
                    buses.set(i, buses.get(i + 1));
                    buses.set(i + 1, temp);
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }
}
