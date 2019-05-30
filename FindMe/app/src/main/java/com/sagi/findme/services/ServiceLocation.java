package com.sagi.findme.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.sagi.findme.utilities.SharedPreferencesHelper;

public class ServiceLocation extends Service {

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public ServiceLocation() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());


        LocationRequest locationRequest = new LocationRequest()
                .setFastestInterval(1000)
                .setInterval(1000)
                .setSmallestDisplacement(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mCallbackLocation,null);

    }

    LocationCallback mCallbackLocation =new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult==null)
                return;

            for (Location location:locationResult.getLocations()){
                Log.d("ServiceLocation",location.getLatitude()+", "+location.getLongitude());
                SharedPreferencesHelper.getInstance(getApplicationContext()).setLocation((float) location.getLatitude(),(float) location.getLongitude());
            }

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
}
