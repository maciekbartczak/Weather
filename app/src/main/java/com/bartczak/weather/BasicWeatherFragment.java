package com.bartczak.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.WeatherViewModel;
import com.bartczak.weather.api.dto.WeatherResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;

public class BasicWeatherFragment extends Fragment {

    WeatherViewModel weatherViewModel;

    public BasicWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_basic_weather, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return root;
        }

        this.weatherViewModel = new ViewModelProvider(activity).get(WeatherViewModel.class);

        setTextViews(root);

        return root;
    }

    private void setTextViews(View view) {

        this.weatherViewModel.getWeather().observe(getViewLifecycleOwner(), weather -> {
            TextView cityName = view.findViewById(R.id.cityName);
            TextView coordinates = view.findViewById(R.id.coordinates);
            TextView temperature = view.findViewById(R.id.temperature);
            TextView pressure = view.findViewById(R.id.pressure);
            TextView description = view.findViewById(R.id.description);
            TextView time = view.findViewById(R.id.time);

            String temperatureUnit = weather.getUnit() == WeatherApi.Unit.METRIC ? "°C" : "°F";
            String pressureUnit = weather.getUnit() == WeatherApi.Unit.METRIC ? "hPa" : "inHg";
            String currentTime = Instant
                    .ofEpochSecond(weather.getDt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime()
                    .toString();

            cityName.setText(weather.getName());
            coordinates.setText(weather.getCoord().getLat() + "° " + weather.getCoord().getLon() + "°");
            temperature.setText(weather.getMain().getTemp() + temperatureUnit);
            pressure.setText(weather.getMain().getPressure() + pressureUnit);
            description.setText(weather.getWeather()[0].getDescription());
            time.setText(currentTime);
        });
    }
}