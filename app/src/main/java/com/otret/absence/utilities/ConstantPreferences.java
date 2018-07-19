package com.otret.absence.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class ConstantPreferences {
    public static final String KEY_GET_VALIDATION = "1";
    public static final String KEY_INVALIDATION = "0";
    public static final String KEY_PREF = "Preferences";
    public static final String KEY_VALIDATION = "KEY_VALIDATION";
    public static final String ABSENCE_NOT_ALLOWED = "Anda tidak bisa mengambil absen disini, silahkan pergi ke kantor anda untuk mengambil absen";
    public static final String WRONG_QRCODE = "Kode QR tidak sesuai dengan kode QR perusahaan anda";
    public static final String QR_CODE_CHECK_IN = "MASUK VIA KODE QR";
    public static final String GEO_TAG_CHECK_IN = "MASUK VIA GEO TAG";
    public static final String ABSENCE = "Absensi";
    public static final String INFO = "Informasi";
    public static final String SCANNING = "Memindai";
    public static final String SCAN_AGAIN = "Pindai Lagi";
    public static final String OK = "OKE";
    public static final String CANCEL = "CANCEL";
    public static final String SPACE = " ";
    public static final String HOUR_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT = "dd-MM-yy";
    public static final String MARKER_OFFICE = "Ambil presensi anda disini";
    public static final String YOUR_MARKER = "Anda berada di lokasi absensi";
    public static final String CHECK_IN = "MASUK";
    public static final String CHECK_OUT = "KELUAR";
    public static final String CHECK_OUT_QR = "KELUAR VIA KODE QR";
    public static final String CHECK_OUT_GEO = "KELUAR VIA GEO Tag";
    public static final String ERROR_FILL_LOGIN = "Silahkan isi username dan Password anda";
    public static final String ERROR_LOGIN_PROCESS = "Username atau Password salah";
    public static final String ERROR_SERVER = "Ada kesalahan pada server";
    public static final String NAMA_PREF = "NAMA";
    public static final String ID_KARYAWAN_PREF = "ID_KARYAWAN";
    public static final String TOKEN = "TOKEN";
    public static final String ID_LOKASI = "LOCATION_ID";
    public static final String JAM_MASUK_KANTOR = "Jam Masuk Kantor : ";
    public static final String JAM_KELUAR_KANTOR = "Jam Keluar Kantor : ";
    public static final String WAKTU_CHECK_IN = "Jam Anda Kasuk : ";
    public static final String WAKTU_CHECK_OUT = "Jam Anda Keluar : ";
    public static final String LATITUDE = "LATTITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String NAMA_PERUSAHAAN = "NAMA_PERUSAHAAN";
    public static final String MASUK_KANTOR = "MASUK_KANTOR";
    public static final String KELUAR_KANTOR = "KELUAR_KANTOR";
    public static final String STATUS = "Status : ";
    public static final String BUTTON_NAME = "BUTTON_NAME ";
    public static final String TANGGAL_IZIN = "Tanggal Mulai Izin";
    public static final String TANGGAL_AKHIR_IZIN = "Tanggal Akhir Izin";
    public static final String LENGKAPI = "Lengkapi data terlebih dahulu!";
    public static final String GAGAL_IZIN = "Gagal mengajukan izin";
    public static final String PROSES_IZIN = "Sedang memproses izin";
    public static final String PROCESS_DATA = "Data sedang diproses";
    public static final String MOCK_WARNING = "Silahkan matikan mock location anda";

    public static void toastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
