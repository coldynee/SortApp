package by.coldynee.sorting;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Общие тесты для всех стратегий сортировки автобусов.
 * <p>
 * Наследники только создают конкретную стратегию и сообщают её ожидаемое название
 * и устойчивость. Все проверки выполняются для каждой стратегии одинаково.
 * </p>
 */
abstract class AbstractSortingStrategyTest {

    protected SortingStrategy strategy;
    protected List<Bus> buses;

    /**
     * @return тестируемая стратегия
     */
    protected abstract SortingStrategy createStrategy();

    /**
     * @return ожидаемое значение {@link SortingStrategy#getName()}
     */
    protected abstract String getExpectedName();

    /**
     * @return {@code true}, если алгоритм устойчивый (не меняет порядок равных элементов)
     */
    protected abstract boolean isStable();

    @BeforeEach
    void setUp() {
        strategy = createStrategy();

        // Заведомо несортированный список валидных автобусов
        buses = new ArrayList<>();
        buses.add(createBus("А333АА77", "Volvo", 300000));
        buses.add(createBus("А111АА77", "Mercedes", 100000));
        buses.add(createBus("А222АА77", "BMW", 200000));
    }

    // ---------- Сортировка по трём полям ----------

    @Test
    @DisplayName("Сортировка по пробегу (возрастание)")
    void testSortByKilometrage() {
        strategy.sort(buses, BusComparators.BY_KILOMETRAGE);

        assertEquals(List.of(100000, 200000, 300000), getKilometrages(buses));
    }

    @Test
    @DisplayName("Сортировка по названию модели (без учета регистра)")
    void testSortByModelName() {
        // "audi" в нижнем регистре: при обычном сравнении строк оказалась бы в конце
        List<Bus> list = new ArrayList<>();
        list.add(createBus("А111АА77", "Volvo", 100));
        list.add(createBus("А222АА77", "audi", 200));
        list.add(createBus("А333АА77", "BMW", 300));

        strategy.sort(list, BusComparators.BY_MODEL_NAME);

        assertEquals(List.of("audi", "BMW", "Volvo"), getModelNames(list));
    }

    @Test
    @DisplayName("Сортировка по гос. номеру (возрастание)")
    void testSortByStateBusNumber() {
        strategy.sort(buses, BusComparators.BY_STATE_BUS_NUMBER);

        assertEquals(List.of("А111АА77", "А222АА77", "А333АА77"), getStateNumbers(buses));
    }

    // ---------- Граничные случаи ----------

    @Test
    @DisplayName("Сортировка пустого списка не должна вызывать ошибок")
    void testSortEmptyList() {
        List<Bus> emptyList = new ArrayList<>();

        assertDoesNotThrow(() -> strategy.sort(emptyList, BusComparators.BY_KILOMETRAGE));
        assertTrue(emptyList.isEmpty());
    }

    @Test
    @DisplayName("Сортировка списка из одного элемента")
    void testSortSingleElement() {
        List<Bus> list = createBuses(500);
        Bus only = list.get(0);

        strategy.sort(list, BusComparators.BY_KILOMETRAGE);

        assertEquals(1, list.size());
        assertSame(only, list.get(0));
    }

    @Test
    @DisplayName("Уже отсортированный список остаётся без изменений")
    void testSortAlreadySortedList() {
        List<Bus> list = createBuses(100, 200, 300, 400);

        strategy.sort(list, BusComparators.BY_KILOMETRAGE);

        assertEquals(List.of(100, 200, 300, 400), getKilometrages(list));
    }

    @Test
    @DisplayName("Список в обратном порядке (худший случай)")
    void testSortReversedList() {
        List<Bus> list = createBuses(500, 400, 300, 200, 100);

        strategy.sort(list, BusComparators.BY_KILOMETRAGE);

        assertEquals(List.of(100, 200, 300, 400, 500), getKilometrages(list));
    }

    @Test
    @DisplayName("Сортировка списка с одинаковыми значениями пробега")
    void testSortWithEqualKeys() {
        List<Bus> list = createBuses(300, 100, 300, 100, 200);

        strategy.sort(list, BusComparators.BY_KILOMETRAGE);

        assertEquals(List.of(100, 100, 200, 300, 300), getKilometrages(list));
    }

    @Test
    @DisplayName("Сортировка меняет переданный список на месте и не теряет элементы")
    void testSortInPlaceKeepsAllElements() {
        List<Bus> before = new ArrayList<>(buses);

        strategy.sort(buses, BusComparators.BY_KILOMETRAGE);

        assertEquals(before.size(), buses.size());
        assertTrue(buses.containsAll(before));
        assertNotEquals(before, buses, "Порядок в переданном списке должен измениться");
    }

    @Test
    @DisplayName("Устойчивость: равные элементы сохраняют исходный порядок")
    void testSortStability() {
        assumeTrue(isStable(), "Алгоритм не является устойчивым, тест пропущен");

        List<Bus> original = createBuses(100, 100, 50, 100);
        List<Bus> list = new ArrayList<>(original);

        strategy.sort(list, BusComparators.BY_KILOMETRAGE);

        List<Bus> expected = List.of(original.get(2), original.get(0), original.get(1), original.get(3));
        assertEquals(expected, list);
    }

