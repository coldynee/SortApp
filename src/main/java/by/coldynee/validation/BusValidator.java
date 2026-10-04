package by.coldynee.validation;

public class BusValidator {

    //Максимальный логичный номер маршрута
    private static final int MAX_ROUTE_NUMBER = 9999;
    //Максимальный реалистичный пробег
    private static final int MAX_KILOMETRAGE = 2_000_000;

    public static void validateRouteNumber(int routeNumber){
        if (routeNumber <= 0 ) {
            throw new IllegalArgumentException("Номер маршрута не может быть меньше или равен нулю");
        }
        if (routeNumber > MAX_ROUTE_NUMBER) {
            throw new IllegalArgumentException("Номер маршрута не может превышать " + MAX_ROUTE_NUMBER);
        }
    }
    public static void validateModelName(String modelName){
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название модели не может быть пустым");
        }
        String trimmedModelName = modelName.trim();

        if (trimmedModelName.length() > 50){
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
        if (kilometrage > MAX_KILOMETRAGE) {
            throw new IllegalArgumentException("Пробег не может превышать " + MAX_KILOMETRAGE + " км");
        }
    }
}
