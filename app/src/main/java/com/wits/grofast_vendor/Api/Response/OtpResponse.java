package com.wits.grofast_vendor.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.Model.SupplierModel;

public class OtpResponse {
    @SerializedName("access_token")
    private String accessToken;
    private String message;
    @SerializedName("supplier")
    private SupplierModel user;
    private long status;

    public String getAccessToken() {
        return accessToken;
    }

    public SupplierModel getUser() {
        return user;
    }
    public String getMessage() {
        return message;
    }

    public long getStatus() {
        return status;
    }
}
