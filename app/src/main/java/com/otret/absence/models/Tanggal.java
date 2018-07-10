package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class Tanggal {
    @SerializedName("date")
    private String date;
    @SerializedName("timezone_type")
    private Integer timezoneType;
    @SerializedName("timezone")
    private String timezone;

    public String getDate() {
        return date;
    }

    public Integer getTimezoneType() {
        return timezoneType;
    }

    public String getTimezone() {
        return timezone;
    }
}
