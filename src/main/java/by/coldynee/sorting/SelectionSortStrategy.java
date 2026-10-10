package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.Comparator;
import java.util.List;

/**
 * Стратегия сортировки списка автобусов методом <b>Selection Sort</b>.
 * <p>
 * Алгоритм пошагово находит минимальный элемент в неотсортированной части списка
 * и меняет его местами с первым элементом этой части.
 * </p>
 * <ul>
 *   <li><b>Временная сложность:</b> O(n²) во всех случаях (лучший, средний, худший).</li>
 *   <li><b>Устойчивость:</b> Стандартная реализация не является устойчивой из-за обменов.</li>
 *   <li><b>Использование памяти:</b> O(1) (сортировка на месте).</li>
 * </ul>
 */
public class SelectionSortStrategy extends AbstractBusSortingStrategy {

    @Override
    public String getName() {
        return "Сортировка выбором";
    }

    @Override
    protected void sortList(List<Bus> buses, Comparator<Bus> comparator) {
        for (int i = 0; i < buses.size() - 1; i++) {
            int minIndex = i;

            // Поиск индекса минимального элемента в оставшейся части списка
            for (int j = i + 1; j < buses.size(); j++) {
                if (comparator.compare(buses.get(j), buses.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }

            // Обмен местами, если минимум не на своем месте
            if (minIndex != i) {
                Bus temp = buses.get(i);
                buses.set(i, buses.get(minIndex));
                buses.set(minIndex, temp);
            }
        }
    }
}