    // ---------- Проверка входных данных ----------

    @Test
    @DisplayName("sort: null вместо списка вызывает NullPointerException")
    void testSortNullList() {
        assertThrows(NullPointerException.class, () -> strategy.sort(null, BusComparators.BY_KILOMETRAGE));
    }

    @Test
    @DisplayName("sort: null вместо компаратора вызывает NullPointerException")
    void testSortNullComparator() {
        assertThrows(NullPointerException.class, () -> strategy.sort(buses, null));
    }

    @Test
    @DisplayName("getName() возвращает корректное название алгоритма")
    void testGetName() {
        assertEquals(getExpectedName(), strategy.getName());
    }

    // ---------- Доп. задание 1: сортировка только чётного пробега ----------

    @Test
    @DisplayName("sortByEvenKilometrage: чётные сортируются, нечётные остаются на местах")
    void testSortByEvenKilometrage() {
        List<Bus> list = createBuses(400, 3, 200, 7, 100, 5, 300);

        strategy.sortByEvenKilometrage(list);

        assertEquals(List.of(100, 3, 200, 7, 300, 5, 400), getKilometrages(list));
    }

    @Test
    @DisplayName("sortByEvenKilometrage: объекты с нечётным пробегом не заменяются другими")
    void testSortByEvenKilometrageKeepsOddObjects() {
        List<Bus> list = createBuses(400, 3, 200, 7, 100, 5, 300);
        Bus odd1 = list.get(1);
        Bus odd2 = list.get(3);
        Bus odd3 = list.get(5);

        strategy.sortByEvenKilometrage(list);

        assertSame(odd1, list.get(1));
        assertSame(odd2, list.get(3));
        assertSame(odd3, list.get(5));
    }

    @Test
    @DisplayName("sortByEvenKilometrage: ноль считается чётным значением")
    void testSortByEvenKilometrageWithZero() {
        List<Bus> list = createBuses(100, 5, 0, 3);

        strategy.sortByEvenKilometrage(list);

        assertEquals(List.of(0, 5, 100, 3), getKilometrages(list));
    }

    @Test
    @DisplayName("sortByEvenKilometrage: все значения нечётные, список не меняется")
    void testSortByEvenKilometrageAllOdd() {
        List<Bus> list = createBuses(9, 3, 7, 1);

        strategy.sortByEvenKilometrage(list);

        assertEquals(List.of(9, 3, 7, 1), getKilometrages(list));
    }

    @Test
    @DisplayName("sortByEvenKilometrage: все значения чётные, сортируется весь список")
    void testSortByEvenKilometrageAllEven() {
        List<Bus> list = createBuses(40, 20, 30, 10);

        strategy.sortByEvenKilometrage(list);

        assertEquals(List.of(10, 20, 30, 40), getKilometrages(list));
    }

    @Test
    @DisplayName("sortByEvenKilometrage: пустой список не вызывает ошибок")
    void testSortByEvenKilometrageEmptyList() {
        List<Bus> emptyList = new ArrayList<>();

        assertDoesNotThrow(() -> strategy.sortByEvenKilometrage(emptyList));
        assertTrue(emptyList.isEmpty());
    }

    @Test
    @DisplayName("sortByEvenKilometrage: null вместо списка вызывает NullPointerException")
    void testSortByEvenKilometrageNullList() {
        assertThrows(NullPointerException.class, () -> strategy.sortByEvenKilometrage(null));
    }

    @Test
    @DisplayName("sortByEvenKilometrage: null внутри списка вызывает NullPointerException")
    void testSortByEvenKilometrageNullElement() {
        buses.add(null);

        assertThrows(NullPointerException.class, () -> strategy.sortByEvenKilometrage(buses));
    }

    // ---------- Вспомогательные методы ----------

    protected static Bus createBus(String stateNumber, String modelName, int kilometrage) {
        return new Bus.BusBuilder()
                .setStateBusNumber(stateNumber)
                .setModelName(modelName)
                .setKilometrage(kilometrage)
                .build();
    }

    /**
     * Создаёт список автобусов с заданным пробегом и уникальными гос. номерами.
     */
    protected static List<Bus> createBuses(int... kilometrages) {
        List<Bus> result = new ArrayList<>();
        for (int i = 0; i < kilometrages.length; i++) {
            String stateNumber = String.format("А%03dАА77", i + 1);
            result.add(createBus(stateNumber, "Volvo", kilometrages[i]));
        }
        return result;
    }

    protected static List<Integer> getKilometrages(List<Bus> list) {
        return list.stream().map(Bus::getKilometrage).collect(Collectors.toList());
    }

    protected static List<String> getModelNames(List<Bus> list) {
        return list.stream().map(Bus::getModelName).collect(Collectors.toList());
    }

    protected static List<String> getStateNumbers(List<Bus> list) {
        return list.stream().map(Bus::getStateBusNumber).collect(Collectors.toList());
    }
}