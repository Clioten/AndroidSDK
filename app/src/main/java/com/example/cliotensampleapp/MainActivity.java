package com.example.cliotensampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.clioten.APIs.CliotAPIs;
import com.clioten.APIs.IApiCallback;
import com.clioten.CliObject;
import com.clioten.livequery.CliveDevices;
import com.clioten.livequery.IDevice;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String REQUEST = "request";


    private EditText textDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textDeviceId = findViewById(R.id.Edittext);

        findViewById(R.id.buttonuserdevice).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = "";
                String deviceId = "";
                CliotAPIs.createUserDevice(userId, deviceId, new IApiCallback() {
                    @Override
                    public void onSuccess(CliObject cliObject) {
                        Log.d(TAG, "User created: " + cliObject.getObjectId());
                    }

                    @Override
                    public void onError(String err) {
                        Log.d("hsn Error", err);
                    }
                });
            }
        });

        findViewById(R.id.buttoncreatedevice).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createDevice();
            }
        });

        findViewById(R.id.buttonlive).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveDevice();
            }
        });

        findViewById(R.id.buttonsend).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String deviceId = textDeviceId.getText().toString();
                final String data = "Test Data";
                sendOneTimeData(data, deviceId);
            }
        });

    }

    private void sendOneTimeData(String data, String deviceId) {

        CliveDevices.sendOneTimeData(data, deviceId, new IApiCallback() {

            @Override
            public void onSuccess(CliObject cliObject) {
                Log.d(TAG, "data sent");
            }

            @Override
            public void onError(String err) {
                Log.d(TAG, "Error: " + err);
            }
        });
    }

    private void startLiveDevice() {
        List<String> devices = new ArrayList<>();
        devices.add(textDeviceId.getText().toString());
        CliveDevices liveDevices = new CliveDevices(new IDevice() {

            @Override
            public void onComm(String data) {
                Log.d(TAG, "Received data: " + data);
            }

            @Override
            public void onError(String err) {
                Log.d("Error: ", err);
            }
        }, devices);
        liveDevices.start();
    }

    private void createDevice() {
        String location = "Palo Alto";
        String devName = "Demo Device";
        String description = "Demo description";
        CliotAPIs.createDevice(devName, location, description, new IApiCallback() {

            @Override
            public void onSuccess(CliObject cliObject) {
                Log.d(TAG, "Device created: " + cliObject.getObjectId());
                textDeviceId.setText(cliObject.getObjectId());
            }

            @Override
            public void onError(String err) {
                Log.d("Error: ", err);
            }
        });
    }
}
