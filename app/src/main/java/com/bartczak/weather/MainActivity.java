package com.bartczak.weather;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.WeatherViewModel;
import com.bartczak.weather.api.dto.WeatherForecastResponse;
import com.bartczak.weather.api.dto.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel weatherViewModel;
    private RequestQueue queue;
    private final Gson gson = new Gson().newBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> {
                Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            })
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.toInstant(ZoneId.systemDefault().getRules().getOffset(src)).toEpochMilli()))
            .create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        this.weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        String city = this.getSharedPreferences("weather", 0).getString("city", "");

        boolean needsRefresh = loadSavedWeather();
        if (needsRefresh) {
            fetchWeather();
        }
        createAdapter(!city.equals(""));
    }

    private void createAdapter(boolean citySet) {
        ViewPager2Adapter viewPagerAdapter = new ViewPager2Adapter(this);
        ViewPager2 viewPager = findViewById(R.id.fragmentContainer);

        viewPager.setAdapter(viewPagerAdapter);
        if (!citySet) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(1);
        }
    }

    public void fetchWeather() {
        WeatherApi.Unit units = this.getSharedPreferences("settings", 0).getString("unit", "metric")
                .equals("metric") ? WeatherApi.Unit.METRIC : WeatherApi.Unit.IMPERIAL;
        String city = this.getSharedPreferences("settings", 0).getString("city", "");

        if (!city.equals("")) {
            JsonObjectRequest currentWeatherRequest = new JsonObjectRequest
                    (Request.Method.GET, WeatherApi.getWeatherUrl(city, units), null, response -> {
                        WeatherResponse weather = gson.fromJson(response.toString(), WeatherResponse.class);
                        weather.setUnit(units);
                        weather.setFetchedAt(LocalDateTime.now());

                        String weatherJson = gson.toJson(weather);
                        this.weatherViewModel.setWeather(weather);
                        this.getSharedPreferences("weather", 0).edit().putString("weather", weatherJson).apply();
                    }, this::makeErrorToast);

            JsonObjectRequest weatherForecastRequest = new JsonObjectRequest
                    (Request.Method.GET, WeatherApi.getForecastUrl(city, units, 16), null, response -> {
                        WeatherForecastResponse forecast = gson.fromJson(response.toString(), WeatherForecastResponse.class);
                        forecast.setUnit(units);
                        forecast.setFetchedAt(LocalDateTime.now());

                        String forecastJson = gson.toJson(forecast);
                        this.weatherViewModel.setWeatherForecast(forecast);
                        this.getSharedPreferences("weather", 0).edit().putString("forecast", forecastJson).apply();
                    }, error -> {

                    });

            this.queue.add(currentWeatherRequest);
            this.queue.add(weatherForecastRequest);
        }
    }

    private void makeErrorToast(VolleyError error) {
        if (error.networkResponse == null) {
            Toast.makeText(this, "You need an internet connection to fetch the current weather", Toast.LENGTH_LONG).show();
        }
        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == 404) {
                Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean loadSavedWeather() {
        WeatherResponse weather = gson.fromJson(this.getSharedPreferences("weather", 0).getString("weather", ""), WeatherResponse.class);
        WeatherForecastResponse forecast = gson.fromJson(this.getSharedPreferences("weather", 0).getString("forecast", ""), WeatherForecastResponse.class);
        if (weather != null) {
            this.weatherViewModel.setWeather(weather);
        }
        if (forecast != null) {
            this.weatherViewModel.setWeatherForecast(forecast);
        }
        if (weather == null || forecast == null
                || weather.getFetchedAt() == null || forecast.getFetchedAt() == null) {
            return true;
        }
        return weather.getFetchedAt().isBefore(LocalDateTime.now().minusMinutes(15)) || forecast.getFetchedAt().isBefore(LocalDateTime.now().minusMinutes(15));
    }
}

