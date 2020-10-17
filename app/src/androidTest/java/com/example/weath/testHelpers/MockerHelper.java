package com.example.weath.testHelpers;

import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.ForecastDay;
import com.example.weath.domain.models.SkyCondition;
import com.example.weath.domain.models.Weather2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockerHelper {
    public static Weather2 mockWeather(){
        Weather2 weatherLocalDto = new Weather2(
                "Houston",
                new Date(),
                mockCoordinate(),
                25,
                SkyCondition.CLEAR,
                mockDomainForecastWithOneDay());

        return weatherLocalDto;
    }

    public static WeatherLocalDto mockCityWeatherDto(){
        WeatherLocalDto weatherLocalDto = new WeatherLocalDto(
                "Houston",
                "(US)",
                new Date(),
                internalMockCoordinateEntity(),
                25,
                SkyConditionDto.CLEAR,
                mockForecastWithOneDay());

        return weatherLocalDto;
    }
    public static WeatherLocalDto updateCityWeatherDto(WeatherLocalDto current) {
        WeatherLocalDto updated = new WeatherLocalDto(
                current.getCityName(),
                current.getCountryCode(),
                new Date(),
                current.getCoordinate(),
                current.getTemperatureInCelsius() + 5,
                createNewSkyCondition(current.getSkyCondition()),
                updateForecast(current.getForecast()));

        return updated;
    }

    public static WeatherRemoteDto mockWeatherOnlyDto() {
        return new WeatherRemoteDto(new Date(), 12.45, SkyConditionDto.CLEAR, mockForecastWithOneDay());
    }

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
    public static WeatherEntity updateWeatherEntity(WeatherEntity mockWeather) {
        WeatherEntity updatedMockWeather = new WeatherEntity();
        updatedMockWeather.cityNameWithCountryCode = mockWeather.cityNameWithCountryCode;
        updatedMockWeather.coordinate = mockWeather.coordinate;
        updatedMockWeather.temperatureInCelsius = mockWeather.temperatureInCelsius + 2;
        updatedMockWeather.skyCondition = SkyConditionDto.RAIN.name();
        updatedMockWeather.recordMoment = new Date();
        return updatedMockWeather;
    }

    public static ForecastDayEntity mockForecastDayEntity(WeatherEntity weather) {
        ForecastDayEntity forecastDay = new ForecastDayEntity();
        forecastDay.date = new Date();
        forecastDay.minimumTemperatureInCelsius = 20;
        forecastDay.maximumTemperatureInCelsius = 30;
        forecastDay.skyCondition = SkyConditionDto.SNOW.name();
        forecastDay.cityNameWithCountryCode = weather.cityNameWithCountryCode;

        return forecastDay;
    }
    public static ForecastDayEntity updateForecastDayEntity(ForecastDayEntity entity) {
        ForecastDayEntity forecastDay = new ForecastDayEntity();

        forecastDay.date = entity.date;
        forecastDay.minimumTemperatureInCelsius = entity.minimumTemperatureInCelsius + 2;
        forecastDay.maximumTemperatureInCelsius = entity.maximumTemperatureInCelsius + 3;
        forecastDay.cityNameWithCountryCode = entity.cityNameWithCountryCode;

        forecastDay.skyCondition = entity.skyCondition == SkyConditionDto.CLEAR.name() ?
                SkyConditionDto.RAIN.name() : SkyConditionDto.CLOUDS.name();


        return forecastDay;
    }

    public static List<ForecastDayEntity> mockForecastWithOneDay(WeatherEntity weather) {
        List<ForecastDayEntity> forecastDays = new ArrayList<>(5);
        ForecastDayEntity forecastDay = mockForecastDayEntity(weather);
        forecastDays.add(forecastDay);

        return forecastDays;
    }

    public static City mockCity() {
        return new City("Houston", "(US)", new Coordinate(11.22, 33.44));
    }

    public static CoordinateEntity mockCoordinateEntity() {
        CoordinateEntity coordinateEntity = new CoordinateEntity();
        coordinateEntity.latitude = -75.45;
        coordinateEntity.longitude = 88.15;

        return coordinateEntity;
    }
    public static Coordinate mockCoordinate(){
        Coordinate coordinate = new Coordinate(-75.45, 88.15);

        return coordinate;
    }

    private static CoordinateEntity internalMockCoordinateEntity() {
        CoordinateEntity coordinateEntity = new CoordinateEntity();
        coordinateEntity.latitude = 11.22;
        coordinateEntity.longitude = 33.44;

        return coordinateEntity;
    }
    private static List<ForecastDayDto> mockForecastWithOneDay(){
        List<ForecastDayDto> forecast = new ArrayList<>(1);
        forecast.add(mockForecastDayDto());

        return forecast;
    }
    private static ForecastDayDto mockForecastDayDto() {
        ForecastDayDto forecastDayDto = new ForecastDayDto(
                new Date(),
                20,
                30,
                SkyConditionDto.SNOW);

        return forecastDayDto;
    }

    private static List<ForecastDay> mockDomainForecastWithOneDay(){
        List<ForecastDay> forecast = new ArrayList<>(1);

        ForecastDay forecastDay = new ForecastDay(
                new Date(),
                20,
                30,
                SkyCondition.SNOW);

        forecast.add(forecastDay);

        return forecast;
    }

    private static List<ForecastDayDto> updateForecast(List<ForecastDayDto> current){
        List<ForecastDayDto> updated = new ArrayList<>(current.size());

        for (int i = 0; i < current.size(); i++) {
            ForecastDayDto currentDay = current.get(i);

            ForecastDayDto updatedDay = new ForecastDayDto(
                    currentDay.getDate(),
                    currentDay.getMinimumTemperatureInCelsius() + 3,
                    currentDay.getMaximumTemperatureInCelsius() + 4,
                    createNewSkyCondition(currentDay.getSkyCondition()));

            updated.add(updatedDay);
        }

        return updated;
    }
    private static SkyConditionDto createNewSkyCondition(SkyConditionDto current) {
        return current == SkyConditionDto.CLEAR ? SkyConditionDto.RAIN : SkyConditionDto.RAIN;
    }
}
