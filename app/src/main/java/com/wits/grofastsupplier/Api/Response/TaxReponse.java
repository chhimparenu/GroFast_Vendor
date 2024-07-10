package com.wits.grofastsupplier.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.Model.TaxModel;

import java.util.List;

public class TaxReponse {
    private String message;

    private Integer status;

    @SerializedName("taxes")
    private List<TaxModel> tax;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<TaxModel> gettax() {
        return tax;
    }
}
