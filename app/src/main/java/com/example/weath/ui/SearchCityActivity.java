package com.example.weath.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.weath.R;
import com.example.weath.businessLogic.viewModels.SearchCityViewModel;
import com.example.weath.databinding.ActivitySearchCityBinding;

public class SearchCityActivity extends AppCompatActivity {
    public static final String SEARCHED_CITY = "searchedCity";

    private ActivitySearchCityBinding binding;
    private SearchCityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeBindings();
        observeForDisplayWeather();
    }

    private void initializeBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_city);
        viewModel = new ViewModelProvider(this).get(SearchCityViewModel.class);
        binding.setViewModel(viewModel);
    }

    private void observeForDisplayWeather() {
        viewModel.isDisplayWeatherRequired.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Intent intent = new Intent(SearchCityActivity.this, DisplayWeatherActivity.class);
                    intent.putExtra(SEARCHED_CITY, viewModel.searchedCity);
                    startActivity(intent);
                }
            }
        });
    }
}