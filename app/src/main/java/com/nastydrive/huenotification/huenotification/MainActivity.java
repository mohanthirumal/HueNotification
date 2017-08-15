package com.nastydrive.huenotification.huenotification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());
        makeRequest();
    }

    protected void makeRequest() {
        AndroidNetworking.get("https://www.meethue.com/api/nupnp")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("+++++++++++++++++ " + response.toString() + "++++++++");
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println("+++++++++++++++++ " + error.toString() + "++++++++");
                    }
                });
    }
}
