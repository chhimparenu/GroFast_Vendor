package com.wits.grofastsupplier.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.Model.UnitModel;

import java.util.List;

public class UnitResponse {
    private String message;

    private Integer status;

    @SerializedName("products")
    private List<UnitModel> unitModels;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<UnitModel> getUnitModel() {
        return unitModels;
    }
}
