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
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.models.Coordinate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DeviceConnectivityImpl implements DeviceConnectivity {
    private static final int REQUEST_LOCATION_CODE = 5;
    private static final String LOCATION_PERMISSION_GRANTED_MESSAGE = "Permission granted, you can get the weather of your current location.";
    private static final String NEED_LOCATION_PERMISSION_MESSAGE = "To get the weather of your current location we need location permission.";

    private ConnectivityManager connectivityManager;

    private FusedLocationProviderClient fusedLocation;
    private MutableLiveData<Coordinate> currentLocation;
    private LocationCallback locationCallback;

    public DeviceConnectivityImpl(ConnectivityManager connectivityManager, Context context) {
        this.connectivityManager = connectivityManager;

        initializeFusedLocationProviderClient(context);
        initializeCurrentLocation(context);
        initializeLocationCallback();
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
        return currentLocation;
    }
    public void onRequestPermissionsResult(Context context){
        boolean isLocationPermissionGranted = isLocationPermissionGranted(context);
        if (!isLocationPermissionGranted)
        {
            return;
        }

        startLocationUpdates();

        Toast.makeText(context, LOCATION_PERMISSION_GRANTED_MESSAGE, Toast.LENGTH_LONG).show();
    }
    public void updateCurrentLocationAsync(Activity activity){
        if (isLocationPermissionGranted(activity)){
            startLocationUpdates();
        }
        else{
            askForLocationPermissionAsync(activity);
            //after requestLocation is invoked the result make callback to onRequestPermissionResult in the activity.
            //That activity method must made request to onRequestPermissionResult in current class.
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeCurrentLocation(Context context){
        currentLocation = new MutableLiveData<>();

        if (isLocationPermissionGranted(context)){
            fusedLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null){
                        Location location = task.getResult();

                        currentLocation.setValue(
                                new Coordinate(location.getLatitude(), location.getLongitude()));
                    }
                }
            });
        }
    }
    private void initializeFusedLocationProviderClient(Context context){
        fusedLocation = LocationServices.getFusedLocationProviderClient(context);
    }
    private void initializeLocationCallback(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                setCurrentLocationOnLocationResult(locationResult);
            }
        };
    }
    private void setCurrentLocationOnLocationResult(LocationResult locationResult){
        if (locationResult == null){
            return;
        }


        Location location = locationResult.getLastLocation();

        currentLocation.setValue(
                new Coordinate(location.getLatitude(), location.getLongitude()));

        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        fusedLocation.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                Looper.getMainLooper());
    }
    private void stopLocationUpdates() {
        if (locationCallback !=null){
            fusedLocation.removeLocationUpdates(locationCallback);
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        return locationRequest;
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
