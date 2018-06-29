package com.otret.absence;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private String TAG = "ABSENCE";
    private final int RECORD_REQUEST_CODE = 101;

    @BindView(R.id.b_qr_code) Button bAbsence;
    @BindView(R.id.iv_map) ImageView ivMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequestCamera();
        }

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            makeRequestLocation();
        }
    }

    @OnClick(R.id.b_qr_code)
    public void scanQRCode(){
        Intent intent = new Intent(MainActivity.this, ReadQR.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.iv_map)
    public void map(){
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    protected void makeRequestCamera() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                RECORD_REQUEST_CODE);
    }

    protected void makeRequestLocation(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RECORD_REQUEST_CODE: {
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Log.i(TAG, "Permission has been denied by User: ");
                } else {
                    Log.i(TAG, "Permission has been granted ");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String getValidation;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                getValidation = data.getStringExtra(ConstantPreferences.KEY_VALIDATION);
                if (getValidation.equals(ConstantPreferences.KEY_GET_VALIDATION)){
                    bAbsence.setText(ConstantPreferences.CHECK_OUT);
                }
            }
        }
    }
}
