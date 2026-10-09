package by.coldynee.model;

import by.coldynee.validation.BusValidator;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Неизменяемая модель автобуса.
 * <p>
 * Создаётся только через {@link BusBuilder}, который гарантирует валидацию полей.
 * </p>
 */
public class Bus {

    //Паттерн проверки валидность
    public static final Pattern RUSSIAN_CAR_NUMBER_PATTERN = Pattern.compile("^[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}\\d{2,3}$");
    //Максимальный реалистичный пробег
    public static final int MAX_KILOMETRAGE = 2_000_000;
    //Максимальная реалистичная длина имени
    public static final int MAX_MODEL_NAME_LENGTH = 50;


    private final String stateBusNumber;
    private final String modelName;
    private final int kilometrage;

    private Bus(BusBuilder builder, String stateBusNumber) {
        this.stateBusNumber = stateBusNumber;
        this.modelName = builder.modelName;
        this.kilometrage = builder.kilometrage;
    }

    public String getStateBusNumber() {
        return stateBusNumber;
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
        private String stateBusNumber;
        private String modelName;
        private int kilometrage;

        public BusBuilder setStateBusNumber(String stateBusNumber) {
            this.stateBusNumber = stateBusNumber;
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
            String normalizedStateBusNumber = stateBusNumber != null
                    ? stateBusNumber.replaceAll("[\\s-]", "").toUpperCase()
                    : null;
            BusValidator.validateStateBusNumber(normalizedStateBusNumber);
            BusValidator.validateModelName(modelName);
            BusValidator.validateKilometrage(kilometrage);
            return new Bus(this, normalizedStateBusNumber);
        }
    }

    @Override
    public String toString() {
        return String.format("Автобус %s | Гос. Номер %s | пробег %d км.", modelName, stateBusNumber, kilometrage);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Bus bus = (Bus) object;
        return kilometrage == bus.kilometrage && Objects.equals(stateBusNumber, bus.stateBusNumber) && Objects.equals(modelName, bus.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateBusNumber, modelName, kilometrage);
    }
}
