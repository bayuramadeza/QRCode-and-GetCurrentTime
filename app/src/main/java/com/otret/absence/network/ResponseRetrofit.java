package com.otret.absence.network;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.otret.absence.interfaces.DialogListener;
import com.otret.absence.interfaces.OnClickListener;
import com.otret.absence.models.LocationResponse;
import com.otret.absence.models.PostData;
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.utilities.DialogsUtil;
import com.otret.absence.utilities.PreferenceHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseRetrofit {
    private Context context;
    private DialogsUtil dialogsUtil;

    public ResponseRetrofit(Context context) {
        this.context = context;
    }

    private PreferenceHelper preferenceHelper = PreferenceHelper.getInstance(context);

    public void locResponse(int id){
        BaseApps.getServices().responseLocation(id).enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, String.valueOf((response.body().getLatitude())), Toast.LENGTH_LONG).show();
                    preferenceHelper.setString(ConstantPreferences.LATITUDE, String.valueOf((response.body().getLatitude())));
                    preferenceHelper.setString(ConstantPreferences.LONGITUDE, String.valueOf(response.body().getLongitude()));
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_LONG).show();
                Log.d("OnFailure", t.getMessage());
            }
        });
    }

    public void sendData(int id, final OnClickListener onClickListener){
        dialogsUtil = new DialogsUtil(context);
        BaseApps.getServices().responsePostData(id).enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if (response.isSuccessful()){
                        dialogsUtil.showAbsenceDialog(ConstantPreferences.ABSENCE, ConstantPreferences.OK,
                                ConstantPreferences.JAM_MASUK_KANTOR + "09.00 WIB", ConstantPreferences.WAKTU_CHECK_IN+response.body().getJamMasukAbsen(), response.body().getKeterangan(), onClickListener);
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_LONG).show();
            }
        });
    }
}
