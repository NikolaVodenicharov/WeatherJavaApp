package com.example.weath.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cities")
public class CityEntity {
    @NonNull
    public String name;

    @NonNull
    public String country;

    @Embedded
    @PrimaryKey
    @NonNull
    public CoordinateEntity location;
}
