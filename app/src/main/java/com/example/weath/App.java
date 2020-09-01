package com.example.weath;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.android.volley.toolbox.Volley;
import com.example.weath.domain.Repository;
import com.example.weath.domain.utils.CitiesCollection;
import com.example.weath.domain.utils.OpenWeatherMapCities;
import com.example.weath.data.RepositoryImpl;
import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.data.local.AppDatabase;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.local.DatabaseManager;
import com.example.weath.data.remote.OpenWeatherMapDataSource;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.remote.VolleyWebService;
import com.example.weath.data.remote.WebService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class App extends Application {
    public static CitiesCollection citiesCollection;
    public static Coordinate currentLocation;
    public static Repository Repository;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeCurrentLocation(this);
        initializeCitiesCollection();
        initializeRepository();
    }


    private void initializeCitiesCollection() {
        List<InputStream> streams = new ArrayList<>(4);
        streams.add(getResources().openRawResource(R.raw.ad));
        streams.add(getResources().openRawResource(R.raw.ek));
        streams.add(getResources().openRawResource(R.raw.mr));
        streams.add(getResources().openRawResource(R.raw.sz));

        citiesCollection = new OpenWeatherMapCities(streams);
    }

    private void initializeRepository() {
        WebService webService = new VolleyWebService(
                Volley.newRequestQueue(
                    getApplicationContext()));

        RemoteDataSource restService = new OpenWeatherMapDataSource(webService);

        LocalDataSource databaseManager = new DatabaseManager(
                AppDatabase.getInstance(getApplicationContext()),
                Executors.newFixedThreadPool(4)
        );

        Repository = new RepositoryImpl(restService, databaseManager);
    }


    @SuppressLint("MissingPermission")
    public static void initializeCurrentLocation(Context context) {
        boolean isPermissionGranted = checkPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (!isPermissionGranted) {
            return;
        }

        FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(context);

        fusedLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();

                if (location == null) {
                    return;
                }

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                currentLocation = new Coordinate(latitude, longitude);
            }
        });
    }

    public static boolean checkPermissionGranted(Context context, String permission){
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        boolean isPermissionGranted = permissionResult == PackageManager.PERMISSION_GRANTED;
        return isPermissionGranted;
    }
}
