package com.bartczak.weather.api;

public class WeatherApi {

    private WeatherApi() {
    }

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String API_KEY = "07746af7962e2bfa2136c0ce9f1477ed";

    public enum Unit {
        METRIC("metric"),
        IMPERIAL("imperial");

        private final String value;

        Unit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static String getWeatherUrl(String city, Unit unit) {
        return BASE_URL + "data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=" + unit.getValue();
    }

    public static String getForecastUrl(String city, Unit unit, int count) {
        return BASE_URL + "data/2.5/forecast?q=" + city + "&appid=" + API_KEY +
                "&units=" + unit.getValue();
    }

}
