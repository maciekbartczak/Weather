package com.bartczak.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.WeatherViewModel;
import com.squareup.picasso.Picasso;

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

        setWeatherData(root);

        return root;
    }

    private void setWeatherData(View view) {

        this.weatherViewModel.getWeather().observe(getViewLifecycleOwner(), weather -> {
            TextView cityName = view.findViewById(R.id.cityName);
            TextView coordinates = view.findViewById(R.id.coordinates);
            TextView temperature = view.findViewById(R.id.temperature);
            TextView pressure = view.findViewById(R.id.pressure);
            TextView description = view.findViewById(R.id.description);
            TextView time = view.findViewById(R.id.time);
            ImageView weatherIcon = view.findViewById(R.id.weather_icon);

            Picasso.get()
                    .load(WeatherApi.getIconUrl(weather.getWeather()[0].getIcon()))
                    .into(weatherIcon);

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