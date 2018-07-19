package com.otret.absence.activitiies;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.otret.absence.R;
import com.otret.absence.models.LoginResponse;
import com.otret.absence.network.BaseApps;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.utilities.PreferenceHelper;
import com.otret.absence.utilities.RunTimePermissionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends RunTimePermissionsActivity {
    private EditText etUsername;
    private EditText etPass;
    private PreferenceHelper preferenceHelper;
    ProgressDialog pDialog;
    @BindView(R.id.tv_login) TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        ButterKnife.bind(this);
    }

    @Override
    protected void onPermissionGranted(int requestCode) {

    }

    @OnClick(R.id.tv_login)
    public void loginProcess(){
        showDialog();
        String password = etPass.getText().toString();
        String user = etUsername.getText().toString();
        if (user.matches("")||password.matches("")){
            Toast.makeText(Login.this, ConstantPreferences.ERROR_FILL_LOGIN, Toast.LENGTH_LONG).show();
        } else {
            login(user, password);
        }
    }

    public void initView(){
        int RECORD_REQUEST_CODE = 101;
        Login.super.requestAppPermissions(new
                        String[]{Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION}, R.string
                        .runtime_permissions_txt
                , RECORD_REQUEST_CODE);
        etUsername = findViewById(R.id.ti_username);
        etPass = findViewById(R.id.ti_password);
        preferenceHelper = PreferenceHelper.getInstance(Login.this);
        pDialog = new ProgressDialog(this);
    }

    public void login(String username, String password){
        BaseApps.getServices().responseLogin(username, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    preferenceHelper.setString(ConstantPreferences.NAMA_PREF, response.body().getMessage().getKaryawanNama());
                    preferenceHelper.setString(ConstantPreferences.ID_KARYAWAN_PREF, String.valueOf(response.body().getMessage().getKaryawanId()));
                    preferenceHelper.setString(ConstantPreferences.TOKEN, response.body().getApiToken());
                    preferenceHelper.setInt(ConstantPreferences.ID_LOKASI, response.body().getMessage().getLokasiId());
                    preferenceHelper.setString(ConstantPreferences.LATITUDE, String.valueOf((response.body().getLatitude())));
                    preferenceHelper.setString(ConstantPreferences.LONGITUDE, String.valueOf(response.body().getLongitude()));
                    preferenceHelper.setString(ConstantPreferences.NAMA_PERUSAHAAN, response.body().getNamaPerusahaan());
                    preferenceHelper.setString(ConstantPreferences.MASUK_KANTOR, response.body().getMasukKantor());
                    preferenceHelper.setString(ConstantPreferences.KELUAR_KANTOR, response.body().getKeluarKantor());
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, ConstantPreferences.ERROR_SERVER, Toast.LENGTH_LONG).show();
                }
                hideDialog();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, ConstantPreferences.ERROR_LOGIN_PROCESS, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showDialog(){
        pDialog.setMessage("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }

    public void hideDialog(){
        pDialog.dismiss();
    }
}