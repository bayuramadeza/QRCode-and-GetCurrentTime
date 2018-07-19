package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("api_token")
    private String apiToken;
    @SerializedName("message")
    private MessageLoginResponse message;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("nama_perusahaan")
    private String namaPerusahaan;
    @SerializedName("jam_masuk_kantor")
    private String masukKantor;
    @SerializedName("jam_keluar_kantor")
    private String keluarKantor;

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getApiToken() {
        return apiToken;
    }

    public MessageLoginResponse getMessage() {
        return message;
    }

    public String getNamaPerusahaan() {
        return namaPerusahaan;
    }

    public String getMasukKantor() {
        return masukKantor;
    }

    public String getKeluarKantor() {
        return keluarKantor;
    }
}
