package com.example.weath.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.weath.App;
import com.example.weath.R;
import com.example.weath.businessLogic.viewModels.SearchCityViewModel;
import com.example.weath.databinding.ActivitySearchCityBinding;

public class SearchCityActivity extends AppCompatActivity {
    public static final String SEARCHED_CITY = "searchedCity";

    private ActivitySearchCityBinding binding;
    private SearchCityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeBindings();
        observeForDisplayWeather();

        initializeAutoCompleteFromHashMap();
    }

    private void initializeBindings() {
        viewModel = new ViewModelProvider(this).get(SearchCityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_city);
        binding.setViewModel(viewModel);
    }

    private void observeForDisplayWeather() {
        viewModel.isDisplayWeatherRequired.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Intent intent = new Intent(SearchCityActivity.this, DisplayWeatherActivity.class);
                    intent.putExtra(SEARCHED_CITY, viewModel.searchedCity);
                    startActivity(intent);
                }
            }
        });
    }

    private void initializeAutoCompleteFromHashMap() {
        String[] a = new String[2];
        String[] testCollection2 = App.cities.keySet().toArray(a);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                testCollection2
        );

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(2);
    }
}