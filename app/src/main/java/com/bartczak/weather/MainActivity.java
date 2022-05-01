package com.bartczak.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.WeatherViewModel;
import com.bartczak.weather.api.dto.WeatherForecastResponse;
import com.bartczak.weather.api.dto.WeatherResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String CITY_NAME = "Lodz";
    private WeatherViewModel weatherViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        this.weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        WeatherApi.Unit units = WeatherApi.Unit.METRIC;

        JsonObjectRequest currentWeatherRequest = new JsonObjectRequest
                (Request.Method.GET, WeatherApi.getWeatherUrl("Lodz", units), null, response -> {
                    WeatherResponse weather = gson.fromJson(response.toString(), WeatherResponse.class);
                    weather.setUnit(units);

                    this.weatherViewModel.setWeather(weather);
                }, error -> {
                    Toast.makeText(this,
                            "Internet connection is needed to fetch the current data", Toast.LENGTH_LONG).show();
                });
        JsonObjectRequest weatherForecastRequest = new JsonObjectRequest
                (Request.Method.GET, WeatherApi.getForecastUrl("Lodz", units, 16), null, response -> {
                    WeatherForecastResponse forecast = gson.fromJson(response.toString(), WeatherForecastResponse.class);
                    forecast.setUnit(units);

                    this.weatherViewModel.setWeatherForecast(forecast);
                }, error -> {
                    Toast.makeText(this,
                            "Internet connection is needed to fetch the forecast", Toast.LENGTH_LONG).show();
                });

        queue.add(currentWeatherRequest);
        queue.add(weatherForecastRequest);

        createAdapter();
    }

    private void createAdapter() {
        ViewPager2Adapter viewPagerAdapter = new ViewPager2Adapter(this);
        ViewPager2 viewPager = findViewById(R.id.fragmentContainer);

        viewPager.setAdapter(viewPagerAdapter);
    }
}