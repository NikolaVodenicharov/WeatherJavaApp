package com.example.weath.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StartPagerAdapter extends FragmentStateAdapter {
    private static final int PAGE_NUM = 2;

    public StartPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == StartActivity.WEATHER_FRAGMENT_POSITION){
            return new WeatherFragment();
        }
        else{
            return new SearchFragment();
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_NUM;
    }
}
