package com.example.weath.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weath.R;

public class DisplayWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather);

        Intent intent = getIntent();
        String searchedCity = intent.getStringExtra(SearchCityActivity.SEARCHED_CITY);

        Toast.makeText(this, searchedCity, Toast.LENGTH_SHORT).show();
    }
}