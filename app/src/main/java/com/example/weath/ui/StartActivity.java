package com.example.weath.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weath.R;
import com.example.weath.businessLogic.viewModels.StartViewModel;
import com.example.weath.data.models.Coordinates;
import com.example.weath.databinding.ActivityStartBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StartActivity extends AppCompatActivity {
    public static final int WEATHER_FRAGMENT_POSITION = 1;

    private Coordinates currentLocation;
    private static final int REQUEST_LOCATION_CODE = 5;
    private static final String NEED_LOCATION_PERMISSION = "To get the coordinate we need access to your location.";
    private static final String LOCATION_PERMISSION_GRANTED = "Permission granted, you can get coordinate.";

    private ViewPager2 pager;
    private StartViewModel viewModel;
    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeBindings();
        initializePager();
        initializeTabLayoutMediator();
        displayWeatherOnSearchCityClicked();

        //setCurrentLocation();
    }

    private void initializePager() {
        FragmentStateAdapter adapter = new StartPagerAdapter(this);
        pager = findViewById(R.id.viewPager);
        pager.setAdapter(adapter);
    }

    private void initializeTabLayoutMediator() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == WEATHER_FRAGMENT_POSITION){
                    tab.setText("Weather");
                }
                else{
                    tab.setText("Search");
                }
            }
        });

        mediator.attach();
    }

    private void initializeBindings() {
        viewModel = new ViewModelProvider(this).get(StartViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        binding.setViewModel(viewModel);
    }

    private void displayWeatherOnSearchCityClicked(){
        viewModel.isSearchCityClicked.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isClicked) {
                if (isClicked){
                    pager.setCurrentItem(WEATHER_FRAGMENT_POSITION);
                }
            }
        });
    }


    // Current location
    @SuppressLint("MissingPermission")
    private void setCurrentLocation() {
        boolean isLocationPermissionGranted = checkPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (!isLocationPermissionGranted){
            askLocationPermission();
            return;
        }

        FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        fusedLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();

                if (location == null){
                    return;
                }

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                currentLocation = new Coordinates(
                        Double.toString(latitude),
                        Double.toString(longitude));
            }
        });
    }
    private void askLocationPermission() {
        if (shouldRationaleLocationPermission()){
            alertDialogRationale();
        }
        else{
            requestLocationPermission();
        }
    }
    private boolean shouldRationaleLocationPermission() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    private boolean checkPermissionGranted(Context context, String permission){
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        boolean isPermissionGranted = checkPermissionGranted(permissionResult);
        return isPermissionGranted;
    }
    private boolean checkPermissionGranted(int permissionResult){
        return permissionResult == PackageManager.PERMISSION_GRANTED;
    }
    private void alertDialogRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(NEED_LOCATION_PERMISSION);

        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestLocationPermission();
            }
        });

        builder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                StartActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isAccessLocationTriggered = requestCode == REQUEST_LOCATION_CODE;

        if (isAccessLocationTriggered){
            boolean arePermissionsEmpty = permissions.length == 0;
            if (arePermissionsEmpty){
                return;
            }

            boolean areGrantResultsEmpty = grantResults.length == 0;
            if (areGrantResultsEmpty){
                return;
            }

            boolean isLocationPermissionGranted = checkPermissionGranted(grantResults[0]);
            if (!isLocationPermissionGranted){
                if (!shouldRationaleLocationPermission()){

                    //ToDo should i remove the get location button if permissions is denied with don't ask me again ?
                    Toast.makeText(this, NEED_LOCATION_PERMISSION, Toast.LENGTH_LONG).show();
                }

                return;
            }

            Toast.makeText(this, LOCATION_PERMISSION_GRANTED, Toast.LENGTH_LONG).show();
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}