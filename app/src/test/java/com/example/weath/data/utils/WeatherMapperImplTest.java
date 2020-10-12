package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather2;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class WeatherMapperImplTest {
    private static final double DELTA = 0.00001;

    @Test
    public void CanGetWeatherMapperInstance(){
        WeatherMapperImpl mapper = WeatherMapperImpl.getInstance();
    }

    @Test
    public void mapToWeather2_giveCorrectResult(){
        ForecastDayDto expectedDay = new ForecastDayDto(
                new Date(2000, 1, 24),
                18.5,
                25.4,
                SkyConditionDto.CLEAR);


        List<ForecastDayDto> expectedForecast = new ArrayList<>(1);
        expectedForecast.add(expectedDay);

        WeatherDto expectedWeather = new WeatherDto(23.4, SkyConditionDto.CLEAR, expectedForecast);
        City expectedDomainCity = new City("Boston", "(US)", new Coordinate(11.22, 33.44));

        WeatherMapperImpl mapper = WeatherMapperImpl.getInstance();
        Weather2 actual = mapper.mapToWeather(expectedWeather, expectedDomainCity);

        Assert.assertEquals(expectedWeather.getTemperatureInCelsius(),
                actual.getTemperatureInCelsius(),
                DELTA);

        Assert.assertEquals(expectedWeather.getSkyCondition().name(),
                actual.getSkyCondition().name());

        Assert.assertEquals(expectedDomainCity.getName(),
                actual.getCityName());

        Assert.assertEquals(expectedWeather.getForecast().get(0).getDate(),
                actual.getForecast().get(0).getDate());

        Assert.assertEquals(expectedWeather.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getForecast().get(0).getMaximumTemperatureInCelsius(),
                DELTA);

        Assert.assertEquals(expectedWeather.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getForecast().get(0).getMinimumTemperatureInCelsius(),
                DELTA);
    }
}
