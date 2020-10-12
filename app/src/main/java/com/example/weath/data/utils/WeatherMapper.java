package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Weather2;

public interface WeatherMapper {
    Weather2 mapToWeather (WeatherDto dto, City city);
}
