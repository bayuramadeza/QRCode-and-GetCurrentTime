package com.otret.absence;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadQR extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScanner;
    private String currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanner = new ZXingScannerView(this);
        setContentView(mScanner);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scanning");
        builder.setMessage(result.getText() + "Date : "+currentDate +currentTime)
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

    public void currentTime(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
        currentTime = "Current Time : " + mdformat.format(calendar.getTime());
        currentDate = "Current Date : "+date.format(calendar.getTime());
    }
}
