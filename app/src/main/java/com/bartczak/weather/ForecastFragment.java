package com.bartczak.weather;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.WeatherViewModel;
import com.bartczak.weather.api.dto.WeatherForecast;


public class ForecastFragment extends Fragment {

    WeatherViewModel weatherViewModel;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forecast, container, false);
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return root;
        }

        this.weatherViewModel = new ViewModelProvider(activity).get(WeatherViewModel.class);

        populateForecast(root);

        return root;
    }

    private void populateForecast(View view) {
        LinearLayout forecastLayout = (LinearLayout) view.findViewById(R.id.row_holder);

        this.weatherViewModel.getWeatherForecast().observe(getViewLifecycleOwner(), forecast -> {
            forecastLayout.removeAllViewsInLayout();


            for (int i = 0; i < forecast.getList().size(); i++) {
                WeatherForecast forecastItem = forecast.getList().get(i);
                ConstraintLayout row = (ConstraintLayout) getLayoutInflater().inflate(R.layout.forecast_row, forecastLayout, false);
                TextView date = row.findViewById(R.id.date);
                TextView temp = row.findViewById(R.id.temperature);
                TextView description = row.findViewById(R.id.forecast_description);

                String savedUnit = getActivity().getSharedPreferences("settings", 0).getString("unit", "");
                String temperatureValue = WeatherApi.convertTemperature(forecast.getUnit(), savedUnit, forecastItem.getMain().getTemp());
                String tempUnit = savedUnit.equals("metric") ? "°C" : "°F";


                date.setText(forecastItem.getDt_txt().substring(0, 10) + " " + forecastItem.getDt_txt().substring(11, 16));
                temp.setText(temperatureValue + tempUnit);
                description.setText(forecastItem.getWeather().get(0).getDescription());


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 16);
                row.setLayoutParams(params);

                forecastLayout.addView(row);
            }
        });
    }
}