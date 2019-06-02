package com.example.cliotensampleapp;

import android.app.Application;
import com.clioten.Cliot;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String appId = "";
        String clientKey = "";
        String host = "";
        Integer port = 80;

        Cliot.init(this, appId, clientKey, host, port);
    }
}
