package com.otret.absence.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class ConstantPreferences {
    public static final String KEY_GET_VALIDATION = "1";
    public static final String KEY_INVALIDATION = "0";
    public static final String KEY_GET_CHECKOUT_VALIDATION = "2";
    public static final String QR_CODE = "QR_CODE";
    public static final String GEO_TAG = "GEO_TAG";
    public static final String KEY_PREF = "Preferences";
    public static final String KEY_VALIDATION = "KEY_VALIDATION";
    public static final String ABSENCE_NOT_ALLOWED = "You can't take your absence here, go to the center of your absence point";
    public static final String ABSENCE_ON_TIME = "Thank you for not coming late";
    public static final String GO_HOME_ON_TIME = "Thank you for your work";
    public static final String ABSENCE_LATE = "You are late";
    public static final String QR_CODE_CHECK_IN = "QR Code Check In";
    public static final String GEO_TAG_CHECK_IN = "Geo Tag Check In";
    public static final String ABSENCE = "Absence";
    public static final String INFO = "Info";
    public static final String SCANNING = "Scanning";
    public static final String SCAN_AGAIN = "Scan Again";
    public static final String OK = "OK";
    public static final String SPACE = " ";
    public static final String HOUR_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT = "dd-MM-yy";
    public static final String MARKER_OFFICE = "Take your absence here";
    public static final String YOUR_MARKER = "You are here";
    public static final String CHECK_IN = "CHECK IN";
    public static final String CHECK_OUT_QR = "CHECK OUT QR CODE";
    public static final String CHECK_OUT_GEO = "CHECK OUT GEO Tag";
    public static final String ERROR_FILL_LOGIN = "Silahkan isi username dan Password anda";
    public static final String ERROR_LOGIN_PROCESS = "Invalid Username or Password";
    public static final String ERROR_SERVER = "Server Error";
    public static final String NAMA_PREF = "NAMA";
    public static final String ID_KARYAWAN_PREF = "ID_KARYAWAN";
    public static final String TOKEN = "TOKEN";
    public static final String ID_LOKASI = "LOCATION_ID";
    public static final String JAM_MASUK_KANTOR = "Jam Masuk Kantor : ";
    public static final String JAM_KELUAR_KANTOR = "Jam Keluar Kantor : ";
    public static final String WAKTU_CHECK_IN = "Check In Time : ";
    public static final String WAKTU_CHECK_OUT = "Check Out Time : ";
    public static final String KETERANGAN = "Keterangan : ";
    public static final String LATITUDE = "LATITIDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String STATUS = "Status : ";

    public static void toastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
