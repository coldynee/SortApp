package by.coldynee.sort;

import by.coldynee.model.Bus;

import java.util.Comparator;
import java.util.List;

/**
 * Стратегия сортировки списка автобусов.
 */
public interface SortStrategy {
    void sort(List<Bus> list, Comparator<Bus> comparator);
}
