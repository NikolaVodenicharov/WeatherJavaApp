package com.example.weath.data.local;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.weath.data.local.dataAccessObjects.CityDao;
import com.example.weath.data.local.entities.CityEntity;

@androidx.room.Database(entities = {CityEntity.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static volatile Database instance;

    public abstract CityDao cityDao();

    public static Database getInstance(Context context){
        if (instance == null){
            createInstance(context);
        }

        return instance;
    }

    private static void createInstance(Context context) {
        synchronized (Database.class) {
            if (instance == null) {

                instance = Room
                    .databaseBuilder(
                        context.getApplicationContext(),
                        Database.class,
                        "Database")
                    .build();
            }
        }
    }
}
