package com.otret.absence.interfaces;

import com.otret.absence.models.LocationResponse;
import com.otret.absence.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> responseLogin(@Query("karyawan_user") String user, @Query("karyawan_password") String pass);

    @GET("lokasi_longlat/{id}")
    Call<LocationResponse> responseLocation(@Path("id") int id);
}
