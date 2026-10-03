package by.coldynee.model;

import by.coldynee.validation.BusValidator;

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
        public Bus build(){
            BusValidator.validateRouteNumber(routeNumber);
            BusValidator.validateModelName(modelName);
            BusValidator.validateKilometrage(kilometrage);
            return new Bus(this);
        }
    }
}
