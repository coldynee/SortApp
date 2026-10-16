package by.coldynee.io;

import by.coldynee.model.Bus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GenerateRandomStateNumberTest {

    private GenerateRandomStateNumber generator;

    @BeforeEach
    void setUp() {
        generator = new GenerateRandomStateNumber();
    }

    @Test
    @DisplayName("Базовая проверка формата генерируемого номера")
    void testRandomStateNumberFormat() {
        String result = generator.randomStateNumber();

        assertNotNull(result, "Сгенерированный номер не должен быть null");
        assertTrue(Bus.RUSSIAN_CAR_NUMBER_PATTERN.matcher(result).matches(),
                "Номер '" + result + "' не соответствует формату А123БВ777");
    }

    @RepeatedTest(value = 1000, name = "Генерация случайного номера (попытка {currentRepetition} из {totalRepetitions})")
    @DisplayName("Многократная проверка для покрытия различных случайных значений")
    void testRandomStateNumberMultipleTimes() {
        String result = generator.randomStateNumber();
        assertTrue(Bus.RUSSIAN_CAR_NUMBER_PATTERN.matcher(result).matches());
    }

    @Test
    @DisplayName("Проверка точного форматирования с помощью подмены Random (Reflection)")
    void testRandomStateNumberDeterministic() throws Exception {
        GenerateRandomStateNumber deterministicGenerator = new GenerateRandomStateNumber();

        Random mockRandom = new Random() {
            private int callCount = 0;

            @Override
            public int nextInt(int bound) {
                return switch (bound) {
                    case 12 -> switch (callCount++) { // LETTERS.length
                        case 0 -> 0; // 1-я буква: 'А'
                        case 1 -> 1; // 2-я буква: 'В'
                        case 2 -> 2; // 3-я буква: 'Е'
                        default -> 0;
                    };
                    case 999 -> 0; // Цифры номера: 0 + 1 = 1 -> "001"
                    default -> 0;   // Регион: индекс 0 -> "01" (REGIONS.length)
                };
            }
        };

        // Используем рефлексию для подмены приватного поля random
        Field randomField = GenerateRandomStateNumber.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(deterministicGenerator, mockRandom);

        // Вызываем метод. Ожидаем точный результат на основе нашего mockRandom:
        // 'А' + "001" + 'В' + 'Е' + "01"
        String result = deterministicGenerator.randomStateNumber();

        assertEquals("А001ВЕ01", result, "Форматирование номера работает некорректно");
    }
}