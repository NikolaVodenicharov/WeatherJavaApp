package com.example.weath.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weath.R;
import com.example.weath.businessLogic.viewModels.StartViewModel;
import com.example.weath.databinding.ActivityStartBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StartActivity extends AppCompatActivity {
    public static final int WEATHER_FRAGMENT_POSITION = 1;

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
}