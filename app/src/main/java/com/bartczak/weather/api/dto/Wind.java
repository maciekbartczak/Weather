package com.bartczak.weather.api.dto;

public class Wind {
    private final String speed;
    private final String deg;

    public Wind(String speed, String deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public String getSpeed() {
        return speed;
    }

    public String getDeg() {
        return deg;
    }
}
