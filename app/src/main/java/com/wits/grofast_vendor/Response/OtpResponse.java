package com.wits.grofast_vendor.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Model.UserModel;

public class OtpResponse {
    @SerializedName("access_token")
    private String accessToken;
    private String message;
    @SerializedName("supplier")
    private UserModel user;
    private long status;

    public String getAccessToken() {
        return accessToken;
    }

    public UserModel getUser() {
        return user;
    }
    public String getMessage() {
        return message;
    }

    public long getStatus() {
        return status;
    }
}
