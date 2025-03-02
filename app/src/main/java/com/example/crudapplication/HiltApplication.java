package com.example.crudapplication;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.example.crudapplication.data.api.RetrofitService;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class HiltApplication extends Application {
    private static HiltApplication instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            RetrofitService.initializeClient();
//        }
    }

    public static Context getContext(){
        if (instance == null){
            throw new IllegalStateException("Application is not initialized");
        }
        return instance.getApplicationContext();
    }
}
