package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("latitude")
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
