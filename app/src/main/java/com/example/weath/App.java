package com.example.weath;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.OpenWeatherMapCities;
import com.example.weath.data.utils.RepositoryFactory;
import com.example.weath.domain.CitiesCollection;
import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.Coordinate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static ConnectivityManager connectivityManager;
    public static MutableLiveData<Coordinate> lastKnownLocation;

    public static CitiesCollection citiesCollection;
    public static Repository repository;
    public static DeviceConnectivity deviceConnectivity;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeConnectivityManager();

        initializeCitiesCollection();
        initializeRepository();
        initializeDeviceConnectivity();
    }

    private void initializeConnectivityManager() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    public static boolean isConnectedToInternet() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected =
                networkInfo != null &&
                networkInfo.isConnectedOrConnecting();

        return isConnected;
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
        repository = RepositoryFactory.createRepository(this);
    }
    private void initializeDeviceConnectivity() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        deviceConnectivity = new DeviceConnectivityImpl(connectivityManager);
    }
}
