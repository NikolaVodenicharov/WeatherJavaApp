package com.example.weath.data.local.dataAccessObjects;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.weath.data.local.entities.CityEntity;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CityEntity cityEntity);

    @Query("Select * From Cities Where latitude == :latitude And longitude == :longitude")
    CityEntity get (double latitude, double longitude);
}
