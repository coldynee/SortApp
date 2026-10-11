package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.Comparator;
import java.util.List;

/**
 * Стратегия сортировки списка автобусов методом <b>Insertion Sort</b>.
 * <p>
 * Алгоритм строит отсортированную последовательность, по одному элементу за шаг,
 * выбирая очередной элемент и вставляя его на подходящее место в уже отсортированной части.
 * </p>
 * <ul>
 *   <li><b>Временная сложность:</b> O(n²) в среднем и худшем случае, O(n) в лучшем (почти отсортированный массив).</li>
 *   <li><b>Устойчивость:</b> Алгоритм является устойчивым (не меняет порядок равных элементов).</li>
 *   <li><b>Использование памяти:</b> O(1) (сортировка на месте).</li>
 * </ul>
 */
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

            // Сдвигаем элементы, которые больше текущего, вправо
            while (j >= 0 && comparator.compare(buses.get(j), current) > 0) {
                buses.set(j + 1, buses.get(j));
                j--;
            }

            // Вставляем текущий элемент на найденную позицию
            buses.set(j + 1, current);
        }
    }
}
