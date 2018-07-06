package com.otret.absence.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("api_token")
    private String apiToken;
    @SerializedName("message")
    private MessageLoginResponse message;

    public Boolean getSuccess() {
        return success;
    }

    public String getApiToken() {
        return apiToken;
    }

    public MessageLoginResponse getMessage() {
        return message;
    }
}
