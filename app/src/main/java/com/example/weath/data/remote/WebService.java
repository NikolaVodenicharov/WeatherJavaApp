package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;

public interface WebService {
    LiveData<JSONObject> getResponse(String url);
}
