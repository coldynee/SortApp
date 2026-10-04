package by.coldynee.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusTest {

    @Test
    @DisplayName("Создание валидного автобуса через Builder")
    void testValidBusCreation(){
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(114)
                .setModelName("Volvo")
                .setKilometrage(99999);

        Bus bus = builder.build();

        assertEquals(114, bus.getRouteNumber());
        assertEquals("Volvo", bus.getModelName());
        assertEquals(99999, bus.getKilometrage());
    }

    @Test
    @DisplayName("Отрицательный номер маршрута")
    void testInvalidRouteNumber() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(-5)
                .setModelName("Volvo")
                .setKilometrage(99999);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Нулевой номер маршрута")
    void testZeroRouteNumber() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(0)
                .setModelName("Volvo")
                .setKilometrage(99999);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Слишком большой номер маршрута")
    void testTooLargeRouteNumber() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(10000)
                .setModelName("Volvo")
                .setKilometrage(99999);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Пустое имя модели должно")
    void testEmptyModelName() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(114)
                .setModelName("")
                .setKilometrage(99999);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Null имя модели")
    void testNullModelName() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(114)
                .setModelName(null)
                .setKilometrage(99999);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Имя модели с недопустимыми символами")
    void testModelWithComma() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(1)
                .setModelName("Vol,vo")
                .setKilometrage(100);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Имя модели с допустимым символом")
    void testModelWithHyphen() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(113)
                .setModelName("Mercedes-Benz")
                .setKilometrage(100000);

        Bus bus = builder.build();
        assertEquals("Mercedes-Benz", bus.getModelName());
    }

    @Test
    @DisplayName("Отрицательный пробег")
    void testNegativeKilometrage() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(114)
                .setModelName("Volvo")
                .setKilometrage(-20);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Слишком большой пробег")
    void testTooLargeKilometrage() {
        Bus.BusBuilder builder = new Bus.BusBuilder()
                .setRouteNumber(114)
                .setModelName("Volvo")
                .setKilometrage(3_000_000);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    @DisplayName("Сравнение одинаковых автобусов")
    void testEquals() {
        Bus bus1 = new Bus.BusBuilder()
                .setRouteNumber(114)
                .setModelName("Volvo")
                .setKilometrage(99999)
                .build();

        Bus bus2 = new Bus.BusBuilder()
                .setRouteNumber(114)
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
                .setRouteNumber(114)
                .setModelName("Volvo")
                .setKilometrage(99999)
                .build();

        Bus bus2 = new Bus.BusBuilder()
                .setRouteNumber(114)
                .setModelName("Volvo")
                .setKilometrage(100000)
                .build();

        assertNotEquals(bus1, bus2);
    }

}
