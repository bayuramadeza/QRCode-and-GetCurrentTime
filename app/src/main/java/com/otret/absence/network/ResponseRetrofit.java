package com.otret.absence.network;

import android.content.Context;
import android.widget.Toast;

import com.otret.absence.interfaces.OnClickListener;
import com.otret.absence.interfaces.OnDialogWarningListener;
import com.otret.absence.models.CheckOut;
import com.otret.absence.models.AbsencePost;
import com.otret.absence.models.IzinResponse;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.utilities.DialogsUtil;
import com.otret.absence.utilities.PreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseRetrofit {
    private Context context;
    private DialogsUtil dialogsUtil;
    PreferenceHelper prefHelper = PreferenceHelper.getInstance(context);

    public ResponseRetrofit(Context context) {
        this.context = context;
    }


    public void sendData(int id, final OnDialogWarningListener onDialogWarningListener, final OnClickListener onClickListener){
        dialogsUtil = new DialogsUtil(context);
        final String masuk = ConstantPreferences.JAM_MASUK_KANTOR + prefHelper.getString(ConstantPreferences.MASUK_KANTOR, "00:00")+" WIB";
        BaseApps.getServices().responsePostData(id).enqueue(new Callback<AbsencePost>() {
            @Override
            public void onResponse(Call<AbsencePost> call, Response<AbsencePost> response) {
                if (response.isSuccessful()){
                    if(response.body().isSuccess()) {
                        dialogsUtil.showAbsenceDialog(ConstantPreferences.ABSENCE, ConstantPreferences.OK,masuk,
                                ConstantPreferences.WAKTU_CHECK_IN + response.body().getData().getJamMasukAbsen(), getStatus(response.body().getData().getStatus()), onClickListener);
                    } else {
                        dialogsUtil.showWarningDialog(ConstantPreferences.ABSENCE, response.body().getMessage(),
                                ConstantPreferences.OK,onDialogWarningListener );
                    }
                }
            }
            @Override
            public void onFailure(Call<AbsencePost> call, Throwable t) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkout(final int id, final OnClickListener onClickListener){
        dialogsUtil = new DialogsUtil(context);
        final String keluar = ConstantPreferences.JAM_KELUAR_KANTOR+prefHelper.getString(ConstantPreferences.KELUAR_KANTOR, "00:00")+" WIB";
        BaseApps.getServices().responCheckOut(id).enqueue(new Callback<CheckOut>() {
            @Override
            public void onResponse(Call<CheckOut> call, Response<CheckOut> response) {
                if (response.isSuccessful()){
                    if (response.body().isSuccess()){
                        String jam = response.body().getJamKeluar();
                        String keterangan = response.body().getKeterangan();
                        dialogsUtil.showAbsenceDialog(ConstantPreferences.ABSENCE, ConstantPreferences.OK, keluar,
                                ConstantPreferences.WAKTU_CHECK_OUT+jam, keterangan, onClickListener);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckOut> call, Throwable t) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void izin(int id, String tglMasuk, String tglAkhir, String alasan, final OnDialogWarningListener onDialogWarningListener){
        dialogsUtil = new DialogsUtil(context);
        BaseApps.getServices().responseIzin(id, tglMasuk, tglAkhir, alasan).enqueue(new Callback<IzinResponse>() {
            @Override
            public void onResponse(Call<IzinResponse> call, Response<IzinResponse> response) {
                if(response.isSuccessful()){
                    if (response.body().getSuccess()){
                        dialogsUtil.showWarningDialog(ConstantPreferences.ABSENCE, response.body().getMessage(),
                                ConstantPreferences.OK,onDialogWarningListener );
                    } else {
                        dialogsUtil.showWarningDialog(ConstantPreferences.ABSENCE, response.body().getMessage(),
                                ConstantPreferences.OK,onDialogWarningListener );
                    }
                }
            }
            @Override
            public void onFailure(Call<IzinResponse> call, Throwable t) {
                Toast.makeText(context, ConstantPreferences.GAGAL_IZIN, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getStatus(int status){
        String keterangan = null;
        switch (status) {
            case 1 :
                keterangan = "Masuk Kerja";
                break;
            case 2 :
                keterangan =  "Terlambat";
                break;
            case 3 :
                keterangan =  "Izin";
                break;
            case 4 :
                keterangan =  "Sakit";
                break;
            case 5 :
                keterangan = "Cuti";
                break;
        }
        return keterangan;
    }
}