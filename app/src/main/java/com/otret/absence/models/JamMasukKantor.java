package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class JamMasukKantor {
    @SerializedName("jam_masuk")
    private String jamMasuk;

    public String getJamMasuk() {
        return jamMasuk;
    }
}
