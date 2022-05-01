package com.bartczak.weather.api;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bartczak.weather.api.dto.WeatherForecastResponse;
import com.bartczak.weather.api.dto.WeatherResponse;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherResponse> weather = new MutableLiveData<>();
    private final MutableLiveData<WeatherForecastResponse> weatherForecast = new MutableLiveData<>();

    public MutableLiveData<WeatherResponse> getWeather() {
        return weather;
    }

    public void setWeather(WeatherResponse weatherResponse) {
        weather.setValue(weatherResponse);
    }

    public MutableLiveData<WeatherForecastResponse> getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(WeatherForecastResponse weatherForecastResponse) {
        weatherForecast.setValue(weatherForecastResponse);
    }
}
