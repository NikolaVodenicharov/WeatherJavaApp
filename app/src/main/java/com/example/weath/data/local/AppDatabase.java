package com.example.weath.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.weath.data.local.dataAccessObjects.CityDao;
import com.example.weath.data.local.entities.CityEntity;

@Database(entities = {CityEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract CityDao cityDao();

    public static AppDatabase getInstance(Context appContext){
        if (instance == null){
            createInstance(appContext);
        }

        return instance;
    }

    private static void createInstance(Context appContext) {
        synchronized (AppDatabase.class) {
            if (instance == null) {

                instance = Room
                    .databaseBuilder(
                        appContext.getApplicationContext(),
                        AppDatabase.class,
                        "AppDatabase")
                    .build();
            }
        }
    }
}
