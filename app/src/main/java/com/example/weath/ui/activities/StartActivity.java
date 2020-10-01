package com.example.weath.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weath.App;
import com.example.weath.R;
import com.example.weath.databinding.ActivityStartBinding;
import com.example.weath.ui.utils.CurrentLocationHelper;
import com.example.weath.ui.utils.StartPagerAdapter;
import com.example.weath.ui.viewModels.StartViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StartActivity extends AppCompatActivity {
    public static final int WEATHER_FRAGMENT_POSITION = 1;

    private static final int REQUEST_LOCATION_CODE = 5;
    private static final String NEED_LOCATION_PERMISSION_MESSAGE = "To get the weather of your current location we need location permission.";
    private static final String LOCATION_PERMISSION_GRANTED_MESSAGE = "Permission granted, you can get the weather of your current location.";

    private ViewPager2 pager;
    private StartViewModel viewModel;
    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ToDo check for internet. If there is not....

        getLocationAsync();
        initializeBindings();
        initializePager();
        initializeTabLayoutMediator();
        displayWeatherOnSearchCityClicked();
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
        viewModel.getIsSearchWeatherCalled().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isClicked) {
                if (isClicked){
                    pager.setCurrentItem(WEATHER_FRAGMENT_POSITION);
                }
            }
        });
    }

    private void getLocationAsync(){
        if (App.lastKnownLocation != null &&
            App.lastKnownLocation.getValue() != null){
            return;
        }
        else if (CurrentLocationHelper.checkCoarseLocationPermission(this)){
            App.lastKnownLocation = CurrentLocationHelper.getLastKnownLocation(this);
        }
        else{
            // if location permission is granted that will trigger onRequestPermissionResult int this class
            CurrentLocationHelper.askForLocationPermissionAsync(this);
        }
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

            App.lastKnownLocation = CurrentLocationHelper.getLastKnownLocation(this);

            Toast.makeText(this, LOCATION_PERMISSION_GRANTED_MESSAGE, Toast.LENGTH_LONG).show();
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}