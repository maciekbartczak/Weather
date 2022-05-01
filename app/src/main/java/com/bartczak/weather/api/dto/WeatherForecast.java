package com.bartczak.weather.api.dto;

import java.util.List;

public class WeatherForecast {
    private final String dt;
    private final DetailedWeatherDescription main;
    private final List<WeatherDescription> weather;
    private final Wind wind;
    private final String visibility;
    private final String pop;
    private final String dt_txt;


    public WeatherForecast(String dt, DetailedWeatherDescription main, List<WeatherDescription> weather, Wind wind, String visibility, String pop, String dt_txt) {
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.wind = wind;
        this.visibility = visibility;
        this.pop = pop;
        this.dt_txt = dt_txt;
    }

    public String getDt() {
        return dt;
    }

    public DetailedWeatherDescription getMain() {
        return main;
    }

    public List<WeatherDescription> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getPop() {
        return pop;
    }

    public String getDt_txt() {
        return dt_txt;
    }
}
