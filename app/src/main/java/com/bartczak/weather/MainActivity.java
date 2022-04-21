package com.bartczak.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new BasicWeatherFragment());

        Button basicWeatherButton = findViewById(R.id.BasicButton);
        Button advancedWeatherButton = findViewById(R.id.AdvancedButton);
        Button forecastWeatherButton = findViewById(R.id.ForecastButton);

        basicWeatherButton.setOnClickListener(v -> loadFragment(new BasicWeatherFragment()));
        advancedWeatherButton.setOnClickListener(v -> loadFragment(new AdvancedWeatherFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment, null);
        transaction.commit();
    }
}