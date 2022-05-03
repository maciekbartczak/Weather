package com.bartczak.weather;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return root;
        }

        EditText cityInput = root.findViewById(R.id.city_input);
        RadioGroup unitGroup = root.findViewById(R.id.unit_group);
        RadioButton metricRadio = root.findViewById(R.id.metric_button);
        RadioButton imperialRadio = root.findViewById(R.id.imperial_button);
        Button refreshButton = root.findViewById(R.id.refresh_button);

        cityInput.setText(getActivity().getSharedPreferences("settings", 0).getString("city", ""));
        if (getActivity().getSharedPreferences("settings", 0).getString("unit", "").equals("metric")) {
            metricRadio.setChecked(true);
        } else {
            imperialRadio.setChecked(true);
        }

        unitGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                activity.getSharedPreferences("settings", 0)
                        .edit()
                        .putString("unit", checkedId == R.id.metric_button ? "metric" : "imperial")
                        .apply();
                activity.fetchWeather();
            }
        });

        cityInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                activity.getSharedPreferences("settings", 0)
                        .edit()
                        .putString("city", cityInput.getText().toString())
                        .apply();
                activity.fetchWeather();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.fetchWeather();
            }
        });

        return root;
    }
}