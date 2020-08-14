package com.example.weath.data.local.dataAccessObjects;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.weath.data.local.dataTransferObjects.CityFullDto;
import com.example.weath.data.local.entities.CityEntity;

import java.util.List;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CityEntity cityEntity);

    @Query("Select * From Cities Where latitude == :latitude And longitude == :longitude Limit 1")
    LiveData<CityFullDto> getFull(double latitude, double longitude);

    @Query("Select Exists (Select * From Cities Where latitude == :latitude And longitude == :longitude)")
    LiveData<Boolean> isExisting(double latitude, double longitude);

    @Query("Select * From Cities")
    LiveData<List<CityFullDto>> getAll();
}
