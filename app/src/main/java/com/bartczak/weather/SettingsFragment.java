package com.bartczak.weather;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Set;


public class SettingsFragment extends Fragment {

    private MainActivity activity;
    private EditText cityInput;
    private Button favoriteButton;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        this.activity = (MainActivity) getActivity();
        if (this.activity == null) {
            return root;
        }

        cityInput = root.findViewById(R.id.city_input);
        favoriteButton = root.findViewById(R.id.favorite_button);
        RadioGroup unitGroup = root.findViewById(R.id.unit_group);
        RadioButton metricRadio = root.findViewById(R.id.metric_button);
        RadioButton imperialRadio = root.findViewById(R.id.imperial_button);
        Button refreshButton = root.findViewById(R.id.refresh_button);
        LinearLayout favoriteList = root.findViewById(R.id.favorite_list);

        Set<String> favoriteCities = new HashSet<>(getActivity().getSharedPreferences("favorites", 0).getStringSet("cities", new HashSet<>()));

        populateFavoriteList(favoriteList, favoriteCities);

        String currentCity = getActivity().getSharedPreferences("settings", 0).getString("city", "");
        if (favoriteCities.contains(currentCity)) {
            favoriteButton.setText(R.string.delete_label);
        }
        cityInput.setText(currentCity);



        if (getActivity().getSharedPreferences("settings", 0).getString("unit", "").equals("metric")) {
            metricRadio.setChecked(true);
        } else {
            imperialRadio.setChecked(true);
        }

        unitGroup.setOnCheckedChangeListener((group, checkedId) -> {
            activity.getSharedPreferences("settings", 0)
                    .edit()
                    .putString("unit", checkedId == R.id.metric_button ? "metric" : "imperial")
                    .apply();
            activity.fetchWeather();
        });

        cityInput.setOnFocusChangeListener((view, b) -> {
            activity.getSharedPreferences("settings", 0)
                    .edit()
                    .putString("city", cityInput.getText().toString())
                    .apply();
            activity.fetchWeather();
        });

        cityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (favoriteCities.contains(cityInput.getText().toString())) {
                    favoriteButton.setText(R.string.delete_label);
                } else {
                    favoriteButton.setText(R.string.favorite_label);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        favoriteButton.setOnClickListener(v -> {
            String city = cityInput.getText().toString();
            if (city.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a city", Toast.LENGTH_SHORT).show();
                return;
            }

            if (favoriteCities.contains(city)) {
                favoriteCities.remove(city);
                favoriteButton.setText(R.string.favorite_label);
            } else {
                favoriteCities.add(city);
                favoriteButton.setText(R.string.delete_label);
            }

            activity.getSharedPreferences("favorites", 0)
                    .edit()
                    .putStringSet("cities", favoriteCities)
                    .apply();
            populateFavoriteList(favoriteList, favoriteCities);
        });

        refreshButton.setOnClickListener(v -> activity.fetchWeather());

        return root;
    }

    private void populateFavoriteList(LinearLayout favoriteList, Set<String> favoriteCities) {
        favoriteList.removeAllViews();
        for (String city : favoriteCities) {
            TextView cityItem = createCityItem(city);
            favoriteList.addView(cityItem);
        }
    }

    private TextView createCityItem(String city) {
        TextView cityView = new TextView(getContext());
        cityView.setText(city);
        cityView.setClickable(true);
        cityView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.favorite_text_size));

        cityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView cityView = (TextView) v;
                String city = cityView.getText().toString();
                activity.getSharedPreferences("settings", 0)
                        .edit()
                        .putString("city", city)
                        .apply();
                cityInput.setText(city);

                activity.fetchWeather();
            }
        });
        return cityView;
    }

}