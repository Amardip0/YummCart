package com.cs7035.yummcart;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class App extends Application {
    static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        try {
            registerReceiver(new AirplaneModeReceiver(), new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
        } catch (Exception ignored) {
        }
    }
}
