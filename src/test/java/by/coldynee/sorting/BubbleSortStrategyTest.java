package by.coldynee.sorting;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BubbleSortStrategyTest {

    private SortingStrategy strategy;
    private List<Bus> buses;

    @BeforeEach
    void setUp() {
        strategy = new BubbleSortStrategy();

        // Заведомо несортированный список валидных автобусов
        buses = new ArrayList<>();
        buses.add(new Bus.BusBuilder().setStateBusNumber("А333АА77").setModelName("Volvo").setKilometrage(300000).build());
        buses.add(new Bus.BusBuilder().setStateBusNumber("А111АА77").setModelName("Mercedes").setKilometrage(100000).build());
        buses.add(new Bus.BusBuilder().setStateBusNumber("А222АА77").setModelName("BMW").setKilometrage(200000).build());
    }

    @Test
    @DisplayName("Сортировка по пробегу (возрастание)")
    void testSortByKilometrage() {
        strategy.sort(buses, BusComparators.BY_KILOMETRAGE);

        assertEquals(100000, buses.get(0).getKilometrage());
        assertEquals(200000, buses.get(1).getKilometrage());
        assertEquals(300000, buses.get(2).getKilometrage());
    }

    @Test
    @DisplayName("Сортировка по названию модели (без учета регистра)")
    void testSortByModelName() {
        strategy.sort(buses, BusComparators.BY_MODEL_NAME);

        assertEquals("BMW", buses.get(0).getModelName());
        assertEquals("Mercedes", buses.get(1).getModelName());
        assertEquals("Volvo", buses.get(2).getModelName());
    }

    @Test
    @DisplayName("Сортировка пустого списка не должна вызывать ошибок")
    void testSortEmptyList() {
        List<Bus> emptyList = new ArrayList<>();

        assertDoesNotThrow(() -> strategy.sort(emptyList, BusComparators.BY_KILOMETRAGE));
        assertTrue(emptyList.isEmpty());
    }

    @Test
    @DisplayName("Сортировка происходит на месте (in-place), ссылка на список не меняется")
    void testSortInPlace() {
        List<Bus> originalReference = buses;

        strategy.sort(buses, BusComparators.BY_KILOMETRAGE);

        assertSame(originalReference, buses);
    }

    @Test
    @DisplayName("getName() возвращает корректное название алгоритма")
    void testGetName() {
        assertEquals("Сортировка пузырьком", strategy.getName());
    }
}