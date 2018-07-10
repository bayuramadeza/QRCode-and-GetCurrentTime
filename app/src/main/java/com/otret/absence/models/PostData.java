package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostData {
    @SerializedName("karyawan_id")
    private String karyawanId;
    @SerializedName("absen_hari")
    private String absenHari;
    @SerializedName("jam_masuk_kantor")
    private List<JamMasukKantor> jamMasukKantor = null;
    @SerializedName("jam_keluar_kantor")
    private List<JamKeluarKantor> jamKeluarKantor = null;
    @SerializedName("jam_masuk_absen")
    private String jamMasukAbsen;
    @SerializedName("tanggal")
    private Tanggal tanggal;
    @SerializedName("keterangan")
    private String keterangan;
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

    public List<JamMasukKantor> getJamMasukKantor() {
        return jamMasukKantor;
    }

    public List<JamKeluarKantor> getJamKeluarKantor() {
        return jamKeluarKantor;
    }

    public String getJamMasukAbsen() {
        return jamMasukAbsen;
    }

    public Tanggal getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
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
