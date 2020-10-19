package com.example.weath.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weath.R;
import com.example.weath.databinding.FragmentWeatherBinding;
import com.example.weath.domain.models.SkyCondition;
import com.example.weath.ui.models.ForecastDayUi;
import com.example.weath.ui.models.WeatherUi;
import com.example.weath.ui.utils.ForecastAdapter;
import com.example.weath.ui.viewModels.StartViewModel;

import java.util.List;

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
    public void onResume() {
        super.onResume();

        viewModel.fillCityWeather();

        observeSkyCondition();
        observeForecast();
        observeErrorMessage();
    }

    private void observeErrorMessage() {
        viewModel.getWeatherUiLiveData().observe(this, new Observer<WeatherUi>() {
            @Override
            public void onChanged(WeatherUi weatherUi) {
                TextView errorMessageTextView = getView().findViewById(R.id.error_message);

                boolean noErrorMessage =
                        viewModel.getWeatherUiLiveData().getValue() == null ||
                                viewModel.getWeatherUiLiveData().getValue().getErrorMessage() == null ||
                                viewModel.getWeatherUiLiveData().getValue().getErrorMessage().isEmpty();

                if (noErrorMessage){
                    errorMessageTextView.setVisibility(View.GONE);
                }
                else{
                    errorMessageTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void observeSkyCondition() {
        viewModel.getWeatherUiLiveData().observe(this, new Observer<WeatherUi>() {
            @Override
            public void onChanged(WeatherUi weatherUi) {
                if (weatherUi == null){
                    return;
                }

                SkyCondition skyCondition = weatherUi.getSkyCondition();
                int drawableId = findSkyConditionDrawableId(skyCondition);
                ImageView imageView = getView().findViewById(R.id.imageView);
                imageView.setImageResource(drawableId);
            }
        });
    }

    private void observeForecast() {
        viewModel.getWeatherUiLiveData().observe(this, new Observer<WeatherUi>() {
            @Override
            public void onChanged(WeatherUi weatherUi) {
                boolean shouldDisplayForecast = weatherUi.getForecast() != null &&
                        weatherUi.getForecast().size() > 0;

                if (!shouldDisplayForecast){
                    return;
                }

                List<ForecastDayUi> forecast = weatherUi.getForecast();
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