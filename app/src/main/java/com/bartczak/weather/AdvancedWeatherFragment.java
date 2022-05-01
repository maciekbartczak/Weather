package com.bartczak.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.WeatherViewModel;
import com.bartczak.weather.api.dto.WeatherResponse;

public class AdvancedWeatherFragment extends Fragment {

    WeatherViewModel weatherViewModel;

    public AdvancedWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View root = inflater.inflate(R.layout.fragment_advanced_weather, container, false);
        this.weatherViewModel = new ViewModelProvider(activity).get(WeatherViewModel.class);

        setTextViews(root);

        return root;
    }

    private void setTextViews(View view) {
        this.weatherViewModel.getWeather().observe(getViewLifecycleOwner(), weather -> {

            TextView windSpeed = view.findViewById(R.id.windSpeed);
            TextView windDirection = view.findViewById(R.id.windDirection);
            TextView humidity = view.findViewById(R.id.humidity);
            TextView visibility = view.findViewById(R.id.visibility);

            String windSpeedUnit = weather.getUnit() == WeatherApi.Unit.METRIC ? "m/s" : "mph";
            String visibilityUnit = weather.getUnit() == WeatherApi.Unit.METRIC ? "m" : "mi";

            windSpeed.setText(weather.getWind().getSpeed() + windSpeedUnit);
            windDirection.setText(weather.getWind().getDeg() + "°");
            humidity.setText(weather.getMain().getHumidity() + "%");
            visibility.setText(weather.getVisibility() + " " + visibilityUnit);
        });
    }
}