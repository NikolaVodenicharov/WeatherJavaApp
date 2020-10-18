package com.example.weath.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.models.Coordinate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class CurrentLocationHelper {
    private static final int REQUEST_LOCATION_CODE = 5;
    private static final String LOCATION_PERMISSION_GRANTED_MESSAGE = "Permission granted, you can get the weather of your current location.";
    private static final String NEED_LOCATION_PERMISSION_MESSAGE = "To get the weather of your current location we need location permission.";

    public static MutableLiveData<Coordinate> getLastKnownLocation(Context context) {
        boolean isPermissionGranted = checkCoarseLocationPermission(context);
        if (!isPermissionGranted) {
            return null;
        }

        MutableLiveData<Coordinate> currentLocation = new MutableLiveData<>();

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

                currentLocation.setValue(new Coordinate(latitude, longitude));
            }
        });

        return currentLocation;
    }

    public static void askForLocationPermissionAsync(Activity activity) {
        boolean isPermissionGranted = checkCoarseLocationPermission(activity);
        if (isPermissionGranted) {
            return;
        }

        if (shouldRationaleLocationPermission(activity)){
            alertDialogRationaleAsync(activity);
        }
        else{
            requestLocationPermissionAsync(activity);
        }
    }

    public static void onRequestPermissionsResult(DeviceConnectivity deviceConnectivity, Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean isAccessLocationTriggered = requestCode == REQUEST_LOCATION_CODE;
        if (!isAccessLocationTriggered){
            return;
        }

        boolean arePermissionsEmpty = permissions.length == 0;
        if (arePermissionsEmpty){
            return;
        }

        boolean areGrantResultsEmpty = grantResults.length == 0;
        if (areGrantResultsEmpty){
            return;
        }

        boolean isLocationPermissionGranted = checkCoarseLocationPermission(context);
        if (!isLocationPermissionGranted)
        {
            return;
        }

        deviceConnectivity.setLastKnownLocation(getLastKnownLocation(context));
        //App.lastKnownLocation = getLastKnownLocation(context);

        Toast.makeText(context, LOCATION_PERMISSION_GRANTED_MESSAGE, Toast.LENGTH_LONG).show();
    }

    private static boolean shouldRationaleLocationPermission(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    private static void alertDialogRationaleAsync(Activity activity) {
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
    private static void requestLocationPermissionAsync(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_CODE);
    }

    public static boolean checkCoarseLocationPermission(Context context){
        return checkPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    private static boolean checkPermissionGranted(Context context, String permission){
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        boolean isPermissionGranted = permissionResult == PackageManager.PERMISSION_GRANTED;
        return isPermissionGranted;
    }

}
