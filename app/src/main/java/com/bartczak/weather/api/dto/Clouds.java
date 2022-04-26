package com.bartczak.weather.api.dto;

public class Clouds {
    private final String all;

    public Clouds(String all) {
        this.all = all;
    }

    public String getAll() {
        return all;
    }
}
