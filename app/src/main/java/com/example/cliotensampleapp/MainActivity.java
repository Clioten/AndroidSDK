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
import com.clioten.CliQuery;
import com.clioten.FindCallback;
import com.clioten.ParseException;
import com.clioten.SaveCallback;
import com.clioten.livequery.CliveDevices;
import com.clioten.livequery.IDevice;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String REQUEST = "request";


    EditText T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        T = findViewById(R.id.Edittext);

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
                String location = "Palo Alto";
                String devName = "Demo Device";
                String description = "Demo description";
                CliotAPIs.createDevice(devName, location, description, new IApiCallback() {
                    @Override
                    public void onSuccess(CliObject cliObject) {
                        Log.d(TAG, "Device created: " + cliObject.getObjectId());
                        T.setText(cliObject.getObjectId());
                    }

                    @Override
                    public void onError(String err) {
                        Log.d("Error: ", err);
                    }
                });
            }
        });

        findViewById(R.id.buttonlive).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> devices = new ArrayList<>();
                devices.add(T.getText().toString());
                CliveDevices liveDevices = new CliveDevices(new IDevice() {
                    @Override
                    public void onComm(String data) {
                        Log.d(TAG, "Received data: " + data);
                    }

                    @Override
                    public void onError(String err) {

                    }
                }, devices);

                liveDevices.start();
            }
        });

        findViewById(R.id.buttonsend).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = "data to be sent";

                CliQuery<CliObject> query = CliQuery.getQuery("Live");
                query.whereEqualTo("liveId", T.getText().toString());

                // Retrieve the object by id
                query.findInBackground(new FindCallback<CliObject>() {
                    public void done(List<CliObject> objects, ParseException eg) {
                        if (eg == null && objects.size() > 0) {
                            CliObject req = objects.get(0);
                            req.put("command", REQUEST);
                            req.put("data", data);
                            req.put("who", req.getObjectId());

                            req.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        //success, saved!
                                        Log.d(TAG, "Successfully saved!");
                                    } else {
                                        //fail to save!
                                        Log.d("Error: ", e.getMessage());
                                        e.printStackTrace();

                                    }
                                }
                            });
                        } else {
                            //fail to get!!
                        }
                    }
                });


            }
        });

    }
}
