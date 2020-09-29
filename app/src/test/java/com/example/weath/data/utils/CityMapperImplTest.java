package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;

import org.junit.Assert;
import org.junit.Test;

public class CityMapperImplTest {
    @Test
    public void CanCreateClass(){
        CityMapper mapper = CityMapperImpl.getInstance();
    }

    @Test
    public void CreatedMapperIsNotNull(){
        CityMapperImpl mapper = CityMapperImpl.getInstance();

        Assert.assertNotNull(mapper);
    }

    @Test
    public void CorrectlyMapFromDomainToDto(){
        City expected = new City("DummyCity", "DummyCountry", new Coordinate(11.22, 33.44));

        CityMapperImpl mapper = CityMapperImpl.getInstance();
        CityDto actual = mapper.mapToCityDto(expected);

        Assert.assertEquals(expected.getName(), actual.name);
        Assert.assertEquals(expected.getCountry(), actual.country);
        Assert.assertEquals(expected.getLocation().getLatitude(), actual.location.latitude);
        Assert.assertEquals(expected.getLocation().getLongitude(), actual.location.longitude);
    }

    @Test
    public void CorrectlyMapFromDtoToDomain(){
        CoordinateEntity coordinateEntity= new CoordinateEntity();
        coordinateEntity.latitude = 11.22;
        coordinateEntity.longitude = 33.44;

        CityDto expected = new CityDto();
        expected.name = "DummyCity";
        expected.country = "DummyCountry";
        expected.location = coordinateEntity;

        CityMapperImpl mapper = CityMapperImpl.getInstance();
        City actual = mapper.mapToCity(expected);

        Assert.assertEquals(expected.name, actual.getName());
        Assert.assertEquals(expected.country, actual.getCountry());
        Assert.assertEquals(expected.location.latitude, actual.getLocation().getLatitude());
        Assert.assertEquals(expected.location.longitude, actual.getLocation().getLongitude());
    }
}