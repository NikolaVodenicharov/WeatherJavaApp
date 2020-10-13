package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.local.entities.CoordinateEntity;

import java.util.concurrent.ExecutorService;

public class DatabaseManager implements LocalDataSource {
    private ExecutorService executorService;
    private AppDatabase database;

    public DatabaseManager(AppDatabase database, ExecutorService executorService){
        this.database = database;
        this.executorService = executorService;
    }

    @Override
    public void insertCityWeather(CityWeatherDto cityWeather) {

    }

    @Override
    public void updateCityWeather(CityWeatherDto cityWeather) {

    }

    @Override
    public LiveData<CityWeatherDto> getCityWeather(CoordinateEntity coordinate) {
        return null;
    }

    @Override
    public LiveData<CityWeatherDto> getCityWeather(String cityName, String countryCode) {
        return null;
    }

    private double trimTwoDigitsPrecision(double number){
        int temp = (int) (number * 100);    // 123.45678 will convert to 12345
        double trimmed = (double) (temp / 100d);    // 12345 will go to 123.45

        return trimmed;
    }

}
