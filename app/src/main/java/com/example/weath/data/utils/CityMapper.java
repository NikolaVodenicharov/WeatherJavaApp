package com.example.weath.data.utils;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.domain.domainModels.City;

public interface CityMapper {
    CityDto mapToCityDto (City city);

    City mapToCity(CityDto cityDto);
}
