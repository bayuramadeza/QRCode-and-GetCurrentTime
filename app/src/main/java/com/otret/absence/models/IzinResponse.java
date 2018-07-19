package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class IzinResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    public String message;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
