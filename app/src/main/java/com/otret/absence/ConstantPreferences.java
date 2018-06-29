package com.otret.absence;

import android.content.Context;
import android.widget.Toast;

public class ConstantPreferences {
    public static final String KEY_GET_VALIDATION = "1";
    public static final String KEY_INVALIDATION = "0";
    public static final String KEY_GET_CHECKOUT_VALIDATION = "2";
    public static final String KEY_VALIDATION = "KEY_VALIDATION";

    public static final String MARKER_OFFICE = "Take your absence here";
    public static final String YOUR_MARKER = "You are here";
    public static final String CHECK_IN = "CHECK IN";
    public static final String CHECK_OUT = "CHECK OUT";

    public static void toastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
