package by.coldynee.validation;

import by.coldynee.model.Bus;

public class BusValidator {

    public static void validateRouteNumber(int routeNumber){
        if (routeNumber <= 0 ) {
            throw new IllegalArgumentException("Номер маршрута не может быть меньше или равен нулю");
        }
        if (routeNumber > Bus.MAX_ROUTE_NUMBER) {
            throw new IllegalArgumentException("Номер маршрута не может превышать " + Bus.MAX_ROUTE_NUMBER);
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

        if (!trimmedModelName.matches("^[a-zA-Zа-яА-ЯёЁ0-9\\s\\-\\.]+$")) {
            throw new IllegalArgumentException("Модель содержит недопустимые символы, разрешены только буквы, цифры, пробелы, дефис и точка");
        }
    }

    public static void validateKilometrage(int kilometrage){
        if (kilometrage < 0 ) {
            throw new IllegalArgumentException("Пробег не может быть отрицательным");
        }
        if (kilometrage > Bus.MAX_KILOMETRAGE) {
            throw new IllegalArgumentException("Пробег не может превышать " + Bus.MAX_KILOMETRAGE + " км");
        }
    }
}
