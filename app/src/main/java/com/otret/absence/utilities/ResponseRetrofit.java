package com.otret.absence.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.otret.absence.models.LocationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseRetrofit {
    Context context;

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
}
