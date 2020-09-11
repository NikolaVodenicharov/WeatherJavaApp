package com.example.weath.data.dataTransferObjects;

import androidx.room.Embedded;

import com.example.weath.data.local.entities.CoordinateEntity;

public class CityDto {
    public String name;
    public String country;

    @Embedded
    public CoordinateEntity location;
}
