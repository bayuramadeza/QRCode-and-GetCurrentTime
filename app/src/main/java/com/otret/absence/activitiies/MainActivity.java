package com.otret.absence.activitiies;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.otret.absence.interfaces.OnClickListener;
import com.otret.absence.interfaces.OnDialogButtonClickListener;
import com.otret.absence.interfaces.OnDialogWarningListener;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.models.ModelRumus;
import com.otret.absence.R;
import com.otret.absence.utilities.DialogsUtil;
import com.otret.absence.utilities.PreferenceHelper;
import com.otret.absence.network.ResponseRetrofit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnDialogWarningListener, OnClickListener, OnDialogButtonClickListener
{
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
    private ProgressDialog pDialog;
    private ModelRumus modelRumus = new ModelRumus(MainActivity.this);
    private PreferenceHelper preferenceHelper;
    @BindView(R.id.tv_nama_perusahaan) TextView tvPerusahaan;
    @BindView(R.id.iv_logout) ImageView ivLogout;
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
        tvPerusahaan.setText(preferenceHelper.getString(ConstantPreferences.NAMA_PERUSAHAAN, "Perusahaan"));
    }

    private void initView(){
        dialogsUtil = new DialogsUtil(this);
        pDialog = new ProgressDialog(this);
        preferenceHelper = PreferenceHelper.getInstance(MainActivity.this);
        tvNama = findViewById(R.id.tv_nama);
        if (preferenceHelper.getString(ConstantPreferences.BUTTON_NAME, ConstantPreferences.CHECK_OUT).equalsIgnoreCase(ConstantPreferences.CHECK_IN)){
            bGeoTag.setText(ConstantPreferences.CHECK_OUT_GEO);
            bAbsence.setText(ConstantPreferences.CHECK_OUT_QR);
        }
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
        showDialog(ConstantPreferences.PROCESS_DATA);
        ResponseRetrofit responseRetrofit = new ResponseRetrofit(MainActivity.this);
        int id = Integer.parseInt(preferenceHelper.getString(ConstantPreferences.ID_KARYAWAN_PREF, "0"));
        if (inArea){
            if (bGeoTag.getText().equals(ConstantPreferences.CHECK_OUT_GEO)){
                responseRetrofit.checkout(id, this);
            } else {
                responseRetrofit.sendData(id, this, this);
            }

        } else {
            dialogsUtil.showWarningDialog(ConstantPreferences.INFO, ConstantPreferences.ABSENCE_NOT_ALLOWED, ConstantPreferences.OK, this);
        }
        hideDialog();
    }

    @OnClick(R.id.b_izin)
    public void bIzin(){
        startActivity(new Intent(MainActivity.this, Izin.class));
    }

    @OnClick(R.id.iv_logout)
    public void logout(){
        dialogsUtil.showWarning("Logout", "Apakah anda yakin ingin keluar?", ConstantPreferences.OK,
                ConstantPreferences.CANCEL, this);
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
                    preferenceHelper.setString(ConstantPreferences.BUTTON_NAME, ConstantPreferences.CHECK_IN);
                }
            }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK){
                getValidation = data.getStringExtra(ConstantPreferences.KEY_VALIDATION);
                if (getValidation.equals(ConstantPreferences.KEY_GET_VALIDATION)) {
                    bAbsence.setText(ConstantPreferences.QR_CODE_CHECK_IN);
                    bGeoTag.setText(ConstantPreferences.GEO_TAG_CHECK_IN);
                    preferenceHelper.setString(ConstantPreferences.BUTTON_NAME, ConstantPreferences.CHECK_OUT);
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
            Boolean mockLoc;
            mockLoc = location.isFromMockProvider();
            if (mockLoc){
                checkMock();
            }
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            officeLatitude = Double.parseDouble(preferenceHelper.getString(ConstantPreferences.LATITUDE, "0"));
            officeLongitude = Double.parseDouble(preferenceHelper.getString(ConstantPreferences.LONGITUDE, "0"));
            distance = modelRumus.distanceBetween(latitude,longitude, officeLatitude, officeLongitude);
            if (distance<radius){
                    inArea = true;
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
    public void onClickListener() {
        if (bGeoTag.getText().equals(ConstantPreferences.CHECK_OUT_GEO)){
            bGeoTag.setText(ConstantPreferences.GEO_TAG_CHECK_IN);
            bAbsence.setText(ConstantPreferences.QR_CODE_CHECK_IN);
            preferenceHelper.setString(ConstantPreferences.BUTTON_NAME, ConstantPreferences.CHECK_OUT);
        } else {
            bGeoTag.setText(ConstantPreferences.CHECK_OUT_GEO);
            bAbsence.setText(ConstantPreferences.CHECK_OUT_QR);
            preferenceHelper.setString(ConstantPreferences.BUTTON_NAME, ConstantPreferences.CHECK_IN);
        }
    }

    public void logoutUser(){
        preferenceHelper.clear(ConstantPreferences.NAMA_PERUSAHAAN);
        preferenceHelper.clear(ConstantPreferences.TOKEN);
        preferenceHelper.clear(ConstantPreferences.BUTTON_NAME);
        preferenceHelper.clear(ConstantPreferences.ID_KARYAWAN_PREF);
        preferenceHelper.clear(ConstantPreferences.NAMA_PREF);
        preferenceHelper.clear(ConstantPreferences.LONGITUDE);
        preferenceHelper.clear(ConstantPreferences.LATITUDE);
        preferenceHelper.clear(ConstantPreferences.ID_LOKASI);
        preferenceHelper.clear(ConstantPreferences.MASUK_KANTOR);
        preferenceHelper.clear(ConstantPreferences.KELUAR_KANTOR);
    }

    @Override
    public void onPositiveButtonClicked() {
        startActivity(new Intent(MainActivity.this, Login.class));
        logoutUser();
        finish();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    public void showDialog(String message){
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }

    public void hideDialog(){
        pDialog.dismiss();
    }

    public void checkMock(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(ConstantPreferences.INFO);
        alert.setMessage(ConstantPreferences.MOCK_WARNING)
                .setPositiveButton(ConstantPreferences.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS),3);
                    }
                })
                .setNegativeButton(ConstantPreferences.CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alert.setCancelable(false);
        alert.create().show();
    }
}
