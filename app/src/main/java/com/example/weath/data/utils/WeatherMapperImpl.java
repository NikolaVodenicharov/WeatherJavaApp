package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
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
    public Weather2 toWeather(WeatherLocalDto dto) {
        Weather2 weather2 = new Weather2(
                dto.getCityName(),
                dto.getRecordMoment(),
                toCoordinate(dto.getCoordinate()),
                dto.getTemperatureInCelsius(),
                toSkyCondition(dto.getSkyCondition()),
                toForecastDayCollection(dto.getForecast()));

        return weather2;
    }

    @Override
    public Weather2 toWeather(WeatherRemoteDto dto, City city) {
        SkyConditionDto skyConditionDto = dto.getSkyCondition();
        SkyCondition skyCondition = toSkyCondition(skyConditionDto);

        List<ForecastDay> forecastDays = toForecastDayCollection(dto.getForecast());

        Weather2 weather = new Weather2(
                city.getName(),
                dto.getRecordMoment(),
                city.getLocation(),
                dto.getTemperatureInCelsius(),
                skyCondition,
                forecastDays);

        return weather;
    }

    @Override
    public WeatherLocalDto toWeatherLocalDto(WeatherWithForecast entity) {
        WeatherLocalDto cityWeather = new WeatherLocalDto(
                extractCityName(entity.weather.cityNameWithCountryCode),
                extractCountryCode(entity.weather.cityNameWithCountryCode),
                entity.weather.recordMoment,
                entity.weather.coordinate,
                entity.weather.temperatureInCelsius,
                toSkyConditionDto(entity.weather.skyCondition),
                toForecastDayDtoCollection(entity.forecast));

        return cityWeather;
    }

    @Override
    public WeatherLocalDto toWeatherLocalDto(WeatherRemoteDto weatherRemoteDto, City city) {
        WeatherLocalDto cityWeather = new WeatherLocalDto(
                city.getName(),
                city.getCountry(),
                weatherRemoteDto.getRecordMoment(),
                toCoordinateEntity(city.getLocation()),
                weatherRemoteDto.getTemperatureInCelsius(),
                weatherRemoteDto.getSkyCondition(),
                weatherRemoteDto.getForecast());

        return cityWeather;
    }

    @Override
    public WeatherEntity toWeatherEntity(WeatherLocalDto weatherLocalDto) {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.recordMoment = weatherLocalDto.getRecordMoment();
        weatherEntity.skyCondition = weatherLocalDto.getSkyCondition().name();
        weatherEntity.temperatureInCelsius = weatherLocalDto.getTemperatureInCelsius();
        weatherEntity.cityNameWithCountryCode = createCityNameWithCountryCode(weatherLocalDto.getCityName(), weatherLocalDto.getCountryCode());
        weatherEntity.coordinate = weatherLocalDto.getCoordinate();

        return weatherEntity;
    }

    @Override
    public List<ForecastDayEntity> toForecastDayEntityCollection(WeatherLocalDto weather) {
        List<ForecastDayEntity> entities = new ArrayList<>(7);
        String cityNameWithCountryCode = createCityNameWithCountryCode(weather.getCityName(), weather.getCountryCode());

        List<ForecastDayDto> forecastDto = weather.getForecast();

        for (ForecastDayDto day : forecastDto){
            ForecastDayEntity entity = new ForecastDayEntity();
            entity.cityNameWithCountryCode = cityNameWithCountryCode;
            entity.skyCondition = day.getSkyCondition().name();
            entity.minimumTemperatureInCelsius = day.getMinimumTemperatureInCelsius();
            entity.maximumTemperatureInCelsius = day.getMaximumTemperatureInCelsius();
            entity.date = day.getDate();

            entities.add(entity);
        }

        return entities;
    }

    @Override
    public CoordinateEntity toCoordinateEntity (Coordinate domain){
        CoordinateEntity entity = new CoordinateEntity();
        entity.latitude = domain.getLatitude();
        entity.longitude = domain.getLongitude();

        return entity;
    }
    private Coordinate toCoordinate (CoordinateEntity entity){
        return new Coordinate(entity.latitude, entity.longitude);
    }

    private List<ForecastDay> toForecastDayCollection(List<ForecastDayDto> collection) {
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

    private String createCityNameWithCountryCode(String cityName, String countryCode) {
        return cityName + " " + countryCode;
    }
}
