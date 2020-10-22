package com.example.weath.domain;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.models.Coordinate;

public interface DeviceConnectivity {
    boolean isConnectedToInternet();

    LiveData<Coordinate> getLastKnownLocation();
    void updateCurrentLocationAsync(Activity activity);

    void onRequestPermissionsResult(Context context);
}
