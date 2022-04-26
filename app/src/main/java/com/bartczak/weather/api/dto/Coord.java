package com.bartczak.weather.api.dto;

public class Coord {
    private final String lon;
    private final String lat;

    public Coord(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }
}
