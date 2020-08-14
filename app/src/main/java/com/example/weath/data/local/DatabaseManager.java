package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.domainModels.Coordinate;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;

import java.util.List;

public interface DatabaseManager {
    void insertCity(CityFullDto city);
    LiveData<CityFullDto> getCityFull(Coordinate coordinate);
    LiveData<Boolean> isExisting(Coordinate coordinate);

    LiveData<List<CityFullDto>> getAll();
}