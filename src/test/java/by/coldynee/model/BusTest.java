package by.coldynee.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusTest {

    @Test
    @DisplayName("Создание валидного автобуса через Builder")
    void testValidBusCreation(){
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(99999);

        Bus bus = builder.build();

        assertEquals("А123ВЕ77", bus.getStateBusNumber());
        assertEquals("Volvo", bus.getModelName());
        assertEquals(99999, bus.getKilometrage());
    }

    @Test
    @DisplayName("Валидный госномер с пробелами и дефисами")
    void testValidStateNumberWithSpacesAndDashes() {
        Bus bus1 = new Bus.BusBuilder().setStateBusNumber("А 123 ВЕ 777").setModelName("Mercedes").setKilometrage(100).build();
        Bus bus2 = new Bus.BusBuilder().setStateBusNumber("К-456-МН-99").setModelName("Mercedes").setKilometrage(100).build();

        assertEquals("А123ВЕ777", bus1.getStateBusNumber());
        assertEquals("К456МН99", bus2.getStateBusNumber());
    }

    @Test
    @DisplayName("Невалидный госномер: null или пустая строка")
    void testNullOrBlankStateNumber() {
        Bus.BusBuilder builder1 = new Bus.BusBuilder().setStateBusNumber(null).setModelName("Volvo").setKilometrage(100);
        Bus.BusBuilder builder2 = new Bus.BusBuilder().setStateBusNumber("   ").setModelName("Volvo").setKilometrage(100);

        assertThrows(IllegalArgumentException.class, builder1::build);
        assertThrows(IllegalArgumentException.class, builder2::build);
    }

    @Test
    @DisplayName("Невалидный госномер: запрещённые буквы (Б, Г, Д)")
    void testInvalidLettersInStateNumber() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("Б123ГД77")
                .setModelName("Volvo")
                .setKilometrage(100);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Невалидный госномер: неправильная длина (мало цифр или региона)")
    void testInvalidLengthStateNumber() {
        Bus.BusBuilder builder1 = new Bus.BusBuilder().setStateBusNumber("А12БВ77").setModelName("Volvo").setKilometrage(100); // 2 цифры вместо 3
        Bus.BusBuilder builder2 = new Bus.BusBuilder().setStateBusNumber("А123БВ7").setModelName("Volvo").setKilometrage(100);  // 1 цифра региона

        assertThrows(IllegalArgumentException.class, builder1::build);
        assertThrows(IllegalArgumentException.class, builder2::build);
    }

    @Test
    @DisplayName("Пустое имя модели")
    void testEmptyModelName() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("")
                .setKilometrage(99999);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Null имя модели")
    void testNullModelName() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName(null)
                .setKilometrage(99999);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Слишком длинное имя модели (51 символ)")
    void testToLongModelName() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("VOLVO-VOLVO-VOLVO-VOLVO-VOLVO-VOLVO-VOLVO-VOLVO-V51")
                .setKilometrage(100);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Имя модели с недопустимыми символами")
    void testModelWithComma() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Vol,vo")
                .setKilometrage(100);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Имя модели с допустимым символом (дефис)")
    void testModelWithHyphen() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Mercedes-Benz")
                .setKilometrage(100000);

        Bus bus = builder.build();
        assertEquals("Mercedes-Benz", bus.getModelName());
    }

    @Test
    @DisplayName("Отрицательный пробег")
    void testNegativeKilometrage() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(-20);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Слишком большой пробег")
    void testTooLargeKilometrage() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(3_000_000);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Сравнение одинаковых автобусов")
    void testEquals() {
        Bus bus1 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(99999)
                .build();

        Bus bus2 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(99999)
                .build();

        assertEquals(bus1, bus2);
        assertEquals(bus1.hashCode(), bus2.hashCode());
    }

    @Test
    @DisplayName("Сравнение разных автобусов")
    void testNotEquals() {
        Bus bus1 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(100000)
                .build();

        Bus bus2 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВМ77")
                .setModelName("Volvo")
                .setKilometrage(100000)
                .build();

        assertNotEquals(bus1, bus2);
    }

    @Test
    @DisplayName("Корректная работа метода toString")
    void testToString() {
        Bus bus = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(99999)
                .build();

        String expected = "Автобус Volvo | Гос. Номер А123ВЕ77 | пробег 99999 км.";
        assertEquals(expected, bus.toString());
    }

    @Test
    @DisplayName("equals возвращает false для null и объектов других классов")
    void testEqualsEdgeCases() {
        Bus bus = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(99999)
                .build();

        assertFalse(bus.equals(null));

        assertFalse(bus.equals("Это строка, а не автобус"));
        assertFalse(bus.equals(new Object()));
    }

    @Test
    @DisplayName("equals возвращает false, если отличается только modelName")
    void testEqualsDifferentModelName() {
        Bus bus1 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(100000)
                .build();

        Bus bus2 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Mercedes")
                .setKilometrage(100000)
                .build();

        assertFalse(bus1.equals(bus2));
    }

    @Test
    @DisplayName("equals возвращает false, если отличается только kilometrage")
    void testEqualsDifferentKilometrage() {
        Bus bus1 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(100000)
                .build();

        Bus bus2 = new Bus.BusBuilder()
                .setStateBusNumber("А123ВЕ77")
                .setModelName("Volvo")
                .setKilometrage(200000)
                .build();

        assertFalse(bus1.equals(bus2));
    }
}
