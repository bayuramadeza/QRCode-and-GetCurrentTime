package com.otret.absence.activitiies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.Result;
import com.otret.absence.interfaces.DialogListener;
import com.otret.absence.interfaces.OnClickListener;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.models.ModelRumus;
import com.otret.absence.interfaces.OnDialogButtonClickListener;
import com.otret.absence.utilities.DialogsUtil;
import com.otret.absence.utilities.PreferenceHelper;
import com.otret.absence.network.ResponseRetrofit;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadQR extends AppCompatActivity implements ZXingScannerView.ResultHandler, OnMapReadyCallback, OnDialogButtonClickListener, OnClickListener {
    private ZXingScannerView mScanner;
    private GoogleMap mMap;
    private DialogsUtil dialogsUtil;
    private Double longitude;
    private Double latitude;
    private float distance;
    private Boolean inArea = false;
    private int radius = 50;
    private String codeQR;
    private PreferenceHelper prefHelper;
    private ModelRumus modelRumus = new ModelRumus();
    private ResponseRetrofit responseRetrofit = new ResponseRetrofit(ReadQR.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanner = new ZXingScannerView(this);
        dialogsUtil = new DialogsUtil(ReadQR.this);
        prefHelper = PreferenceHelper.getInstance(ReadQR.this);
        setContentView(mScanner);
        Intent intent = getIntent();
        codeQR = intent.getStringExtra(ConstantPreferences.KEY_VALIDATION);
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
        String date = modelRumus.getCurrentDate();
        String time = modelRumus.getCurrentTime();
        int id = Integer.parseInt(prefHelper.getString(ConstantPreferences.ID_KARYAWAN_PREF, "0"));
        if (inArea) {
            if (codeQR.equalsIgnoreCase(ConstantPreferences.CHECK_IN)){
                responseRetrofit.sendData(id, this);
//                dialogsUtil.showAbsenceDialog(ConstantPreferences.ABSENCE, ConstantPreferences.OK,
//                        ConstantPreferences.JAM_MASUK_KANTOR + "09.00 WIB", ConstantPreferences.WAKTU_CHECK_IN+time, "telat");
            } else {
                dialogsUtil.showAbsenceDialog(ConstantPreferences.ABSENCE, ConstantPreferences.OK,
                        ConstantPreferences.JAM_KELUAR_KANTOR+"17.00 WIB", ConstantPreferences.WAKTU_CHECK_OUT + time, "telat", this);
            }
        } else {
            dialogsUtil.showQRWarning(ConstantPreferences.SCANNING, ConstantPreferences.ABSENCE_NOT_ALLOWED,
                        ConstantPreferences.OK, ConstantPreferences.SCAN_AGAIN, this);
        }
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
                Double officeLong = Double.parseDouble(prefHelper.getString(ConstantPreferences.LONGITUDE, "0"));
                Double officeLat = Double.parseDouble(prefHelper.getString(ConstantPreferences.LATITUDE, "0"));
                distance = modelRumus.distanceBetween(latitude, longitude, officeLat, officeLong);
                if (distance<radius){
                    inArea = true;
                } else {
                    ConstantPreferences.toastMessage(ReadQR.this, ConstantPreferences.ABSENCE_NOT_ALLOWED);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScanner.stopCameraPreview();
        mScanner.stopCamera();
    }

    @Override
    public void onPositiveButtonClicked() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ConstantPreferences.KEY_VALIDATION, ConstantPreferences.KEY_GET_VALIDATION);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onNegativeButtonClicked() {
        mScanner.resumeCameraPreview(ReadQR.this);
    }

    @Override
    public void onClickListener() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ConstantPreferences.KEY_VALIDATION, ConstantPreferences.KEY_GET_VALIDATION);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
