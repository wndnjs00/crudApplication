package com.example.crudapplication;

import android.app.Application;
import android.os.Build;

import com.example.crudapplication.data.api.RetrofitService;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class HiltApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            RetrofitService.initializeClient();
        }
    }
}
