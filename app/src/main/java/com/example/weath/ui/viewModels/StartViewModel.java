package com.example.weath.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.domain.models.ForecastDay;
import com.example.weath.domain.models.Weather;
import com.example.weath.ui.models.ForecastDayUi;
import com.example.weath.ui.models.WeatherUi;

import java.util.ArrayList;
import java.util.List;

public class StartViewModel extends ViewModel {
    private MutableLiveData<WeatherUi> weatherUiLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSearchWeatherCalled = new MutableLiveData<>(false);

    public String searchedCity;

    public MutableLiveData<WeatherUi> getWeatherUiLiveData() {
        return weatherUiLiveData;
    }
    public LiveData<Boolean> getIsSearchWeatherCalled() {
        return isSearchWeatherCalled;
    }

    public void searchWeatherCalledSignal(){
        isSearchWeatherCalled.setValue(true);
        isSearchWeatherCalled.setValue(false);
    }

    public void fillCityWeather(){
        LiveData<Weather> result = App.weatherCases.getWeather(searchedCity);

        result.observeForever(new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                WeatherUi weatherUi = toWeatherUi(weather);

                weatherUiLiveData.setValue(weatherUi);
            }
        });
    }

    private WeatherUi toWeatherUi (Weather weather){
        WeatherUi weatherUi = new WeatherUi(
                weather.getCityName(),
                weather.getRecordMoment(),
                weather.getTemperatureInCelsius(),
                weather.getSkyCondition(),
                weather.getErrorMessage(),
                toForecastDayUiCollection(weather.getForecast()));

        return weatherUi;
    }
    private List<ForecastDayUi> toForecastDayUiCollection(List<ForecastDay> forecast){
        List<ForecastDayUi> forecastUi = new ArrayList<>(forecast.size());

        for (int i = 0; i < forecast.size(); i++) {
            ForecastDay forecastDay = forecast.get(i);

            ForecastDayUi forecastDayUi = new ForecastDayUi(
                    forecastDay.getDate(),
                    forecastDay.getMinimumTemperatureInCelsius(),
                    forecastDay.getMaximumTemperatureInCelsius(),
                    forecastDay.getSkyCondition());

            forecastUi.add(forecastDayUi);
        }

        return forecastUi;
    }
}

