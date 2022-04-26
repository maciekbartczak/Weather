package com.bartczak.weather.api.dto;

public class Sys {
    private final int type;
    private final int id;
    private final String country;
    private final String sunrise;
    private final String sunset;

    public Sys(int type, int id, String country, String sunrise, String sunset) {
        this.type = type;
        this.id = id;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}
