package com.example.weath;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.models.Coordinate;

public class DeviceConnectivityImpl implements DeviceConnectivity {
    private ConnectivityManager connectivityManager;
    private MutableLiveData<Coordinate> lastKnownLocation = new MutableLiveData<>();

    public DeviceConnectivityImpl(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public boolean isConnectedToInternet() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected =
                networkInfo != null &&
                        networkInfo.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public LiveData<Coordinate> getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(LiveData<Coordinate> location) {
        location.observeForever(new Observer<Coordinate>() {
            @Override
            public void onChanged(Coordinate coordinate) {
                location.removeObserver(this);

                lastKnownLocation.setValue(coordinate);
            }
        });
    }
}
