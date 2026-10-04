package by.coldynee.model;

import by.coldynee.validation.BusValidator;

import java.util.Objects;
/**
 * Неизменяемая модель автобуса.
 * <p>
 * Создаётся только через {@link BusBuilder}, который гарантирует валидацию полей.
 * </p>
 */
public class Bus {
    private final int routeNumber;
    private final String modelName;
    private final int kilometrage;

    private Bus(BusBuilder builder) {
        this.routeNumber = builder.routeNumber;
        this.modelName = builder.modelName;
        this.kilometrage = builder.kilometrage;
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    /**
     * Строитель для безопасного создания {@link Bus} с валидацией.
     */
    public static class BusBuilder{
        private int routeNumber;
        private String modelName;
        private int kilometrage;

        public BusBuilder setRouteNumber(int routeNumber) {
            this.routeNumber = routeNumber;
            return this;
        }

        public BusBuilder setModelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public BusBuilder setKilometrage(int kilometrage) {
            this.kilometrage = kilometrage;
            return this;
        }

        /**
         * Создаёт объект {@link Bus} после валидации всех полей.
         *
         * @throws IllegalArgumentException если хотя бы одно поле невалидно
         */
        public Bus build(){
            BusValidator.validateRouteNumber(routeNumber);
            BusValidator.validateModelName(modelName);
            BusValidator.validateKilometrage(kilometrage);
            return new Bus(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Автобус %s | Номер маршрута %d | пробег %d км.", modelName, routeNumber, kilometrage);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true; //Сравнение по ссылке для оптимизации
        if (object == null || getClass() != object.getClass()) return false;
        Bus bus = (Bus) object;
        return routeNumber == bus.routeNumber && kilometrage == bus.kilometrage && Objects.equals(modelName, bus.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeNumber, modelName, kilometrage);
    }
}
