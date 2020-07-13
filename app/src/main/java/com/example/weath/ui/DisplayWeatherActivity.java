package com.example.weath.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.weath.R;
import com.example.weath.businessLogic.viewModels.DisplayWeatherViewModel;
import com.example.weath.databinding.ActivityDisplayWeatherBinding;

public class DisplayWeatherActivity extends AppCompatActivity {
    private ActivityDisplayWeatherBinding binding;
    private DisplayWeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather);

        initializeBinding();

        String searchedCity = extractSearchedCity();
        viewModel.getWeather(searchedCity);

    }

    //Todo: this code is repeated in activities, may be i can extract it in base activity, but i need viewModel in the child activity
    private void initializeBinding() {
        viewModel = new ViewModelProvider(this).get(DisplayWeatherViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_display_weather);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private String extractSearchedCity() {
        Intent intent = getIntent();
        return intent.getStringExtra(SearchCityActivity.SEARCHED_CITY);
    }
}