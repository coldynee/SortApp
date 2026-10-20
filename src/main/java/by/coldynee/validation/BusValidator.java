package by.coldynee.validation;

import by.coldynee.model.Bus;

public class BusValidator {

    public static void validateStateBusNumber(String stateBusNumber){
        if (stateBusNumber == null || stateBusNumber.isBlank()) {
            throw new IllegalArgumentException("Гос. номер не может быть пустым");
        }

        String trimmed = stateBusNumber.trim().toUpperCase();

        if (!Bus.RUSSIAN_CAR_NUMBER_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(
                    "Неверный формат гос. номера. Ожидается формат 'А111АА77' или 'А111АА777' " +
                            "\nИспользуются только буквы: А, В, Е, К, М, Н, О, Р, С, Т, У, Х." +
                            "\nи цифры для региона (от 01 до 997), цифры для номера могут быть любыми"
            );
        }

        String regionStr = trimmed.replaceFirst(".*?(\\d{2,3})$", "$1");
        try {
            int region = Integer.parseInt(regionStr);
            if (region < 1 || region > 997) {
                throw new IllegalArgumentException("Код региона в госномере должен быть числом от 1 до 997");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Код региона должен быть корректным числом");
        }

    }
    public static void validateModelName(String modelName){
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название модели не может быть пустым");
        }
        String trimmedModelName = modelName.trim();

        if (trimmedModelName.length() > Bus.MAX_MODEL_NAME_LENGTH){
            throw new IllegalArgumentException("Модель не может быть длиннее 50 символов");
        }

        if (!trimmedModelName.matches("^[a-zA-Zа-яА-ЯёЁ0-9][a-zA-Zа-яА-ЯёЁ0-9\\s\\-\\.]*$")) {
            throw new IllegalArgumentException("Модель содержит недопустимые символы или начинается с пробела/точки. Разрешены только буквы, цифры, пробелы, дефис и точка");
        }
    }

    public static void validateKilometrage(int kilometrage){
        if (kilometrage < 0) {
            throw new IllegalArgumentException("Пробег не может быть отрицательным");
        }
        if (kilometrage > Bus.MAX_KILOMETRAGE) {
            throw new IllegalArgumentException("Пробег не может превышать " + Bus.MAX_KILOMETRAGE + " км");
        }
    }
}
