package by.coldynee.sorting;

import by.coldynee.model.Bus;

import java.util.Comparator;
import java.util.List;

/**
 * Стратегия сортировки списка автобусов методом <b>Bubble Sort</b>.
 * <p>
 * Алгоритм многократно проходит по списку, сравнивая соседние элементы и меняя их местами,
 * если они находятся в неправильном порядке относительно заданного компаратора.
 * Проходы повторяются до тех пор, пока список не будет полностью отсортирован.
 * </p>
 * <p>
 * <b>Оптимизация:</b> Данная реализация включает флаг {@code swapped} для ранней остановки.
 * Если за полный проход по списку не было произведено ни одного обмена, алгоритм делает
 * вывод, что список уже отсортирован, и завершает работу досрочно.
 * </p>
 * <ul>
 *   <li><b>Временная сложность:</b> O(n²) в среднем и худшем случае, O(n) в лучшем (уже отсортированный список).</li>
 *   <li><b>Устойчивость:</b> Алгоритм является устойчивым (не меняет относительный порядок равных элементов).</li>
 *   <li><b>Использование памяти:</b> O(1) (сортировка на месте).</li>
 * </ul>
 */
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

            // Если обменов не было, список уже отсортирован, выходим из цикла
            if (!swapped) {
                break;
            }
        }
    }
}
