package com.bartczak.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class BasicWeatherFragment extends Fragment {


    public BasicWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, WeatherApi.getGeoUrl("Lodz"), null, response -> {

                }, error ->  {
                    System.out.println(error);
                });
        queue.add(jsonObjectRequest);

        return inflater.inflate(R.layout.fragment_basic_weather, container, false);
    }


}