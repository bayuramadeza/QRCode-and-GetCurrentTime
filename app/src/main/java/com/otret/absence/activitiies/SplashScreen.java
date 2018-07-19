package com.otret.absence.activitiies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.otret.absence.R;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.utilities.PreferenceHelper;

public class SplashScreen extends AppCompatActivity {
    PreferenceHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefHelper = PreferenceHelper.getInstance(SplashScreen.this);
        final String token = prefHelper.getString(ConstantPreferences.TOKEN, "");
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep (3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }   finally {
                    if (!token.equals("")){
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashScreen.this, Login.class));
                    }
                    finish();

                }
            }
        };
        thread.start();
    }
}
