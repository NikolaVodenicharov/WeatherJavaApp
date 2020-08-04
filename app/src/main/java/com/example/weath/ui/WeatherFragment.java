package com.example.weath.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.weath.R;
import com.example.weath.businessLogic.viewModels.StartViewModel;
import com.example.weath.data.models.ForecastDay;
import com.example.weath.data.models.SkyCondition;
import com.example.weath.data.models.Weather;
import com.example.weath.databinding.FragmentWeatherBinding;

import java.util.List;

//ToDo if there is no internet or permission is denied, also if location permission is denied, what will this fragment display ?
public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;
    StartViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(getActivity()).get(StartViewModel.class);
        binding.setViewModel(viewModel);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeSkyCondition();
        observeForecast();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.fillCityWeather();
    }

    private void observeSkyCondition() {
        viewModel.getWeather().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                boolean shouldDisplayCurrentSkyCondition = weather.currentWeather != null;
                if (!shouldDisplayCurrentSkyCondition){
                    return;
                }

                SkyCondition skyCondition = weather.currentWeather.skyCondition;
                int drawableId = findSkyConditionDrawableId(skyCondition);
                ImageView imageView = getView().findViewById(R.id.imageView);
                imageView.setImageResource(drawableId);
            }
        });
    }

    private void observeForecast() {
        viewModel.getWeather().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                boolean shouldDisplayForecast = weather.forecast != null &&
                        weather.forecast.size() > 0;

                if (!shouldDisplayForecast){
                    return;
                }

                List<ForecastDay> forecast = weather.forecast;
                ForecastAdapter adapter = new ForecastAdapter(forecast);

                RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewForecast);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }

    public static int findSkyConditionDrawableId(SkyCondition skyCondition){
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