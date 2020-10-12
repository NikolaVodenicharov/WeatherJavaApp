package com.example.weath.data.utils;

import android.content.Context;

import com.android.volley.toolbox.Volley;
import com.example.weath.data.RepositoryImpl;
import com.example.weath.data.local.AppDatabase;
import com.example.weath.data.local.DatabaseManager;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.remote.OpenWeatherMapDataSource;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.remote.VolleyWebService;
import com.example.weath.data.remote.WebService;

import java.util.concurrent.Executors;

public class RepositoryFactory {

    public static RepositoryImpl createRepository(Context context) {
        WebService webService = new VolleyWebService(
                Volley.newRequestQueue(
                        context.getApplicationContext()));

        RemoteDataSource restService = new OpenWeatherMapDataSource(webService);

        LocalDataSource databaseManager = new DatabaseManager(
                AppDatabase.getInstance(context.getApplicationContext()),
                Executors.newFixedThreadPool(4)
        );

        return
            new RepositoryImpl(
                restService,
                databaseManager,
                WeatherMapperImpl.getInstance());
    }
}
