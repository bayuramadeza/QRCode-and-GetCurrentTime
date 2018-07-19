package com.otret.absence.interfaces;

import com.otret.absence.models.CheckOut;
import com.otret.absence.models.IzinResponse;
import com.otret.absence.models.LoginResponse;
import com.otret.absence.models.AbsencePost;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> responseLogin(@Query("karyawan_user") String user, @Query("karyawan_password") String pass);

    @POST("absensi")
    Call<AbsencePost> responsePostData(@Query("karyawan_id") int id);

    @POST("checkout")
    Call<CheckOut> responCheckOut(@Query("karyawan_id") int id);

    @FormUrlEncoded
    @POST("izin")
    Call<IzinResponse> responseIzin(@Field("karyawan_id") int id, @Field("tanggal_awal") String tglAwal, @Field("tanggal_akhir") String tglAkhir, @Field("alasan") String alasan);
}
