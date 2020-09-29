package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.CurrentWeatherDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.domain.models.CurrentWeather;
import com.example.weath.domain.models.ForecastDay;
import com.example.weath.domain.models.SkyCondition;
import com.example.weath.domain.models.Weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherMapperImpl implements WeatherMapper {
    private static WeatherMapperImpl instance;

    private WeatherMapperImpl(){

    }

    public static WeatherMapperImpl getInstance() {
        if (instance == null){
            instance = new WeatherMapperImpl();
        }

        return instance;
    }

    @Override
    public Weather mapToWeather(WeatherDto dto) {
        CurrentWeather currentWeather = toCurrentWeather(dto.getCurrentWeather());
        List<ForecastDay> forecast = toForecastDays(dto.getForecast());

        return new Weather(currentWeather, forecast);
    }

    private CurrentWeather toCurrentWeather(CurrentWeatherDto currentWeatherDto) {
        return new CurrentWeather(
                currentWeatherDto.getTemperature(),
                toSkyCondition(currentWeatherDto.getSkyCondition()),
                currentWeatherDto.getHumidity(),
                currentWeatherDto.getSunrise(),
                currentWeatherDto.getSunset());
    }

    private List<ForecastDay> toForecastDays(List<ForecastDayDto> forecastDayDtos) {
        List<ForecastDay> forecast = new ArrayList<>(7);

        for (int i = 0; i < forecastDayDtos.size(); i++) {
            ForecastDayDto forecastDayDto = forecastDayDtos.get(i);
            ForecastDay forecastDay = toForecastDay(forecastDayDto);

            forecast.add(forecastDay);
        }

        return forecast;
    }
    private ForecastDay toForecastDay(ForecastDayDto dto){
        return new ForecastDay(
                dto.getDate(),
                dto.getMinimumTemperature(),
                dto.getMaximumTemperature(),
                toSkyCondition(dto.getSkyCondition())
        );
    }
    private SkyCondition toSkyCondition (SkyConditionDto dto){
        return SkyCondition.valueOf(dto.name());
    }
}
