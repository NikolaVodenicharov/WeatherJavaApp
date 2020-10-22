package com.example.weath;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.models.Coordinate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DeviceConnectivityImpl implements DeviceConnectivity {
    private static final int REQUEST_LOCATION_CODE = 5;
    private static final String LOCATION_PERMISSION_GRANTED_MESSAGE = "Permission granted, you can get the weather of your current location.";
    private static final String NEED_LOCATION_PERMISSION_MESSAGE = "To get the weather of your current location we need location permission.";

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

    public void onRequestPermissionsResult(Context context){
        boolean isLocationPermissionGranted = isLocationPermissionGranted(context);
        if (!isLocationPermissionGranted)
        {
            return;
        }

        setLastKnownLocation(
                requestLastKnownLocation(context));

        Toast.makeText(context, LOCATION_PERMISSION_GRANTED_MESSAGE, Toast.LENGTH_LONG).show();
    }

    public void updateCurrentLocationAsync(Activity activity){
        if (isLocationPermissionGranted(activity)){
            LiveData<Coordinate> location = requestLastKnownLocation(activity);
            setLastKnownLocation(location);
        }
        else{
            askForLocationPermissionAsync(activity);
            //after requestLocation is invoked the result make callback to onRequestPermissionResult in the activity.
            //That activity method must made request to onRequestPermissionResult in current class.
        }
    }

    @SuppressLint("MissingPermission")
    private MutableLiveData<Coordinate> requestLastKnownLocation(Context context) {
        boolean isPermissionGranted = isLocationPermissionGranted(context);
        if (!isPermissionGranted) {
            return null;
        }

        MutableLiveData<Coordinate> currentLocation = new MutableLiveData<>();

        FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(context);

        fusedLocation
                .getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();

                        if (location == null) {
                            return;
                        }

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        currentLocation.setValue(new Coordinate(latitude, longitude));
                    }
                });

        return currentLocation;
    }
    private void setLastKnownLocation(LiveData<Coordinate> location) {
        location.observeForever(new Observer<Coordinate>() {
            @Override
            public void onChanged(Coordinate coordinate) {
                location.removeObserver(this);

                lastKnownLocation.setValue(coordinate);
            }
        });
    }

    private void askForLocationPermissionAsync(Activity activity) {
        if (shouldRationaleLocationPermission(activity)){
            alertDialogRationaleAsync(activity);
        }
        else{
            requestLocationPermissionAsync(activity);
        }
    }
    private boolean shouldRationaleLocationPermission(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    private void alertDialogRationaleAsync(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(NEED_LOCATION_PERMISSION_MESSAGE);

        builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestLocationPermissionAsync(activity);
            }
        });

        builder.show();
    }
    private void requestLocationPermissionAsync(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_CODE);
    }

    private boolean isLocationPermissionGranted(Context context){
        return checkPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    private boolean checkPermissionGranted(Context context, String permission){
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        boolean isPermissionGranted = permissionResult == PackageManager.PERMISSION_GRANTED;
        return isPermissionGranted;
    }
}
