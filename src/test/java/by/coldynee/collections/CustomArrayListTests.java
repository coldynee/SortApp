package by.coldynee.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomArrayListTests {
    private CustomArrayList<String> customList;

    CustomArrayListTests(){
        customList = new CustomArrayList<String>();
        customList.add("1");
        customList.add("2");
        customList.add("3");
    }


    @Test
    @DisplayName("Добавление элементов")
    void testArrayListCreation(){

        var iter = customList.iterator();
        assertEquals(3, customList.size());

        assertEquals("1",  iter.next());
        assertEquals("2",  iter.next());
        assertEquals("3",  iter.next());
    }

    @Test
    @DisplayName("Наличие элемента")
    void testArrayListContains(){

        assertEquals(true,  customList.contains("2"));
        assertEquals(false,  customList.contains("4"));
    }

    @Test
    @DisplayName("Удаление элементов")
    void testArrayListDelete(){
        customList.remove("2");

        var iter = customList.iterator();
        assertEquals(2, customList.size());

        assertEquals("1",  iter.next());
        assertEquals("3",  iter.next());
    }

    @Test
    @DisplayName("Многопоточная проверка на вхождения")
    void testArrayListOccurrences(){

        customList.addAll( List.of(new String[]{"4", "5", "6", "7", "8", "4", "5", "9", "10"}));
        var count = customList.countOccurrencesStream("5");
        assertEquals(2,  count);
    }

    @Test
    @DisplayName("Очистка списка")
    void testArrayListClear(){
        customList.clear();

        assertEquals(0, customList.size());
        var customlistarray = customList.toArray();
        assertEquals(0,  customlistarray.length);
    }
}