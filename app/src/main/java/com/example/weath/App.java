package com.example.weath;

import android.app.Application;
import android.net.ConnectivityManager;

import com.example.weath.data.OpenWeatherMapCities;
import com.example.weath.data.utils.RepositoryFactory;
import com.example.weath.domain.CitiesCollection;
import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.cases.WeatherCases;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static ConnectivityManager connectivityManager;
    //public static MutableLiveData<Coordinate> lastKnownLocation;

    public static CitiesCollection citiesCollection;
    public static DeviceConnectivity deviceConnectivity;

    public static WeatherCases weatherCases;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeConnectivityManager();

        initializeCitiesCollection();
        initializeDeviceConnectivity();

        weatherCases = new WeatherCases(
                RepositoryFactory.createRepository(this),
                citiesCollection,
                deviceConnectivity);
    }

    private void initializeConnectivityManager() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    private void initializeCitiesCollection() {
        List<InputStream> streams = new ArrayList<>(4);
        streams.add(getResources().openRawResource(R.raw.ad));
//        streams.add(getResources().openRawResource(R.raw.ek));
//        streams.add(getResources().openRawResource(R.raw.mr));
//        streams.add(getResources().openRawResource(R.raw.sz));

        citiesCollection = new OpenWeatherMapCities(streams);
    }

    private void initializeDeviceConnectivity() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        deviceConnectivity = new DeviceConnectivityImpl(connectivityManager);
    }
}
