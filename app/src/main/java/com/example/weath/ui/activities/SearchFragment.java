package com.example.weath.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weath.App;
import com.example.weath.R;
import com.example.weath.databinding.FragmentSearchBinding;
import com.example.weath.ui.viewModels.StartViewModel;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        StartViewModel viewModel = new ViewModelProvider(getActivity()).get(StartViewModel.class);
        binding.setViewModel(viewModel);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AutoCompleteTextView autocomplete = getView().findViewById(R.id.autoCompleteTextView);
        initializeAutoCompleteFromHashMap(autocomplete);
    }

    //Todo extract somewhere else ?
    private void initializeAutoCompleteFromHashMap(AutoCompleteTextView autocomplete) {
        String[] a = new String[2];
        String[] cities = App.citiesCollection.getCitiesNameAndCountryCode().toArray(a);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                cities
        );

        autocomplete.setAdapter(adapter);
        autocomplete.setThreshold(2);
    }
}