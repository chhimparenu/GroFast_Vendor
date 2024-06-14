package com.wits.grofast_vendor.Api.Response;

import com.wits.grofast_vendor.Api.Model.TaxModel;
import com.wits.grofast_vendor.Api.Model.UnitModel;

import java.util.List;

public class UnitResponse {
    private String message;

    private Integer status;

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
