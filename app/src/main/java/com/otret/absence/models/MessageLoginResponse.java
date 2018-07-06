package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class MessageLoginResponse {
    @SerializedName("karyawan_id")
    private Integer karyawanId;
    @SerializedName("lokasi_id")
    private Integer lokasiId;
    @SerializedName("karyawan_nama")
    private String karyawanNama;
    @SerializedName("karyawan_jabatan")
    private String karyawanJabatan;
    @SerializedName("karyawan_ttl")
    private Object karyawanTtl;
    @SerializedName("karyawan_email")
    private String karyawanEmail;
    @SerializedName("karyawan_user")
    private String karyawanUser;
    @SerializedName("no_hp")
    private String noHp;
    @SerializedName("karyawan_alamat")
    private String karyawanAlamat;
    @SerializedName("karyawan_salary")
    private Object karyawanSalary;

    public Integer getKaryawanId() {
        return karyawanId;
    }

    public Integer getLokasiId() {
        return lokasiId;
    }

    public String getKaryawanNama() {
        return karyawanNama;
    }

    public String getKaryawanJabatan() {
        return karyawanJabatan;
    }

    public Object getKaryawanTtl() {
        return karyawanTtl;
    }

    public String getKaryawanEmail() {
        return karyawanEmail;
    }

    public String getKaryawanUser() {
        return karyawanUser;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getKaryawanAlamat() {
        return karyawanAlamat;
    }

    public Object getKaryawanSalary() {
        return karyawanSalary;
    }
}
