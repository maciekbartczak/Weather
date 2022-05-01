package com.bartczak.weather.api.dto;

public class FeelsLike {
    private final double day;
    private final double night;
    private final double eve;
    private final double morn;

    public FeelsLike(double day, double night, double eve, double morn) {
        this.day = day;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public double getDay() {
        return day;
    }

    public double getNight() {
        return night;
    }

    public double getEve() {
        return eve;
    }

    public double getMorn() {
        return morn;
    }
}
