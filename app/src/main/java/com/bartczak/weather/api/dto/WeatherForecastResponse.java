package com.bartczak.weather.api.dto;

import com.bartczak.weather.api.WeatherApi;

import java.util.List;

public class WeatherForecastResponse {
    private final String cod;
    private final String message;
    private final int cnt;
    private final List<WeatherForecast> list;

    private WeatherApi.Unit unit;

    public WeatherForecastResponse(String cod, String message, int cnt, List<WeatherForecast> list) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    public String getCod() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public List<WeatherForecast> getList() {
        return list;
    }

    public WeatherApi.Unit getUnit() {
        return unit;
    }

    public void setUnit(WeatherApi.Unit unit) {
        this.unit = unit;
    }
}