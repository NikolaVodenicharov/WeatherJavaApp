package com.example.weath;

import android.app.Application;

import com.example.weath.data.Repository;

public class App extends Application {
    private static Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeRepository();
    }

    private void initializeRepository() {
        repository = Repository.getInstance(getApplicationContext());
    }
}
