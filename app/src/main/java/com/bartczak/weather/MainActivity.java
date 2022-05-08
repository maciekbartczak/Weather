package com.bartczak.weather;

import android.content.Context;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, WeatherResponse> weatherMap = new HashMap<>();
    private HashMap<String, WeatherForecastResponse> weatherForecastMap = new HashMap<>();

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

        loadSavedWeather();
        fetchWeather();
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

                        this.weatherMap.put(city, weather);

                        Set<String> favorites = this.getSharedPreferences("favorites", 0).getStringSet("cities", new HashSet<>());
                        if (favorites.contains(city)) {
                            String weatherJson = gson.toJson(this.weatherMap);
                            this.getSharedPreferences("weather", 0).edit().putString("weather", weatherJson).apply();
                        }

                        this.weatherViewModel.setWeather(weather);
                    }, error -> {
                       this.makeErrorToast(error);
                       if (this.weatherMap.containsKey(city)) {
                           this.weatherViewModel.setWeather(this.weatherMap.get(city));
                       }
                    });

            JsonObjectRequest weatherForecastRequest = new JsonObjectRequest
                    (Request.Method.GET, WeatherApi.getForecastUrl(city, units, 16), null, response -> {
                        WeatherForecastResponse forecast = gson.fromJson(response.toString(), WeatherForecastResponse.class);
                        forecast.setUnit(units);
                        forecast.setFetchedAt(LocalDateTime.now());

                        this.weatherForecastMap.put(city, forecast);

                        Set<String> favorites = this.getSharedPreferences("favorites", 0).getStringSet("cities", new HashSet<>());
                        if (favorites.contains(city)) {
                            String forecastJson = gson.toJson(this.weatherForecastMap);
                            this.getSharedPreferences("weather", 0).edit().putString("forecast", forecastJson).apply();
                        }

                        this.weatherViewModel.setWeatherForecast(forecast);
                    }, error -> {
                        if (this.weatherForecastMap.containsKey(city)) {
                            this.weatherViewModel.setWeatherForecast(this.weatherForecastMap.get(city));
                        }
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

    private void loadSavedWeather() {
        Type weatherType = new TypeToken<HashMap<String, WeatherResponse>>(){}.getType();
        Type forecastType = new TypeToken<HashMap<String, WeatherForecastResponse>>(){}.getType();

        String weatherJson = this.getSharedPreferences("weather", 0).getString("weather", "");
        String forecastJson = this.getSharedPreferences("weather", 0).getString("forecast", "");

        if (!weatherJson.isEmpty()) {
            this.weatherMap = gson.fromJson(weatherJson, weatherType);
        }
        if (!forecastJson.isEmpty()) {
            this.weatherForecastMap = gson.fromJson(forecastJson, forecastType);
        }
    }
}

