package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.dataTransferObjects.WeatherOnlyDto;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Weather2;

public interface WeatherMapper {
    Weather2 toWeather(WeatherOnlyDto dto, City city);

    CityWeatherDto toCityWeather (WeatherWithForecast entity);
}
