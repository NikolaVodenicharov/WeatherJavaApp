package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather2;

import java.util.List;

public interface WeatherMapper {
    Weather2 toWeather(WeatherLocalDto weatherLocalDto);
    Weather2 toWeather(WeatherRemoteDto dto, City city);

    WeatherLocalDto toWeatherLocalDto(WeatherWithForecast entity);
    WeatherLocalDto toWeatherLocalDto(WeatherRemoteDto weatherRemoteDto, City city);

    WeatherEntity toWeatherEntity(WeatherLocalDto weatherLocalDto);

    List<ForecastDayEntity> toForecastDayEntityCollection(WeatherLocalDto weather);

    CoordinateEntity toCoordinateEntity (Coordinate domain);

}
