package com.example.weath;

import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mockers {
    public static WeatherEntity mockWeatherEntity() {
        CoordinateEntity coordinate = new CoordinateEntity();
        coordinate.latitude = 11.22;
        coordinate.longitude = 33.44;

        WeatherEntity weather = new WeatherEntity();
        weather.cityNameWithCountryCode = "Houston (US)";
        weather.recordMoment = new Date();
        weather.skyCondition = SkyConditionDto.CLOUDS.name();
        weather.temperatureInCelsius = 25;
        weather.coordinate = coordinate;

        return weather;
    }

    public static List<ForecastDayEntity> mockForecastWithOneDay(String cityNameWithCountryCode) {
        List<ForecastDayEntity> forecastDays = new ArrayList<>(5);
        ForecastDayEntity forecastDay = mockForecastDayEntity(cityNameWithCountryCode);
        forecastDays.add(forecastDay);

        return forecastDays;
    }
    private static ForecastDayEntity mockForecastDayEntity(String cityNameWithCountryCode) {
        ForecastDayEntity forecastDay = new ForecastDayEntity();
        forecastDay.date = new Date();
        forecastDay.minimumTemperatureInCelsius = 20;
        forecastDay.maximumTemperatureInCelsius = 30;
        forecastDay.skyCondition = SkyConditionDto.SNOW.name();
        forecastDay.cityNameWithCountryCode = cityNameWithCountryCode;

        return forecastDay;
    }

    public static ForecastDayDto mockForecastDayDto(){
        ForecastDayDto forecastDay = new ForecastDayDto(
                new Date(2000, 1, 24),
                18.5,
                25.4,
                SkyConditionDto.CLEAR);

        return forecastDay;
    }

    public static City mockCity() {
        return new City("Boston", "(US)", new Coordinate(11.22, 33.44));
    }
}
