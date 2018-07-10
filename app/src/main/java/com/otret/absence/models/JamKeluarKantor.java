package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class JamKeluarKantor {
    @SerializedName("jam_keluar")
    private String jamKeluar;

    public String getJamKeluar() {
        return jamKeluar;
    }
}
