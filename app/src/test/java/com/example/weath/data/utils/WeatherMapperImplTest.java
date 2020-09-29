package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.CurrentWeatherDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.domain.models.Weather;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class WeatherMapperImplTest {

    @Test
    public void CanGetWeatherMapperInstance(){
        WeatherMapperImpl mapper = WeatherMapperImpl.getInstance();
    }

    @Test
    public void CorrectlyMapFromDtoToDomain (){
        CurrentWeatherDto expectedCurrentWeather = new CurrentWeatherDto(
                "28",
                SkyConditionDto.CLOUDS,
                "60%",
                new Date(2000, 1, 22),
                new Date(2000, 1, 23));

        ForecastDayDto expectedDay = new ForecastDayDto(
                new Date(2000, 1, 24),
                "20",
                "30",
                SkyConditionDto.CLEAR);


        List<ForecastDayDto> expectedForecast = new ArrayList<>(1);
        expectedForecast.add(expectedDay);

        WeatherDto expectedWeather = new WeatherDto(expectedCurrentWeather, expectedForecast);

        WeatherMapperImpl mapper = WeatherMapperImpl.getInstance();
        Weather actual = mapper.mapToWeather(expectedWeather);

        Assert.assertEquals(expectedCurrentWeather.getHumidity(), actual.getCurrentWeather().getHumidity());
        Assert.assertEquals(expectedCurrentWeather.getTemperature(), actual.getCurrentWeather().getTemperature());
        Assert.assertEquals(expectedCurrentWeather.getSunset(), actual.getCurrentWeather().getSunset());
        Assert.assertEquals(expectedCurrentWeather.getSunrise(), actual.getCurrentWeather().getSunrise());
        Assert.assertEquals(expectedCurrentWeather.getSkyCondition().name(), actual.getCurrentWeather().getSkyCondition().name());

        Assert.assertEquals(expectedDay.getMaximumTemperature(), actual.getForecast().get(0).getMaximumTemperature());
        Assert.assertEquals(expectedDay.getMinimumTemperature(), actual.getForecast().get(0).getMinimumTemperature());
        Assert.assertEquals(expectedDay.getDate(), actual.getForecast().get(0).getDate());
        Assert.assertEquals(expectedDay.getSkyCondition().name(), actual.getForecast().get(0).getSkyCondition().name());
    }
}
