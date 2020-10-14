package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherOnlyDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.ForecastDay;
import com.example.weath.domain.models.SkyCondition;
import com.example.weath.domain.models.Weather2;

import java.util.ArrayList;
import java.util.List;

public class WeatherMapperImpl implements WeatherMapper {
    @Override
    public Weather2 toWeather(WeatherOnlyDto dto, City city) {
        SkyConditionDto skyConditionDto = dto.getSkyCondition();
        SkyCondition skyCondition = toSkyCondition(skyConditionDto);

        List<ForecastDay> forecastDays = toForecastDays(dto.getForecast());

        Weather2 weather = new Weather2(
                city.getName(),
                city.getLocation(),
                dto.getTemperatureInCelsius(),
                skyCondition,
                forecastDays);

        return weather;
    }

    @Override
    public CityWeatherDto toCityWeatherDto(WeatherWithForecast entity) {

        CityWeatherDto cityWeather = new CityWeatherDto(
                extractCityName(entity.weather.cityNameWithCountryCode),
                extractCountryCode(entity.weather.cityNameWithCountryCode),
                entity.weather.coordinate,
                entity.weather.temperatureInCelsius,
                toSkyConditionDto(entity.weather.skyCondition),
                toForecastDayDtoCollection(entity.forecast));

        return cityWeather;
    }

    @Override
    public CoordinateEntity toCoordinateEntity (Coordinate domain){
        CoordinateEntity entity = new CoordinateEntity();
        entity.latitude = domain.getLatitude();
        entity.longitude = domain.getLongitude();

        return entity;
    }


    private List<ForecastDay> toForecastDays(List<ForecastDayDto> collection) {
        List<ForecastDay> forecast = new ArrayList<>(7);

        for (int i = 0; i < collection.size(); i++) {
            ForecastDayDto forecastDayDto = collection.get(i);
            ForecastDay forecastDay = toForecastDay(forecastDayDto);

            forecast.add(forecastDay);
        }

        return forecast;
    }
    private ForecastDay toForecastDay(ForecastDayDto dto){
        return new ForecastDay(
                dto.getDate(),
                dto.getMinimumTemperatureInCelsius(),
                dto.getMaximumTemperatureInCelsius(),
                toSkyCondition(dto.getSkyCondition())
        );
    }

    private List<ForecastDayDto> toForecastDayDtoCollection(List<ForecastDayEntity> collection) {
        List<ForecastDayDto> forecast = new ArrayList<>(7);

        if (collection == null){
            return forecast;
        }

        for (int i = 0; i < collection.size(); i++) {
            ForecastDayEntity forecastDayDto = collection.get(i);
            ForecastDayDto forecastDay = toForecastDayDto(forecastDayDto);

            forecast.add(forecastDay);
        }

        return forecast;
    }
    private ForecastDayDto toForecastDayDto(ForecastDayEntity entity){
        return new ForecastDayDto(
                entity.date,
                entity.minimumTemperatureInCelsius,
                entity.maximumTemperatureInCelsius,
                toSkyConditionDto(entity.skyCondition)
        );
    }

    private SkyCondition toSkyCondition (SkyConditionDto dto){
        return SkyCondition.valueOf(dto.name());
    }
    private SkyConditionDto toSkyConditionDto(String skyCondition) {
        return SkyConditionDto.valueOf(skyCondition);
    }

    private String extractCityName (String cityNameWithCountryCode){
        int length = cityNameWithCountryCode.length();
        int endIndex = length - 5;
        String cityName = cityNameWithCountryCode.substring(0, endIndex);

        return cityName;
    }
    private String extractCountryCode (String cityNameWithCountryCode){
        int length = cityNameWithCountryCode.length();
        int startIndex = length - 4;
        String countryCode = cityNameWithCountryCode.substring(startIndex);

        return countryCode;
    }
}
