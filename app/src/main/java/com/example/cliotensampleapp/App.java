package com.example.cliotensampleapp;

import android.app.Application;
import com.clioten.Cliot;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String appId = "denemdemQe5SiKDVFk";
        String clientKey = "clfpIaRtAuhJDSGCeb4b6R";
        String host = "www.clioten.com";
        int port = 80;

        Cliot.init(this, appId, clientKey, host, port);
    }
}
