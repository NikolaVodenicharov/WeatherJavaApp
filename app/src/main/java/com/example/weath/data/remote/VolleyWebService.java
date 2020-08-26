package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class VolleyWebService implements WebService {
    private RequestQueue queue;

    public VolleyWebService(RequestQueue queue){
        this.queue = queue;
    }

    @Override
    public LiveData<JSONObject> getResponse(String url) {
        MutableLiveData<JSONObject> result = new MutableLiveData<>();

        JsonObjectRequest request = createRequest(url, result);
        queue.add(request);

        return result;
    }

    private JsonObjectRequest createRequest(String url, final MutableLiveData<JSONObject> result){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result.setValue(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        result.setValue(null);
                    }
                }
        );

        return request;
    }


}
