package com.example.weath.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.data.utils.WeatherMapper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class DatabaseManager implements LocalDataSource {
    private ExecutorService executorService;
    private AppDatabase database;
    private WeatherMapper weatherMapper;

    public DatabaseManager(AppDatabase database, ExecutorService executorService, WeatherMapper weatherMapper){
        this.database = database;
        this.executorService = executorService;
        this.weatherMapper = weatherMapper;
    }

    @Override
    public void insertOrReplaceCityWeather(WeatherLocalDto weather) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                WeatherEntity weatherEntity = weatherMapper.toWeatherEntity(weather);
                database.weatherDao().insertOrReplaceWeather(weatherEntity);

                database.weatherDao().deleteForecastDays(weatherEntity.cityNameWithCountryCode);
                List<ForecastDayEntity> forecast = weatherMapper.toForecastDayEntityCollection(weather);
                database.weatherDao().insertOrReplaceForecast(forecast);
            }
        });
    }

    @Override
    public LiveData<WeatherLocalDto> getWeather(CoordinateEntity coordinate) {
        MutableLiveData<WeatherLocalDto> cityWeatherDto = new MutableLiveData<>();

        LiveData<WeatherWithForecast> weatherWithForecast = database.weatherDao().getWeather(coordinate.latitude, coordinate.longitude);

        weatherWithForecast.observeForever(new Observer<WeatherWithForecast>() {
            @Override
            public void onChanged(WeatherWithForecast entity) {
                if (entity == null){
                    return;
                }

                weatherWithForecast.removeObserver(this);

                WeatherLocalDto dto = weatherMapper.toWeatherLocalDto(entity);
                cityWeatherDto.setValue(dto);
            }
        });

        return cityWeatherDto;
    }

    @Override
    public LiveData<WeatherLocalDto> getLastCachedWeatherAsync() {
        MutableLiveData<WeatherLocalDto> result = new MutableLiveData<>();

        LiveData<WeatherWithForecast> weatherLiveData = database.weatherDao().getLastCachedWeather();

        weatherLiveData.observeForever(new Observer<WeatherWithForecast>() {
            @Override
            public void onChanged(WeatherWithForecast entity) {
                weatherLiveData.removeObserver(this);

                if (entity == null){
                    return;
                }

                WeatherLocalDto weatherLocalDto = weatherMapper.toWeatherLocalDto(entity);

                result.setValue(weatherLocalDto);
            }
        });

        return result;
    }

    @Override
    public LiveData<Boolean> isExistingAndUpToDate(CoordinateEntity coordinate, Date minimumUpToDate) {
        return database.weatherDao().isExistingAndUpToDate(coordinate.latitude, coordinate.longitude, minimumUpToDate.getTime());
    }
}
