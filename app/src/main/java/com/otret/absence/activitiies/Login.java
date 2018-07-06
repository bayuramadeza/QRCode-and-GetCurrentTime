package com.otret.absence.activitiies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.otret.absence.R;
import com.otret.absence.models.LoginResponse;
import com.otret.absence.utilities.BaseApps;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.utilities.DialogsUtil;
import com.otret.absence.utilities.PreferenceHelper;
import com.otret.absence.utilities.ResponseRetrofit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPass;
    private PreferenceHelper preferenceHelper;
    private DialogsUtil dialogsUtil;
    private ResponseRetrofit responseRetrofit;
    @BindView(R.id.tv_login) TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_login)
    public void loginProcess(){
        dialogsUtil.pDialogShow("Please Wait");
        String password = etPass.getText().toString();
        String user = etUsername.getText().toString();
        if (user.matches("")||password.matches("")){
            Toast.makeText(Login.this, ConstantPreferences.ERROR_FILL_LOGIN, Toast.LENGTH_LONG).show();
        } else {
            login(user, password);
        }
    }

    public void initView(){
        etUsername = findViewById(R.id.ti_username);
        etPass = findViewById(R.id.ti_password);
        preferenceHelper = PreferenceHelper.getInstance(Login.this);
        dialogsUtil = new DialogsUtil(Login.this);
        responseRetrofit = new ResponseRetrofit(Login.this);
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
                    responseRetrofit.locResponse(response.body().getMessage().getLokasiId());
                    startActivity(new Intent(Login.this, MainActivity.class));
                } else {
                    Toast.makeText(Login.this, ConstantPreferences.ERROR_SERVER, Toast.LENGTH_LONG).show();
                }
                dialogsUtil.pDialogHide();
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, ConstantPreferences.ERROR_LOGIN_PROCESS, Toast.LENGTH_LONG).show();
                dialogsUtil.pDialogHide();
            }
        });
    }
}
