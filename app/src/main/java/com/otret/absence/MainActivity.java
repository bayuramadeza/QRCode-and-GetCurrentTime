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

public class MainActivity extends AppCompatActivity {
    private String TAG = "ABSENCE";
    private final int RECORD_REQUEST_CODE = 101;
    private Button bAbsence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAbsence = findViewById(R.id.b_qr_code);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }

        bAbsence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReadQR.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
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
        getValidation = data.getStringExtra(ConstantPreferences.KEY_VALIDATION);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                if (getValidation.equals(ConstantPreferences.KEY_GET_VALIDATION)){
                    bAbsence.setText("Check Out");
                }
            }
        }
    }
}
