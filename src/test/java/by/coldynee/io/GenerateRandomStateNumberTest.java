package by.coldynee.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GenerateRandomStateNumberTest {

    private GenerateRandomStateNumber generator;

    // Регулярное выражение для проверки госномера:
    // 1 буква, 3 цифры (от 001 до 999), 2 буквы, 2 или 3 цифры региона
    private static final String NUMBER_REGEX = "^[АВЕКМНОРСТУХ](?!000)\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}$";

    @BeforeEach
    void setUp() {
        generator = new GenerateRandomStateNumber();
    }

    @Test
    @DisplayName("Базовая проверка формата генерируемого номера")
    void testRandomStateNumberFormat() {
        String result = generator.randomStateNumber();

        assertNotNull(result, "Сгенерированный номер не должен быть null");
        assertTrue(result.matches(NUMBER_REGEX),
                "Номер '" + result + "' не соответствует формату А123БВ777");
    }

    @RepeatedTest(value = 1000, name = "Генерация случайного номера (попытка {currentRepetition} из {totalRepetitions})")
    @DisplayName("Многократная проверка для покрытия различных случайных значений")
    void testRandomStateNumberMultipleTimes() {
        String result = generator.randomStateNumber();
        assertTrue(result.matches(NUMBER_REGEX));
    }

    @Test
    @DisplayName("Проверка точного форматирования с помощью подмены Random (Reflection)")
    void testRandomStateNumberDeterministic() throws Exception {
        GenerateRandomStateNumber deterministicGenerator = new GenerateRandomStateNumber();

        // Создаем кастомный Random, который возвращает предсказуемые значения
        Random mockRandom = new Random() {
            private int callCount = 0;

            @Override
            public int nextInt(int bound) {
                int result;
                if (bound == 12) { // Запрос буквы (LETTERS.length)
                    if (callCount == 0) result = 0;      // 1-я буква: индекс 0 ('А')
                    else if (callCount == 2) result = 1; // 2-я буква: индекс 1 ('В')
                    else result = 2;                     // 3-я буква: индекс 2 ('Е')
                } else if (bound == 999) { // Запрос цифр номера
                    result = 0; // Вернет 0, в коде прибавится 1 -> 001
                } else { // Запрос региона (REGIONS.length)
                    result = 0; // Индекс 0 ("01")
                }
                callCount++;
                return result;
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