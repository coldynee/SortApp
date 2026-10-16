package by.coldynee.io;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomInputTest {

    private RandomInput randomInput;

    @BeforeEach
    void setUp() {
        randomInput = new RandomInput();
    }

    @Test
    @DisplayName("Проверка количества генерируемых автобусов")
    void testLoadCreatesCorrectNumberOfBuses() {
        int count = 5;
        List<Bus> buses = randomInput.load(count);

        assertNotNull(buses, "Метод не должен возвращать null");
        assertEquals(count, buses.size(), "Размер списка должен совпадать с запрошенным количеством");
    }

    @Test
    @DisplayName("Проверка генерации при нулевом или отрицательном запросе")
    void testLoadWithZeroOrNegativeCount() {
        assertTrue(randomInput.load(0).isEmpty(), "При запросе 0 автобусов список должен быть пустым");
        assertTrue(randomInput.load(-5).isEmpty(), "При отрицательном запросе список должен быть пустым");
    }

    @Test
    @DisplayName("Проверка корректности данных сгенерированных автобусов")
    void testLoadGeneratesValidBusData() {
        List<Bus> buses = randomInput.load(10); // Генерируем 10 автобусов для проверки

        assertFalse(buses.isEmpty(), "Список не должен быть пустым");

        for (Bus bus : buses) {
            // Используем assertAll, чтобы проверить все поля сразу.
            // Если упадет одна проверка, остальные тоже выполнятся и покажут ошибки.
            assertAll("Проверка полей автобуса",
                    () -> assertNotNull(bus, "Сгенерированный автобус не должен быть null"),

                    () -> assertNotNull(bus.getStateBusNumber(), "Госномер не должен быть null"),
                    () -> assertTrue(Bus.RUSSIAN_CAR_NUMBER_PATTERN.matcher(bus.getStateBusNumber()).matches(),
                            "Госномер " + bus.getStateBusNumber() + " не соответствует формату"),

                    () -> assertNotNull(bus.getModelName(), "Модель автобуса не должна быть null"),
                    () -> assertFalse(bus.getModelName().trim().isEmpty(), "Имя модели не должно быть пустым"),

                    () -> assertTrue(bus.getKilometrage() >= 1,
                            "Пробег должен быть >= 1, а по факту: " + bus.getKilometrage()),
                    () -> assertTrue(bus.getKilometrage() <= Bus.MAX_KILOMETRAGE,
                            "Пробег не должен превышать MAX_KILOMETRAGE")
            );
        }
    }
}