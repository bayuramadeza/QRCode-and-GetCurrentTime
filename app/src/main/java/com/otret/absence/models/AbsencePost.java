package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AbsencePost {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataAbsence data;

    public Boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public DataAbsence getData() {
        return data;
    }
}
