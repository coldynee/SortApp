package by.coldynee.sorting;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusComparatorsTest {

    @Test
    @DisplayName("BY_KILOMETRAGE: сравнивает по пробегу")
    void testByKilometrage() {
        Bus low = createBus("А111АА77", "Volvo", 100);
        Bus high = createBus("А222АА77", "Volvo", 200);
        Bus same = createBus("А333АА77", "Volvo", 100);

        assertTrue(BusComparators.BY_KILOMETRAGE.compare(low, high) < 0);
        assertTrue(BusComparators.BY_KILOMETRAGE.compare(high, low) > 0);
        assertEquals(0, BusComparators.BY_KILOMETRAGE.compare(low, same));
    }

    @Test
    @DisplayName("BY_MODEL_NAME: сравнивает по модели без учёта регистра")
    void testByModelName() {
        Bus audi = createBus("А111АА77", "audi", 100);
        Bus bmw = createBus("А222АА77", "BMW", 100);
        Bus audiUpperCase = createBus("А333АА77", "AUDI", 100);

        // При обычном сравнении строк "audi" оказалась бы после "BMW"
        assertTrue(BusComparators.BY_MODEL_NAME.compare(audi, bmw) < 0);
        assertTrue(BusComparators.BY_MODEL_NAME.compare(bmw, audi) > 0);
        assertEquals(0, BusComparators.BY_MODEL_NAME.compare(audi, audiUpperCase));
    }

    @Test
    @DisplayName("BY_STATE_BUS_NUMBER: сравнивает по гос. номеру")
    void testByStateBusNumber() {
        Bus first = createBus("А111АА77", "Volvo", 100);
        Bus second = createBus("А222АА77", "Volvo", 100);
        Bus firstCopy = createBus("А111АА77", "Mercedes", 200);

        assertTrue(BusComparators.BY_STATE_BUS_NUMBER.compare(first, second) < 0);
        assertTrue(BusComparators.BY_STATE_BUS_NUMBER.compare(second, first) > 0);
        assertEquals(0, BusComparators.BY_STATE_BUS_NUMBER.compare(first, firstCopy));
    }

    private static Bus createBus(String stateNumber, String modelName, int kilometrage) {
        return new Bus.BusBuilder()
                .setStateBusNumber(stateNumber)
                .setModelName(modelName)
                .setKilometrage(kilometrage)
                .build();
    }
}