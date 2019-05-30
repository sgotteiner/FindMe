package com.sagi.findme.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import com.sagi.findme.entities.Location;

import static android.content.Context.MODE_PRIVATE;



public class SharedPreferencesHelper {

    private static final String LAST_LOCATION_LONG_KEY = "LAST_LOCATION_LONG_KEY";
    private static final String LAST_LOCATION_LAT_KEY = "LAST_LOCATION_LAT_KEY";
    private static SharedPreferences preferences;
    private static SharedPreferencesHelper mInstance;
    private final String SETTINGS_APP = "SETTINGS_APP";


    private SharedPreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(SETTINGS_APP, MODE_PRIVATE);
    }

    public static SharedPreferencesHelper getInstance(Context context) {

        if (mInstance == null)
            mInstance = new SharedPreferencesHelper(context);

        return mInstance;
    }


     public void setLocation(float latitude,float longitude) {
        preferences.edit().putFloat(LAST_LOCATION_LAT_KEY,latitude).commit();
        preferences.edit().putFloat(LAST_LOCATION_LONG_KEY,longitude).commit();
    }


     public com.sagi.findme.entities.Location getLastLocation(){
         float latitude,  longitude;
         latitude=preferences.getFloat(LAST_LOCATION_LAT_KEY,-1);
         longitude=preferences.getFloat(LAST_LOCATION_LONG_KEY,-1);
         com.sagi.findme.entities.Location location =new Location(latitude,longitude);
         return location;
     }


    public void resetSharedPreferences() {
            preferences.edit().clear().commit();
    }
}
