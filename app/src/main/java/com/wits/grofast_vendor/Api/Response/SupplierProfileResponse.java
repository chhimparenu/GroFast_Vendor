package com.wits.grofast_vendor.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.Model.SupplierModel;

public class SupplierProfileResponse {
    private int status;
    private String message;

    @SerializedName("profileUpdatedData")
    private SupplierModel supplierprofile;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public SupplierModel getSupplierprofile() {
        return supplierprofile;
    }
}
