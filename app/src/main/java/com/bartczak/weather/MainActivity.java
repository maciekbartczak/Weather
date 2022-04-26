package com.bartczak.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.dto.WeatherResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String CITY_NAME = "Lodz";
    private WeatherResponse weatherResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Gson gson = new Gson();

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, WeatherApi.getWeatherUrl("Lodz"), null, response -> {
                    this.weatherResponse = gson.fromJson(response.toString(), WeatherResponse.class);
                    this.createAdapter();
                }, error -> {
                    error.printStackTrace();
                    this.createAdapter();
                });

        queue.add(request);
    }

    public WeatherResponse getWeatherResponse() {
        return weatherResponse;
    }

    private void createAdapter() {
        ViewPager2Adapter viewPagerAdapter = new ViewPager2Adapter(this);
        ViewPager2 viewPager = findViewById(R.id.fragmentContainer);

        viewPager.setAdapter(viewPagerAdapter);
    }
}