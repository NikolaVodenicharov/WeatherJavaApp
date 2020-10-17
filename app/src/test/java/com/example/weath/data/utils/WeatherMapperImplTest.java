package com.example.weath.data.utils;

import com.example.weath.Constants;
import com.example.weath.Mockers;
import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.ForecastDay;
import com.example.weath.domain.models.Weather2;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherMapperImplTest {
    @Test
    public void CanGetWeatherMapperInstance(){
        WeatherMapperImpl mapper = new WeatherMapperImpl();
    }

    @Test
    public void toWeather_fromWeatherRemoteDtoAndCity_giveCorrectResult(){
        ForecastDayDto expectedDay = Mockers.mockForecastDayDto();

        List<ForecastDayDto> expectedForecast = new ArrayList<>(1);
        expectedForecast.add(expectedDay);

        WeatherRemoteDto expectedWeather = new WeatherRemoteDto(new Date(), 23.4, SkyConditionDto.CLEAR, expectedForecast);
        City expectedDomainCity = Mockers.mockCity();

        WeatherMapperImpl mapper = new WeatherMapperImpl();
        Weather2 actual = mapper.toWeather(expectedWeather, expectedDomainCity);

        Assert.assertEquals(expectedDomainCity.getName(),
                actual.getCityName());

        Assert.assertEquals(expectedWeather.getRecordMoment(),
                actual.getRecordMoment());

        Assert.assertEquals(expectedDomainCity.getLocation().getLatitude(),
                actual.getCoordinate().getLatitude());

        Assert.assertEquals(expectedDomainCity.getLocation().getLongitude(),
                actual.getCoordinate().getLongitude());

        Assert.assertEquals(expectedWeather.getTemperatureInCelsius(),
                actual.getTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expectedWeather.getSkyCondition().name(),
                actual.getSkyCondition().name());

        forecastDayAssertEquals(expectedWeather.getForecast().get(0),
                actual.getForecast().get(0));
    }

    @Test
    public void toWeather_fromWeatherLocalDto_giveCorrectResult(){
        WeatherMapper mapper = new WeatherMapperImpl();

        WeatherLocalDto weatherLocalDto = Mockers.mockCityWeatherDto();

        Weather2 weather2 = mapper.toWeather(weatherLocalDto);

        Assert.assertEquals(weatherLocalDto.getCityName(), weather2.getCityName());

        Assert.assertEquals(weatherLocalDto.getRecordMoment(), weather2.getRecordMoment());

        Assert.assertEquals(weatherLocalDto.getCoordinate().longitude, weather2.getCoordinate().getLongitude());
        Assert.assertEquals(weatherLocalDto.getCoordinate().latitude, weather2.getCoordinate().getLatitude());

        Assert.assertEquals(weatherLocalDto.getSkyCondition().name(), weather2.getSkyCondition().name());
        Assert.assertEquals(weatherLocalDto.getTemperatureInCelsius(), weather2.getTemperatureInCelsius(), Constants.DELTA);

        forecastDayAssertEquals(weatherLocalDto.getForecast().get(0), weather2.getForecast().get(0));
    }

    @Test
    public void toWeatherLocalDto_fromWeatherRemoteDtoAndCity_giveCorrectResult(){
        WeatherMapper mapper = new WeatherMapperImpl();

        WeatherRemoteDto weatherRemoteDto = Mockers.mockWeatherRemoteDto();
        City city = Mockers.mockCity();

        WeatherLocalDto actualWeatherLocalDto = mapper.toWeatherLocalDto(weatherRemoteDto, city);

        Assert.assertEquals(weatherRemoteDto.getTemperatureInCelsius(), actualWeatherLocalDto.getTemperatureInCelsius(), Constants.DELTA);
        Assert.assertEquals(weatherRemoteDto.getSkyCondition(), actualWeatherLocalDto.getSkyCondition());

        Assert.assertEquals(city.getLocation().getLatitude(), actualWeatherLocalDto.getCoordinate().latitude);
        Assert.assertEquals(city.getLocation().getLongitude(), actualWeatherLocalDto.getCoordinate().longitude);
        Assert.assertEquals(city.getName(), actualWeatherLocalDto.getCityName());
        Assert.assertEquals(city.getCountry(), actualWeatherLocalDto.getCountryCode());

        forecastDayAssertEquals(weatherRemoteDto.getForecast().get(0), actualWeatherLocalDto.getForecast().get(0));
    }

    @Test
    public void toWeatherLocalDto_fromWeatherWithForecast_giveCorrectResult(){
        WeatherEntity weather = Mockers.mockWeatherEntity();
        List<ForecastDayEntity> forecast = Mockers.mockForecastDayEntityCollectionWithOneDay(weather.cityNameWithCountryCode);

        WeatherWithForecast expected = new WeatherWithForecast();
        expected.weather = weather;
        expected.forecast = forecast;

        WeatherMapperImpl mapper = new WeatherMapperImpl();

        WeatherLocalDto actual = mapper.toWeatherLocalDto(expected);

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

    @Test
    public void toWeatherLocalDto_fromWeatherWithForecast_notThrowingException_whenForecastIsEmpty(){
        WeatherEntity weather = Mockers.mockWeatherEntity();

        WeatherWithForecast expected = new WeatherWithForecast();
        expected.weather = weather;
        expected.forecast = new ArrayList<>();

        WeatherMapperImpl mapper = new WeatherMapperImpl();

        WeatherLocalDto actual = mapper.toWeatherLocalDto(expected);

        Assert.assertEquals(expected.weather.skyCondition,
                actual.getSkyCondition().name());

        Assert.assertEquals(expected.weather.temperatureInCelsius,
                actual.getTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals("Houston", actual.getCityName());

        Assert.assertEquals("(US)", actual.getCountryCode());
    }

    @Test
    public void toWeatherLocalDto_fromWeatherWithForecast_notThrowingException_whenForecastIsNull(){
        WeatherEntity weather = Mockers.mockWeatherEntity();

        WeatherWithForecast expected = new WeatherWithForecast();
        expected.weather = weather;
        expected.forecast = null;

        WeatherMapperImpl mapper = new WeatherMapperImpl();

        WeatherLocalDto actual = mapper.toWeatherLocalDto(expected);

        Assert.assertEquals(expected.weather.skyCondition,
                actual.getSkyCondition().name());

        Assert.assertEquals(expected.weather.temperatureInCelsius,
                actual.getTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals("Houston", actual.getCityName());

        Assert.assertEquals("(US)", actual.getCountryCode());
    }

    @Test
    public void toWeatherEntity_fromWeatherLocalDto_giveCorrectResult(){
        WeatherMapper mapper = new WeatherMapperImpl();

        WeatherLocalDto expectedDto = Mockers.mockCityWeatherDto();

        WeatherEntity actualEntity = mapper.toWeatherEntity(expectedDto);

        Assert.assertEquals(expectedDto.getCityName() + " " + expectedDto.getCountryCode(),
                actualEntity.cityNameWithCountryCode);

        Assert.assertEquals(expectedDto.getRecordMoment().getTime(), actualEntity.recordMoment.getTime());

        Assert.assertEquals(expectedDto.getCoordinate().longitude, actualEntity.coordinate.longitude);
        Assert.assertEquals(expectedDto.getCoordinate().latitude, actualEntity.coordinate.latitude);

        Assert.assertEquals(expectedDto.getSkyCondition().name(), actualEntity.skyCondition);
        Assert.assertEquals(expectedDto.getTemperatureInCelsius(), actualEntity.temperatureInCelsius, Constants.DELTA);
    }

    @Test
    public void toForecastDayEntityCollection_fromForecastDayDto_giveCorrectResult(){
        WeatherLocalDto weatherLocalDto = Mockers.mockCityWeatherDto();

        WeatherMapper mapper = new WeatherMapperImpl();

        List<ForecastDayEntity> actualEntity = mapper.toForecastDayEntityCollection(weatherLocalDto);

        for (int i = 0; i < weatherLocalDto.getForecast().size(); i++) {
            forecastDayAssertEquals(weatherLocalDto.getForecast().get(i), actualEntity.get(i));
        }
    }

    @Test
    public void toCoordinateEntity_giveCorrectResult(){
        Coordinate domain = Mockers.mockCoordinate();

        WeatherMapperImpl mapper = new WeatherMapperImpl();

        CoordinateEntity entity = mapper.toCoordinateEntity(domain);

        Assert.assertEquals(domain.getLatitude(), entity.latitude, Constants.DELTA);
        Assert.assertEquals(domain.getLongitude(), entity.longitude, Constants.DELTA);
    }

    private void forecastDayAssertEquals(ForecastDayDto dto, ForecastDay domain) {
        Assert.assertEquals(dto.getDate(),
                domain.getDate());

        Assert.assertEquals(dto.getMinimumTemperatureInCelsius(),
                domain.getMinimumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(dto.getMaximumTemperatureInCelsius(),
                domain.getMaximumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(dto.getSkyCondition().name(),
                domain.getSkyCondition().name());
    }
    private void forecastDayAssertEquals(ForecastDayDto dto, ForecastDayEntity entity) {
        Assert.assertEquals(dto.getDate(),
                entity.date);

        Assert.assertEquals(dto.getMinimumTemperatureInCelsius(),
                entity.minimumTemperatureInCelsius,
                Constants.DELTA);

        Assert.assertEquals(dto.getMaximumTemperatureInCelsius(),
                entity.maximumTemperatureInCelsius,
                Constants.DELTA);

        Assert.assertEquals(dto.getSkyCondition().name(),
                entity.skyCondition);
    }
    private void forecastDayAssertEquals(ForecastDayDto expected, ForecastDayDto actual) {
        Assert.assertEquals(expected.getDate(),
                actual.getDate());

        Assert.assertEquals(expected.getMinimumTemperatureInCelsius(),
                actual.getMinimumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expected.getMaximumTemperatureInCelsius(),
                actual.getMaximumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expected.getSkyCondition().name(),
                actual.getSkyCondition().name());
    }
}
