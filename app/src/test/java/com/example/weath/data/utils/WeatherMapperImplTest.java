package com.example.weath.data.utils;

import com.example.weath.Constants;
import com.example.weath.Mockers;
import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherOnlyDto;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Weather2;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WeatherMapperImplTest {
    @Test
    public void CanGetWeatherMapperInstance(){
        WeatherMapperImpl mapper = WeatherMapperImpl.getInstance();
    }

    @Test
    public void mapToWeather2_giveCorrectResult(){
        ForecastDayDto expectedDay = Mockers.mockForecastDayDto();


        List<ForecastDayDto> expectedForecast = new ArrayList<>(1);
        expectedForecast.add(expectedDay);

        WeatherOnlyDto expectedWeather = new WeatherOnlyDto(23.4, SkyConditionDto.CLEAR, expectedForecast);
        City expectedDomainCity = Mockers.mockCity();

        WeatherMapperImpl mapper = WeatherMapperImpl.getInstance();
        Weather2 actual = mapper.toWeather(expectedWeather, expectedDomainCity);

        Assert.assertEquals(expectedWeather.getTemperatureInCelsius(),
                actual.getTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expectedWeather.getSkyCondition().name(),
                actual.getSkyCondition().name());

        Assert.assertEquals(expectedDomainCity.getName(),
                actual.getCityName());

        Assert.assertEquals(expectedWeather.getForecast().get(0).getDate(),
                actual.getForecast().get(0).getDate());

        Assert.assertEquals(expectedWeather.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getForecast().get(0).getMaximumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expectedWeather.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getForecast().get(0).getMinimumTemperatureInCelsius(),
                Constants.DELTA);
    }



    @Test
    public void toCityWeather_giveCorrectResult(){
        WeatherEntity weather = Mockers.mockWeatherEntity();
        List<ForecastDayEntity> forecast = Mockers.mockForecastWithOneDay(weather.cityNameWithCountryCode);

        WeatherWithForecast expected = new WeatherWithForecast();
        expected.weather = weather;
        expected.forecast = forecast;

        WeatherMapperImpl mapper = WeatherMapperImpl.getInstance();

        CityWeatherDto actual = mapper.toCityWeather(expected);

        Assert.assertEquals(expected.weather.skyCondition,
                actual.getSkyCondition().name());

        Assert.assertEquals(expected.weather.temperatureInCelsius,
                actual.getTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals("Houston", actual.getCityName());

        Assert.assertEquals("(US)", actual.getCountryCode());

        Assert.assertEquals(expected.forecast.get(0).skyCondition,
                actual.getForecast().get(0).getSkyCondition().name());

        Assert.assertEquals(expected.forecast.get(0).minimumTemperatureInCelsius,
                actual.getForecast().get(0).getMinimumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expected.forecast.get(0).maximumTemperatureInCelsius,
                actual.getForecast().get(0).getMaximumTemperatureInCelsius(),
                Constants.DELTA);
    }
}
