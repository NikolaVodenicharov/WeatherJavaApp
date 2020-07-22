package com.example.weath.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weath.R;
import com.example.weath.businessLogic.viewModels.DisplayWeatherViewModel;
import com.example.weath.data.models.CurrentWeatherAndForecast;
import com.example.weath.data.models.SkyCondition;
import com.example.weath.databinding.ActivityDisplayWeatherBinding;

import java.util.Objects;

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

        observeSkyCondition();
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

    private void observeSkyCondition() {
        viewModel.weather.observe(this, new Observer<CurrentWeatherAndForecast>() {
            @Override
            public void onChanged(CurrentWeatherAndForecast currentWeatherAndForecast) {
                boolean shouldDisplayCurrentSkyCondition = currentWeatherAndForecast.currentWeather != null;
                if (shouldDisplayCurrentSkyCondition){
                    SkyCondition skyCondition = currentWeatherAndForecast.currentWeather.skyCondition;
                    int drawableId = findSkyConditionDrawableId(skyCondition);
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageResource(drawableId);
                }

                boolean shouldDisplayForecastSkyCondition = currentWeatherAndForecast.forecast != null &&
                        currentWeatherAndForecast.forecast.size() > 0;

                if (shouldDisplayForecastSkyCondition){

                }
            }
        });
    }
    private int findSkyConditionDrawableId(SkyCondition skyCondition){
        switch (skyCondition){
            case CLEAR:
                return R.drawable.sun;
            case CLOUDS:
                return R.drawable.clouds;
            case RAIN:
                return R.drawable.heavy_rain;
            case THUNDERSTORM:
                return R.drawable.cloud_lightning;
            case SNOW:
                return R.drawable.snow;
            default:
                throw new IllegalArgumentException("Input parameter is not found.");
        }
    }
}