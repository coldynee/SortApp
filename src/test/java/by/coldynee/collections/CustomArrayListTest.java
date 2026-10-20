package by.coldynee.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {

    private CustomArrayList<String> list;

    @BeforeEach
    void setUp() {
        list = new CustomArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
    }

    @Test
    @DisplayName("Конструктор создаёт пустой список")
    void constructorCreatesEmptyList() {
        CustomArrayList<String> empty = new CustomArrayList<>();
        assertEquals(0, empty.size());
        assertTrue(empty.isEmpty());
    }
    @Test
    @DisplayName("size() и isEmpty() работают корректно")
    void sizeAndIsEmpty() {
        assertEquals(3, list.size());
        assertFalse(list.isEmpty());

        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("add() добавляет элемент в конец")
    void addAppendsElement() {
        list.add("D");
        assertEquals(4, list.size());
        assertEquals("D", list.get(3));
    }

    @Test
    @DisplayName("get() возвращает элемент по индексу")
    void getReturnsElement() {
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    @DisplayName("get() бросает IndexOutOfBoundsException для невалидных индексов")
    void getThrowsOnInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(100));
    }

    @Test
    @DisplayName("set() заменяет элемент и возвращает старый")
    void setReplacesAndReturnsOld() {
        String old = list.set(1, "X");
        assertEquals("B", old);
        assertEquals("X", list.get(1));
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("set() бросает IndexOutOfBoundsException для невалидных индексов")
    void setThrowsOnInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, "X"));
    }

    @Test
    @DisplayName("add(index, element) вставляет в середину")
    void addInMiddle() {
        list.add(1, "X");
        assertEquals(4, list.size());
        assertEquals("A", list.get(0));
        assertEquals("X", list.get(1));
        assertEquals("B", list.get(2));
        assertEquals("C", list.get(3));
    }

    @Test
    @DisplayName("add(0, element) вставляет в начало")
    void addAtBeginning() {
        list.add(0, "X");
        assertEquals("X", list.get(0));
        assertEquals("A", list.get(1));
    }

    @Test
    @DisplayName("add(size, element) вставляет в конец")
    void addAtEnd() {
        list.add(3, "X");
        assertEquals("X", list.get(3));
    }

    @Test
    @DisplayName("add(index) бросает IndexOutOfBoundsException для невалидных индексов")
    void addThrowsOnInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(4, "X"));
    }

    @Test
    @DisplayName("remove(Object) удаляет первое вхождение")
    void removeObject() {
        list.add("B");
        assertTrue(list.remove("B"));
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
        assertEquals("B", list.get(2));
    }
    @Test
    @DisplayName("remove(Object) возвращает false, если элемента нет")
    void removeObjectNotFound() {
        assertFalse(list.remove("Z"));
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("remove(Object) игнорирует null")
    void removeObjectNull() {
        assertFalse(list.remove(null));
    }

    @Test
    @DisplayName("remove(int) удаляет по индексу и возвращает старый элемент")
    void removeByIndex() {
        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    @DisplayName("remove(int) бросает IndexOutOfBoundsException для невалидных индексов")
    void removeByIndexThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(3));
    }

    @Test
    @DisplayName("contains() находит существующий элемент")
    void containsFindsExisting() {
        assertTrue(list.contains("B"));
    }

    @Test
    @DisplayName("contains() возвращает false для отсутствующего")
    void containsReturnsFalseForMissing() {
        assertFalse(list.contains("Z"));
    }

    @Test
    @DisplayName("contains() возвращает false для null")
    void containsReturnsFalseForNull() {
        assertFalse(list.contains(null));
    }

    @Test
    @DisplayName("indexOf() возвращает индекс первого вхождения")
    void indexOfReturnsFirst() {
        list.add("B");
        assertEquals(1, list.indexOf("B"));
    }

    @Test
    @DisplayName("indexOf() возвращает -1 для отсутствующего")
    void indexOfReturnsMinusOne() {
        assertEquals(-1, list.indexOf("Z"));
    }

    @Test
    @DisplayName("indexOf() возвращает -1 для null")
    void indexOfReturnsMinusOneForNull() {
        assertEquals(-1, list.indexOf(null));
    }

    @Test
    @DisplayName("lastIndexOf() возвращает индекс последнего вхождения")
    void lastIndexOfReturnsLast() {
        list.add("B");
        assertEquals(3, list.lastIndexOf("B"));
    }

    @Test
    @DisplayName("lastIndexOf() возвращает -1 для отсутствующего")
    void lastIndexOfReturnsMinusOne() {
        assertEquals(-1, list.lastIndexOf("Z"));
    }

    @Test
    @DisplayName("containsAll() проверяет наличие всех элементов")
    void containsAll() {
        assertTrue(list.containsAll(List.of("A", "C")));
        assertFalse(list.containsAll(List.of("A", "Z")));
    }

    @Test
    @DisplayName("addAll() добавляет все элементы из коллекции")
    void addAll() {
        assertTrue(list.addAll(List.of("D", "E")));
        assertEquals(5, list.size());
        assertEquals("D", list.get(3));
        assertEquals("E", list.get(4));
    }

    @Test
    @DisplayName("addAll(int, Collection) вставляет по индексу")
    void addAllByIndex() {
        list.addAll(1, List.of("X", "Y"));
        assertEquals(5, list.size());
        assertEquals("A", list.get(0));
        assertEquals("X", list.get(1));
        assertEquals("Y", list.get(2));
        assertEquals("B", list.get(3));
    }

    @Test
    @DisplayName("removeAll() удаляет все элементы из коллекции")
    void removeAll() {
        assertTrue(list.removeAll(List.of("A", "C")));
        assertEquals(1, list.size());
        assertEquals("B", list.get(0));
    }

    @Test
    @DisplayName("retainAll() оставляет только элементы из коллекции")
    void retainAll() {
        assertTrue(list.retainAll(List.of("A", "C")));
        assertEquals(2, list.size());
        assertTrue(list.contains("A"));
        assertTrue(list.contains("C"));
    }

    @Test
    @DisplayName("clear() очищает список и зануляет ссылки")
    void clear() {
        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("toArray() возвращает массив нужного размера")
    void toArrayNoArgs() {
        Object[] arr = list.toArray();
        assertEquals(3, arr.length);
        assertEquals("A", arr[0]);
        assertEquals("B", arr[1]);
        assertEquals("C", arr[2]);
    }

    @Test
    @DisplayName("toArray(E[]) заполняет переданный массив")
    void toArrayWithArray() {
        String[] arr = new String[3];
        String[] result = list.toArray(arr);
        assertSame(arr, result);
        assertEquals("A", arr[0]);
    }

    @Test
    @DisplayName("toArray(E[]) создаёт новый массив, если переданный мал")
    void toArrayCreatesNewIfSmall() {
        String[] arr = new String[1];
        String[] result = list.toArray(arr);
        assertNotSame(arr, result);
        assertEquals(3, result.length);
    }

    @Test
    @DisplayName("toArray(E[]) зануляет элемент после последнего, если массив больше")
    void toArrayNullsTrailing() {
        String[] arr = new String[5];
        arr[3] = "OLD";
        list.toArray(arr);
        assertNull(arr[3]);
    }

    @Test
    @DisplayName("iterator() корректно обходит список")
    void iteratorTraverses() {
        Iterator<String> it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    @DisplayName("iterator.next() бросает NoSuchElementException в конце")
    void iteratorNextThrowsAtEnd() {
        Iterator<String> it = list.iterator();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @DisplayName("iterator.remove() удаляет последний элемент")
    void iteratorRemove() {
        Iterator<String> it = list.iterator();
        it.next();
        it.next();
        it.remove();
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    @DisplayName("iterator.remove() бросает IllegalStateException без next()")
    void iteratorRemoveThrowsWithoutNext() {
        Iterator<String> it = list.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    @DisplayName("iterator.remove() бросает IllegalStateException при повторном вызове")
    void iteratorRemoveThrowsOnDoubleCall() {
        Iterator<String> it = list.iterator();
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    @DisplayName("listIterator() обходит список вперёд и назад")
    void listIteratorBidirectional() {
        ListIterator<String> it = list.listIterator();
        assertTrue(it.hasNext());
        assertFalse(it.hasPrevious());

        assertEquals("A", it.next());
        assertTrue(it.hasPrevious());
        assertEquals("B", it.next());

        assertEquals("B", it.previous());
        assertEquals("A", it.previous());
        assertFalse(it.hasPrevious());
    }

    @Test
    @DisplayName("listIterator(int) стартует с указанного индекса")
    void listIteratorFromIndex() {
        ListIterator<String> it = list.listIterator(1);
        assertEquals(1, it.nextIndex());
        assertEquals(0, it.previousIndex());
        assertEquals("B", it.next());
    }

    @Test
    @DisplayName("listIterator.set() заменяет последний элемент")
    void listIteratorSet() {
        ListIterator<String> it = list.listIterator();
        it.next();
        it.set("X");
        assertEquals("X", list.get(0));
    }

    @Test
    @DisplayName("listIterator.add() вставляет элемент перед курсором")
    void listIteratorAdd() {
        ListIterator<String> it = list.listIterator(1);
        it.add("X");
        assertEquals(4, list.size());
        assertEquals("X", list.get(1));
    }

    @Test
    @DisplayName("subList() возвращает копию части списка")
    void subListReturnsCopy() {
        List<String> sub = list.subList(1, 3);
        assertEquals(2, sub.size());
        assertEquals("B", sub.get(0));
        assertEquals("C", sub.get(1));
    }

    @Test
    @DisplayName("subList() бросает IndexOutOfBoundsException для невалидных границ")
    void subListThrowsOnInvalidBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.subList(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.subList(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> list.subList(2, 1));
    }

    @Test
    @DisplayName("subList() с одинаковыми индексами возвращает пустой список")
    void subListEmpty() {
        List<String> sub = list.subList(1, 1);
        assertTrue(sub.isEmpty());
    }

    @Test
    @DisplayName("Массив растёт при превышении начальной вместимости")
    void arrayGrowsOnOverflow() {
        CustomArrayList<Integer> nums = new CustomArrayList<>();
        for (int i = 0; i < 100; i++) {
            nums.add(i);
        }
        assertEquals(100, nums.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, nums.get(i));
        }
    }

    @Test
    @DisplayName("Работает с Integer")
    void workWithIntegers() {
        CustomArrayList<Integer> nums = new CustomArrayList<>();
        nums.add(1);
        nums.add(2);
        assertTrue(nums.contains(1));
        assertEquals(2, nums.size());
    }

    @Test
    @DisplayName("Работает с null-элементами (add)")
    void workWithNullElements() {
        CustomArrayList<String> nullable = new CustomArrayList<>();
        nullable.add(null);
        assertEquals(1, nullable.size());
        assertNull(nullable.get(0));
    }
}