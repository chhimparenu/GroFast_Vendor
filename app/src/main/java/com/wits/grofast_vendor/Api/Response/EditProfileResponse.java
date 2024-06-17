package com.wits.grofast_vendor.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.Model.SupplierModel;

public class EditProfileResponse {
    private int status;
    private String message;

    @SerializedName("user")
    private SupplierModel userProfile;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public SupplierModel getUserProfile() {
        return userProfile;
    }
}
