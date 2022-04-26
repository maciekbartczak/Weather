package com.bartczak.weather;

public class WeatherApi {

    private WeatherApi() {
    }

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String API_KEY = "07746af7962e2bfa2136c0ce9f1477ed";

    public static final String getGeoUrl(String city) {
        return BASE_URL + "geo/1.0/direct?q=" + city + "&appid=" + API_KEY;
    }
}
