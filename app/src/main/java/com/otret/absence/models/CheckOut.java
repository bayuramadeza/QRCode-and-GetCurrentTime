package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class CheckOut {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    public String message;
    @SerializedName("jam_keluar")
    private String jamKeluar;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("status")
    private String status;

    public Boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getJamKeluar() {
        return jamKeluar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getStatus() {
        return status;
    }
}
