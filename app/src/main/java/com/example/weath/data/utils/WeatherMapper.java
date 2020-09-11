package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.domain.domainModels.Weather;

public interface WeatherMapper {
    Weather mapToWeather (WeatherDto dto);
}
