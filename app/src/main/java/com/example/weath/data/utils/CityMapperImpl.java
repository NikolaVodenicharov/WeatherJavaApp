package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.domain.domainModels.City;
import com.example.weath.domain.domainModels.Coordinate;

public class CityMapperImpl implements CityMapper {
    private static CityMapperImpl instance;

    private CityMapperImpl(){}

    public static CityMapperImpl getInstance(){
        if (instance == null){
            instance = new CityMapperImpl();
        }

        return instance;
    }

    @Override
    public CityDto mapToCityDto(City city) {
        CityDto dto = new CityDto();
        dto.country = city.getCountry();
        dto.name = city.getName();
        dto.location = fromCoordinateToCoordinateEntity(city.getLocation());

        return dto;
    }

    @Override
    public City mapToCity(CityDto cityDto) {
        return new City(
                cityDto.name,
                cityDto.country,
                fromCoordinateEntityToCoordinate(cityDto.location));
    }

    private Coordinate fromCoordinateEntityToCoordinate (CoordinateEntity entity){
        return new Coordinate(entity.latitude, entity.longitude);
    }

    private CoordinateEntity fromCoordinateToCoordinateEntity (Coordinate coordinate){
        CoordinateEntity coordinateEntity = new CoordinateEntity();
        coordinateEntity.latitude = coordinate.getLatitude();
        coordinateEntity.longitude = coordinate.getLongitude();

        return coordinateEntity;
    }
}
