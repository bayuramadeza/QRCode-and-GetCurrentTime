package com.otret.absence.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private static PreferenceHelper preferenceHelper;
    private SharedPreferences sharedPreferences;

    public static PreferenceHelper getInstance(Context context) {
        if (null == preferenceHelper) {
            preferenceHelper = new PreferenceHelper(
                    context.getSharedPreferences(ConstantPreferences.KEY_PREF, Context.MODE_PRIVATE)
            );
        }
        return preferenceHelper;
    }

    public PreferenceHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    public void clear(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
