package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class DataAbsence {
    @SerializedName("karyawan_id")
    private String karyawanId;
    @SerializedName("absen_hari")
    private String absenHari;
    @SerializedName("jam_masuk_absen")
    private String jamMasukAbsen;
    @SerializedName("tanggal")
    private Tanggal tanggal;
    @SerializedName("status")
    private Integer status;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("id")
    public Integer id;

    public String getKaryawanId() {
        return karyawanId;
    }

    public String getAbsenHari() {
        return absenHari;
    }

    public String getJamMasukAbsen() {
        return jamMasukAbsen;
    }

    public Tanggal getTanggal() {
        return tanggal;
    }

    public Integer getStatus() {
        return status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getId() {
        return id;
    }
}
