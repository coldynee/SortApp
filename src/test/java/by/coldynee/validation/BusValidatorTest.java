package by.coldynee.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BusValidatorTest {

    @Test
    @DisplayName("validateStateBusNumber: отклоняет null и пустые строки")
    void validateStateBusNumber_NullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber(null));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber(""));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber("   "));
    }

    @Test
    @DisplayName("validateStateBusNumber: отклоняет запрещенные буквы и неверную длину")
    void validateStateBusNumber_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber("Б123ВЕ77"));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber("А123ВЭ77")); // Э нет в списке

        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber("А12ВЕ77"));  // 2 цифры вместо 3
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber("А123ВЕ7"));   // 1 цифра региона
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateStateBusNumber("А123ВЕ7777")); // 4 цифры региона
    }

    @Test
    @DisplayName("validateStateBusNumber: принимает валидные номера")
    void validateStateBusNumber_Valid() {
        assertDoesNotThrow(() -> BusValidator.validateStateBusNumber("А123ВЕ77"));
        assertDoesNotThrow(() -> BusValidator.validateStateBusNumber("К456МН177"));
        assertDoesNotThrow(() -> BusValidator.validateStateBusNumber("О001ОО777"));
    }

    @Test
    @DisplayName("validateModelName: отклоняет null, пустые и слишком длинные строки")
    void validateModelName_NullBlankOrTooLong() {
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateModelName(null));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateModelName(""));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateModelName("   "));

        String longName = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateModelName(longName));
    }

    @Test
    @DisplayName("validateModelName: отклоняет недопустимые символы")
    void validateModelName_InvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateModelName("Vol,vo"));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateModelName("Volvo@Car"));
    }

    @Test
    @DisplayName("validateModelName: принимает валидные имена (с дефисом, пробелом, цифрами)")
    void validateModelName_Valid() {
        assertDoesNotThrow(() -> BusValidator.validateModelName("Mercedes-Benz"));
        assertDoesNotThrow(() -> BusValidator.validateModelName("Lada Vesta"));
        assertDoesNotThrow(() -> BusValidator.validateModelName("Kamaz 6520"));
    }

    @Test
    @DisplayName("validateKilometrage: отклоняет отрицательный и превышающий максимум пробег")
    void validateKilometrage_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateKilometrage(-1));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateKilometrage(-1000));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateKilometrage(2_000_001));
        assertThrows(IllegalArgumentException.class, () -> BusValidator.validateKilometrage(9_999_999));
    }

    @Test
    @DisplayName("validateKilometrage: принимает валидный пробег (включая граничные значения)")
    void validateKilometrage_Valid() {
        assertDoesNotThrow(() -> BusValidator.validateKilometrage(0));
        assertDoesNotThrow(() -> BusValidator.validateKilometrage(150_000));
        assertDoesNotThrow(() -> BusValidator.validateKilometrage(2_000_000));
    }
}