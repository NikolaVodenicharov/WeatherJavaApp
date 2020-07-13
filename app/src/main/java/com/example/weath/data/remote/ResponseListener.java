package com.example.weath.data.remote;

import org.json.JSONObject;

public interface ResponseListener {
    void onSuccess(JSONObject response);
    void onError(String message);
}
