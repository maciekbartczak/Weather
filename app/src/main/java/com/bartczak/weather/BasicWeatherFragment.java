package com.bartczak.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bartczak.weather.api.WeatherApi;
import com.bartczak.weather.api.dto.WeatherResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class BasicWeatherFragment extends Fragment {

    WeatherResponse weather;

    public BasicWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        this.weather = activity.getWeatherResponse();

        View root = inflater.inflate(R.layout.fragment_basic_weather, container, false);
        TextView cityName = root.findViewById(R.id.cityName);
        TextView coordinates = root.findViewById(R.id.coordinates);
        TextView temperature = root.findViewById(R.id.temperature);
        TextView pressure = root.findViewById(R.id.pressure);
        TextView description = root.findViewById(R.id.description);

        cityName.setText(this.weather.getName());
        coordinates.setText(this.weather.getCoord().getLat() + " " + this.weather.getCoord().getLon());
        temperature.setText(this.weather.getMain().getTemp());
        pressure.setText(this.weather.getMain().getPressure());
        description.setText(this.weather.getWeather()[0].getDescription());

        return root;
    }

}