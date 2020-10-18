package com.example.weath.domain;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.models.Coordinate;

public interface DeviceConnectivity {
    boolean isConnectedToInternet();

    LiveData<Coordinate> getLastKnownLocation();
    void setLastKnownLocation(LiveData<Coordinate> location);
}
