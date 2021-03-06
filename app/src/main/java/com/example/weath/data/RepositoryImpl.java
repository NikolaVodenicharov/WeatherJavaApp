package com.example.weath.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.utils.WeatherMapper;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Weather;

import java.util.Date;

public class RepositoryImpl implements Repository {
    private final RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;
    private WeatherMapper weatherMapper;

    public RepositoryImpl(RemoteDataSource remoteDataSource,
                          LocalDataSource localDataSource,
                          WeatherMapper weatherMapper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.weatherMapper = weatherMapper;
    }


    @Override
    public LiveData<Weather> getWeatherAsync(City city, Date minimumUpToDate) {
        CoordinateEntity coordinate = weatherMapper.toCoordinateEntity(city.getLocation());

        refreshWeather(city, minimumUpToDate, coordinate);

        return loadWeatherFromDatabase(coordinate, minimumUpToDate);
    }

    @Override
    public LiveData<Weather> getWeatherCacheAsync(City city) {
        CoordinateEntity coordinate = weatherMapper.toCoordinateEntity(city.getLocation());

        return loadWeatherFromDatabase(coordinate);
    }

    @Override
    public LiveData<Weather> getLastCachedWeatherAsync() {
        MutableLiveData<Weather> weatherLiveData = new MutableLiveData<>();

        LiveData<WeatherLocalDto> dto = localDataSource.getLastCachedWeatherAsync();

        dto.observeForever(new Observer<WeatherLocalDto>() {
            @Override
            public void onChanged(WeatherLocalDto weatherLocalDto) {
                dto.removeObserver(this);

                Weather weather = weatherMapper.toWeather(weatherLocalDto);

                weatherLiveData.setValue(weather);
            }
        });

        return weatherLiveData;
    }

    private void refreshWeather(City city, Date minimumUpToDate, CoordinateEntity coordinate) {
        LiveData<Boolean> isExisting = localDataSource.isExistingAndUpToDate(coordinate, minimumUpToDate);

        isExisting.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExistAndUpToDate) {
                isExisting.removeObserver(this);

                if (isExistAndUpToDate){
                    return;
                }

                final LiveData<WeatherRemoteDto> dto = remoteDataSource.getWeatherAsync(coordinate);

                dto.observeForever(new Observer<WeatherRemoteDto>() {
                    @Override
                    public void onChanged(WeatherRemoteDto weatherRemoteDto) {
                        dto.removeObserver(this);

                        WeatherLocalDto weatherLocalDto = weatherMapper.toWeatherLocalDto(weatherRemoteDto, city);
                        localDataSource.insertOrReplaceCityWeather(weatherLocalDto);
                    }
                });
            }
        });
    }
    private LiveData<Weather> loadWeatherFromDatabase(CoordinateEntity coordinate, Date minimumUpToDate) {
        MutableLiveData<Weather> weatherResult = new MutableLiveData<>();

        LiveData<WeatherLocalDto> cityWeather = localDataSource.getWeather(coordinate);

        cityWeather.observeForever(new Observer<WeatherLocalDto>() {
            @Override
            public void onChanged(WeatherLocalDto weatherLocalDto) {
                if (weatherLocalDto == null || weatherLocalDto.getRecordMoment().getTime() < minimumUpToDate.getTime()){
                    return;
                }

                cityWeather.removeObserver(this);

                Weather weather = weatherMapper.toWeather(weatherLocalDto);
                weatherResult.setValue(weather);
            }
        });

        return weatherResult;
    }

    private LiveData<Weather> loadWeatherFromDatabase(CoordinateEntity coordinate) {
        MutableLiveData<Weather> weatherResult = new MutableLiveData<>();

        LiveData<WeatherLocalDto> cityWeather = localDataSource.getWeather(coordinate);

        cityWeather.observeForever(new Observer<WeatherLocalDto>() {
            @Override
            public void onChanged(WeatherLocalDto weatherLocalDto) {
                if (weatherLocalDto == null){
                    return;
                }

                cityWeather.removeObserver(this);

                Weather weather = weatherMapper.toWeather(weatherLocalDto);
                weatherResult.setValue(weather);
            }
        });

        return weatherResult;
    }
}