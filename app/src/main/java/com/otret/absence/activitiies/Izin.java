package com.otret.absence.activitiies;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.otret.absence.R;
import com.otret.absence.interfaces.OnDialogWarningListener;
import com.otret.absence.network.ResponseRetrofit;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.utilities.DialogsUtil;
import com.otret.absence.utilities.PreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Izin extends AppCompatActivity implements OnDialogWarningListener {
    @BindView(R.id.tv_mulai)
    TextView tvMulaiIzin;
    @BindView(R.id.tv_akhir)
    TextView tvAkhirIzin;
    @BindView(R.id.switch1)
    Switch aSwitch;
    @BindView(R.id.rl_tanggal_akhir)
    RelativeLayout rlAkhir;
    @BindView(R.id.b_ajukan_izin)
    Button bIzin;
    @BindView(R.id.ti_alasan)
    TextInputEditText tiAlasan;
    Unbinder unbinder;
    private PreferenceHelper preferenceHelper;
    private DialogsUtil dialogsUtil;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin);

        ButterKnife.bind(this);

        unbinder = ButterKnife.bind(this);
        dialogsUtil = new DialogsUtil(this);
        preferenceHelper = PreferenceHelper.getInstance(Izin.this);
        pDialog = new ProgressDialog(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.switch1)
    public void setaSwitch(){
        if (aSwitch.isChecked()){
            rlAkhir.setVisibility(View.VISIBLE);
        } else {
            rlAkhir.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_mulai)
    public void tanggalMulai(){
        datePicker(tvMulaiIzin);
    }

    @OnClick(R.id.tv_akhir)
    public void tanggalAkhir(){
        datePicker(tvAkhirIzin);
    }

    @OnClick(R.id.b_ajukan_izin)
    public void izin(){
        showDialog(ConstantPreferences.PROSES_IZIN);
        if (tvMulaiIzin.getText().toString().equalsIgnoreCase(ConstantPreferences.TANGGAL_IZIN)|| tiAlasan.getText().toString().matches("")){
            Toast.makeText(Izin.this, ConstantPreferences.LENGKAPI, Toast.LENGTH_LONG).show();
        } else {
            ResponseRetrofit respon = new ResponseRetrofit(Izin.this);
            int id = Integer.parseInt(preferenceHelper.getString(ConstantPreferences.ID_KARYAWAN_PREF, "0"));
            String akhirIzin = tvAkhirIzin.getText().toString();
            if (akhirIzin.equalsIgnoreCase(ConstantPreferences.TANGGAL_AKHIR_IZIN)){
                akhirIzin = tvMulaiIzin.getText().toString();
            }
            respon.izin(id, tvMulaiIzin.getText().toString(), akhirIzin, tiAlasan.getText().toString(), this);
        }
        hideDialog();
    }

    public void datePicker(final TextView textView){
        final Calendar myCalendar = Calendar.getInstance();
        new DatePickerDialog(Izin.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formatTanggal = "yyyy-MM-dd";
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                textView.setText(sdf.format(myCalendar.getTime()));
            }
        },
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onWarningDialog() {
        finish();
    }

    public void showDialog(String message){
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
    }

    public void hideDialog(){
        pDialog.hide();
    }
}