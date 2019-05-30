package com.sagi.findme;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sagi.findme.services.ServiceLocation;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);


        Intent intent=new Intent(MainActivity.this, ServiceLocation.class);
        startService(intent);
    }
}
