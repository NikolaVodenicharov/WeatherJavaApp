package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.domain.domainModels.Coordinate;

import java.util.List;

public interface LocalDataSource {
    void insertCity(CityDto city);
    LiveData<CityDto> getCityFull(Coordinate coordinate);
    LiveData<Boolean> isExisting(Coordinate coordinate);

    LiveData<List<CityDto>> getAll();
}