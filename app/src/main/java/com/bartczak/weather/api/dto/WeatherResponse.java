package com.bartczak.weather.api.dto;

import com.bartczak.weather.api.WeatherApi;

import java.time.LocalDateTime;

public class WeatherResponse {
    private final Coord coord;
    private final WeatherDescription[] weather;
    private final String base;
    private final DetailedWeatherDescription main;
    private final String visibility;
    private final Wind wind;
    private final Clouds clouds;
    private final long dt;
    private final Sys sys;
    private final String timezone;
    private final long id;
    private final String name;
    private final int cod;
    private LocalDateTime fetchedAt;

    private WeatherApi.Unit unit;

    public WeatherResponse(Coord coord, WeatherDescription[] weather, String base, DetailedWeatherDescription main, String visibility, Wind wind, Clouds clouds, long dt, Sys sys, String timezone, long id, String name, int cod, WeatherApi.Unit unit) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.timezone = timezone;
        this.id = id;
        this.name = name;
        this.cod = cod;
        this.unit = unit;
    }

    public Coord getCoord() {
        return coord;
    }

    public WeatherDescription[] getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public DetailedWeatherDescription getMain() {
        return main;
    }

    public String getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public long getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public String getTimezone() {
        return timezone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    public WeatherApi.Unit getUnit() {
        return unit;
    }

    public void setUnit(WeatherApi.Unit units) {
        this.unit = units;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }
}
