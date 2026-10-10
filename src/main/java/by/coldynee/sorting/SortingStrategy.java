package by.coldynee.sorting;

import by.coldynee.model.Bus;
import java.util.Comparator;
import java.util.List;

public interface SortingStrategy {

    String getName();
    void sort(List<Bus> buses, Comparator<Bus> comparator);
    void sortByEvenKilometrage(List<Bus> buses);
}
