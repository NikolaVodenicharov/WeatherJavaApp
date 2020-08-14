package com.example.weath.data.local.dataTransferObjects;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.PrimaryKey;

import com.example.weath.data.local.entities.CoordinateEntity;

public class CityFullDto {
    public String name;
    public String country;

    @Embedded
    public CoordinateEntity location;
}
