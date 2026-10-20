package by.coldynee.collections;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultithreadedCounterTest {
    @Test
    @DisplayName("Подсчёт вхождений в список строк")
    void countStringOccurrences() {
        List<String> list = List.of("apple", "banana", "apple", "cherry", "apple", "banana");
        long count = MultithreadedCounter.countOccurrences(list, s -> s.equals("apple"));
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Подсчёт вхождений в список чисел")
    void countIntegerOccurrences() {
        List<Integer> list = List.of(1, 2, 3, 2, 4, 2, 5);
        long count = MultithreadedCounter.countOccurrences(list, n -> n == 2);
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Подсчёт в пустом списке возвращает 0")
    void countInEmptyList() {
        List<String> list = List.of();
        long count = MultithreadedCounter.countOccurrences(list, s -> true);
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Подсчёт в списке из одного элемента")
    void countInSingleElementList() {
        List<String> list = List.of("only");
        assertEquals(1, MultithreadedCounter.countOccurrences(list, s -> s.equals("only")));
        assertEquals(0, MultithreadedCounter.countOccurrences(list, s -> s.equals("other")));
    }

    @Test
    @DisplayName("Подсчёт когда нет совпадений")
    void countNoMatches() {
        List<String> list = List.of("a", "b", "c");
        long count = MultithreadedCounter.countOccurrences(list, s -> s.equals("z"));
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Подсчёт когда все элементы совпадают")
    void countAllMatch() {
        List<String> list = List.of("x", "x", "x", "x", "x");
        long count = MultithreadedCounter.countOccurrences(list, s -> s.equals("x"));
        assertEquals(5, count);
    }

    @Test
    @DisplayName("Подсчёт с сложным Predicate")
    void countWithComplexPredicate() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        long evenCount = MultithreadedCounter.countOccurrences(list, n -> n % 2 == 0);
        assertEquals(5, evenCount);
    }

    @Test
    @DisplayName("Подсчёт в большом списке (проверка работы чанков)")
    void countInLargeList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i % 100);
        }
        long count = MultithreadedCounter.countOccurrences(list, n -> n == 42);
        assertEquals(100, count);
    }

    @Test
    @DisplayName("Подсчёт с использованием CustomArrayList")
    void countWithCustomArrayList() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("a");
        list.add("b");
        list.add("a");
        list.add("c");
        list.add("a");
        long count = MultithreadedCounter.countOccurrences(list, s -> s.equals("a"));
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Подсчёт вхождений Bus по модели")
    void countBusByModel() {
        List<Bus> buses = List.of(
                new Bus.BusBuilder().setStateBusNumber("А111АА77").setModelName("Volvo").setKilometrage(100).build(),
                new Bus.BusBuilder().setStateBusNumber("А222АА77").setModelName("BMW").setKilometrage(200).build(),
                new Bus.BusBuilder().setStateBusNumber("А333АА77").setModelName("Volvo").setKilometrage(300).build(),
                new Bus.BusBuilder().setStateBusNumber("А444АА77").setModelName("Volvo").setKilometrage(400).build()
        );
        long count = MultithreadedCounter.countOccurrences(buses, bus -> bus.getModelName().equals("Volvo"));
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Подсчёт вхождений Bus по пробегу")
    void countBusByKilometrage() {
        List<Bus> buses = List.of(
                new Bus.BusBuilder().setStateBusNumber("А111АА77").setModelName("Volvo").setKilometrage(100).build(),
                new Bus.BusBuilder().setStateBusNumber("А222АА77").setModelName("BMW").setKilometrage(100).build(),
                new Bus.BusBuilder().setStateBusNumber("А333АА77").setModelName("Volvo").setKilometrage(200).build()
        );
        long count = MultithreadedCounter.countOccurrences(buses, bus -> bus.getKilometrage() == 100);
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Подсчёт с регистронезависимым сравнением")
    void countCaseInsensitive() {
        List<String> list = List.of("Apple", "APPLE", "apple", "aPpLe");
        long count = MultithreadedCounter.countOccurrences(list, s -> s.equalsIgnoreCase("apple"));
        assertEquals(4, count);
    }

    @Test
    @DisplayName("Подсчёт с Predicate, проверяющим длину строки")
    void countByStringLength() {
        List<String> list = List.of("a", "ab", "abc", "abcd", "abcde");
        long count = MultithreadedCounter.countOccurrences(list, s -> s.length() >= 3);
        assertEquals(3, count);
    }
}
