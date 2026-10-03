package by.coldynee.validation;

public class BusValidator {
    public static void validateRouteNumber(int routeNumber){
        if (routeNumber <= 0 ) {
            throw new IllegalArgumentException("Номер маршрута не может быть меньше нуля");
        }
    }
    public static void validateModelName(String modelName){
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название модели не может быть пустым");
        }
    }

    public static void validateKilometrage(int kilometrage){
        if (kilometrage < 0 ) {
            throw new IllegalArgumentException("Пробег не может быть отрицательным");
        }
    }
}
