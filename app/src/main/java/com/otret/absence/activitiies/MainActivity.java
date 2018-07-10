package com.otret.absence.activitiies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.otret.absence.interfaces.DialogListener;
import com.otret.absence.interfaces.OnClickListener;
import com.otret.absence.interfaces.OnDialogWarningListener;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.models.ModelRumus;
import com.otret.absence.R;
import com.otret.absence.utilities.DialogsUtil;
import com.otret.absence.utilities.PreferenceHelper;
import com.otret.absence.network.ResponseRetrofit;
import com.otret.absence.utilities.RunTimePermissionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends RunTimePermissionsActivity implements OnMapReadyCallback, OnDialogWarningListener, OnClickListener {

    GoogleMap mMap;
    private TextView tvNama;
    private Double longitude;
    private Double latitude;
    private Double officeLatitude;
    private Double officeLongitude;
    private float distance;
    private Boolean inArea = false;
    private int radius = 50;
    private DialogsUtil dialogsUtil;
    private ModelRumus modelRumus = new ModelRumus();
    private PreferenceHelper preferenceHelper;

    @BindView(R.id.b_qr_code) Button bAbsence;
    @BindView(R.id.b_takegeo) Button bGeoTag;
    @BindView(R.id.b_izin) Button izin;
    @BindView(R.id.iv_map) ImageView ivMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        tvNama.setText(preferenceHelper.getString(ConstantPreferences.NAMA_PREF, "Karyawan"));
    }

    @Override
    protected void onPermissionGranted(int requestCode) {

    }

    private void initView(){
        dialogsUtil = new DialogsUtil(MainActivity.this);
        int RECORD_REQUEST_CODE = 101;
        MainActivity.super.requestAppPermissions(new
                        String[]{Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION}, R.string
                        .runtime_permissions_txt
                , RECORD_REQUEST_CODE);
        preferenceHelper = PreferenceHelper.getInstance(MainActivity.this);
        tvNama = findViewById(R.id.tv_nama);
    }

    @OnClick(R.id.b_qr_code)
    public void scanQRCode(){
        Intent intent = new Intent(MainActivity.this, ReadQR.class);
        if (bAbsence.getText().equals(ConstantPreferences.QR_CODE_CHECK_IN)){
            intent.putExtra(ConstantPreferences.KEY_VALIDATION, ConstantPreferences.CHECK_IN);
            startActivityForResult(intent, 1);
        } else {
            intent.putExtra(ConstantPreferences.KEY_VALIDATION, ConstantPreferences.CHECK_OUT_QR);
            startActivityForResult(intent, 2);
        }
    }

    @OnClick(R.id.iv_map)
    public void map(){
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    @OnClick(R.id.b_takegeo)
    public void tagGeo(){
        dialogsUtil.pDialogShow("Data Sedang diproses");
        ResponseRetrofit responseRetrofit = new ResponseRetrofit(MainActivity.this);
        String time = modelRumus.getCurrentTime();
        if (inArea){
            if (bGeoTag.getText().equals(ConstantPreferences.CHECK_OUT_GEO)){
                dialogsUtil.showAbsenceDialog(ConstantPreferences.ABSENCE, ConstantPreferences.OK,
                        ConstantPreferences.JAM_KELUAR_KANTOR+"07.00 WIB", time, "Terimakasih atas kerja kerasnya", this);
            } else {
                int id = Integer.parseInt(preferenceHelper.getString(ConstantPreferences.ID_KARYAWAN_PREF, "0"));
                responseRetrofit.sendData(id,  this);
            }

        } else {
            dialogsUtil.showWarningDialog(ConstantPreferences.INFO, ConstantPreferences.ABSENCE_NOT_ALLOWED, ConstantPreferences.OK, this);
        }
        dialogsUtil.pDialogHide();
    }

    @OnClick(R.id.b_izin)
    public void bIzin(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String getValidation;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                getValidation = data.getStringExtra(ConstantPreferences.KEY_VALIDATION);
                if (getValidation.equals(ConstantPreferences.KEY_GET_VALIDATION)){
                    bAbsence.setText(ConstantPreferences.CHECK_OUT_QR);
                    bGeoTag.setText(ConstantPreferences.CHECK_OUT_GEO);
                }
            }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK){
                getValidation = data.getStringExtra(ConstantPreferences.KEY_VALIDATION);
                if (getValidation.equals(ConstantPreferences.KEY_GET_VALIDATION)) {
                    bAbsence.setText(ConstantPreferences.QR_CODE_CHECK_IN);
                    bGeoTag.setText(ConstantPreferences.GEO_TAG_CHECK_IN);
                }
            }
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
            officeLatitude = Double.parseDouble(preferenceHelper.getString(ConstantPreferences.LATITUDE, "0"));
            officeLongitude = Double.parseDouble(preferenceHelper.getString(ConstantPreferences.LONGITUDE, "0"));
            distance = modelRumus.distanceBetween(latitude,longitude, officeLatitude, officeLongitude);
            if (distance<radius){
                inArea = true;
                ConstantPreferences.toastMessage(MainActivity.this, "You are here");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onMapReady(mMap);
    }

    @Override
    public void onWarningDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferenceHelper.clear(ConstantPreferences.LATITUDE);
        preferenceHelper.clear(ConstantPreferences.LONGITUDE);
    }

    @Override
    public void onClickListener() {
        if (bGeoTag.getText().equals(ConstantPreferences.CHECK_OUT_GEO)){
            bGeoTag.setText(ConstantPreferences.GEO_TAG_CHECK_IN);
            bAbsence.setText(ConstantPreferences.QR_CODE_CHECK_IN);
        } else {
            bGeoTag.setText(ConstantPreferences.CHECK_OUT_GEO);
            bAbsence.setText(ConstantPreferences.CHECK_OUT_QR);
        }
    }
}
