package com.otret.absence.models;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;

import com.otret.absence.utilities.ConstantPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ModelRumus {
    private Context context;

    public ModelRumus(Context context) {
        this.context = context;
    }

    public float distanceBetween(Double latitude1, Double longitude1, Double officeLatitude, Double officeLongitude){
        final Location loc1 = new Location("");
        loc1.setLatitude(latitude1);
        loc1.setLongitude(longitude1);
        Location loc2 = new Location("");
        loc2.setLatitude(officeLatitude);
        loc2.setLongitude(officeLongitude);
        return loc1.distanceTo(loc2);
    }
//
//    public String getCurrentTime(){
//        Calendar calendar = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat mdformat = new SimpleDateFormat(ConstantPreferences.HOUR_FORMAT);
//        return mdformat.format(calendar.getTime());
//    }
//
//    public String getCurrentDate(){
//        Calendar calendar = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat date = new SimpleDateFormat(ConstantPreferences.DATE_FORMAT);
//        return date.format(calendar.getTime());
//    }
}
