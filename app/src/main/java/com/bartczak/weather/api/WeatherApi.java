package com.bartczak.weather.api;

public class WeatherApi {

    private WeatherApi() {
    }

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String API_KEY = "07746af7962e2bfa2136c0ce9f1477ed";

    public static String getWeatherUrl(String city) {
        return BASE_URL + "data/2.5/weather?q=" + city + "&appid=" + API_KEY;
    }

}
