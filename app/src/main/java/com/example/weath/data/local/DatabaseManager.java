package com.example.weath.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.data.utils.WeatherMapper;

import java.util.ArrayList;
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
    public void insertOrReplaceCityWeather(CityWeatherDto cityWeather) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                WeatherEntity weatherEntity = createWeatherEntity(cityWeather);
                database.weatherDao().insertOrReplaceWeather(weatherEntity);

                List<ForecastDayEntity> forecast = createForecast(cityWeather);
                database.weatherDao().insertOrReplaceForecast(forecast);
            }
        });
    }

    @Override
    public LiveData<CityWeatherDto> getCityWeather(CoordinateEntity coordinate) {
        MutableLiveData<CityWeatherDto> cityWeatherDto = new MutableLiveData<>();

        LiveData<WeatherWithForecast> weatherWithForecast = database.weatherDao().getWeather(coordinate.latitude, coordinate.longitude);

        weatherWithForecast.observeForever(new Observer<WeatherWithForecast>() {
            @Override
            public void onChanged(WeatherWithForecast entity) {
                weatherWithForecast.removeObserver(this);

                if (entity == null){
                    return;
                }

                CityWeatherDto dto = weatherMapper.toCityWeatherDto(entity);
                cityWeatherDto.setValue(dto);
            }
        });

        return cityWeatherDto;
    }

    @Override
    public LiveData<Boolean> isExistingAndUpToDate(CoordinateEntity coordinate, Date oldestMoment) {
        return database.weatherDao().isExistingAndUpToDate(coordinate.latitude, coordinate.longitude, oldestMoment.getTime());
    }

    private WeatherEntity createWeatherEntity(CityWeatherDto cityWeather) {
        CoordinateEntity coordinateEntity = new CoordinateEntity();
        coordinateEntity.longitude = cityWeather.getCoordinate().longitude;
        coordinateEntity.latitude = cityWeather.getCoordinate().latitude;

        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.recordMoment = new Date();
        weatherEntity.skyCondition = cityWeather.getSkyCondition().name();
        weatherEntity.temperatureInCelsius = cityWeather.getTemperatureInCelsius();
        weatherEntity.cityNameWithCountryCode = createCityNameWithCountryCode(cityWeather.getCityName(), cityWeather.getCountryCode());
        weatherEntity.coordinate = coordinateEntity;

        return weatherEntity;
    }

    private String createCityNameWithCountryCode(String cityName, String countryCode) {
        return cityName + " " + countryCode;
    }

    private List<ForecastDayEntity> createForecast(CityWeatherDto cityWeather){
        List<ForecastDayEntity> forecast = new ArrayList<>(7);
        String cityNameWithCountryCode = createCityNameWithCountryCode(cityWeather.getCityName(), cityWeather.getCountryCode());

        List<ForecastDayDto> forecastDto = cityWeather.getForecast();

        for (ForecastDayDto day : forecastDto){
            ForecastDayEntity entity = new ForecastDayEntity();
            entity.cityNameWithCountryCode = cityNameWithCountryCode;
            entity.skyCondition = day.getSkyCondition().name();
            entity.minimumTemperatureInCelsius = day.getMinimumTemperatureInCelsius();
            entity.maximumTemperatureInCelsius = day.getMaximumTemperatureInCelsius();
            entity.date = day.getDate();

            forecast.add(entity);
        }

        return forecast;
    }
}
