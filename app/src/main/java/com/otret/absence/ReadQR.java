package com.otret.absence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadQR extends AppCompatActivity implements ZXingScannerView.ResultHandler, OnMapReadyCallback{
    private ZXingScannerView mScanner;
    private String currentDate, currentTime;
    private GoogleMap mMap;
    private Double longitude;
    private Double latitude;
    private Double officeLatitude = -7.7197619;
    private Double officeLongitude = 110.3825028;
    private float distance;
    private Boolean inArea = false;
    private int radius = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanner = new ZXingScannerView(this);
        setContentView(mScanner);
        onMapReady(mMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScanner.setResultHandler(this);
        mScanner.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScanner.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
            alert(result);
    }

    public void alert(Result result){
        currentTime();

        if (inArea) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Info");
            builder.setMessage(result.getText() + "Date : " + currentDate + currentTime)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(ConstantPreferences.KEY_VALIDATION, ConstantPreferences.KEY_GET_VALIDATION);
                            setResult(RESULT_OK, returnIntent);
                            finish();

                        }
                    })
                    .setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mScanner.resumeCameraPreview(ReadQR.this);
                        }
                    });

            builder.create().show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Scanning");
            builder.setMessage("You are not allowed to take the absence because you are out of absence area")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(ConstantPreferences.KEY_VALIDATION, ConstantPreferences.KEY_GET_VALIDATION);
                            setResult(RESULT_OK, returnIntent);
                            finish();

                        }
                    })
                    .setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mScanner.resumeCameraPreview(ReadQR.this);
                        }
                    });

            builder.create().show();
        }
    }

    public void currentTime(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
        currentTime = "Current Time : " + mdformat.format(calendar.getTime());
        currentDate = "Current Date : "+date.format(calendar.getTime());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                distanceBetween(latitude, officeLatitude, longitude, officeLongitude);

                if (distance<radius){
                    inArea = true;
                    ConstantPreferences.toastMessage(ReadQR.this, "You are in here");
                } else {
                    ConstantPreferences.toastMessage(ReadQR.this, "You have to go to the location to take your absence");
                }
            }
        });
    }

    private void distanceBetween(Double latitude1, Double latitude2, Double longitude1, Double longitude2){
        Location loc1 = new Location("");
        loc1.setLatitude(latitude1);
        loc1.setLongitude(longitude1);
        Location loc2 = new Location("");
        loc2.setLatitude(latitude2);
        loc2.setLongitude(longitude2);
        distance = loc1.distanceTo(loc2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScanner.stopCameraPreview();
        mScanner.stopCamera();
    }
}
