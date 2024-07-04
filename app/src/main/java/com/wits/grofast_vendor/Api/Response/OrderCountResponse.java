package com.wits.grofast_vendor.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.Model.OrderCountModel;

import java.util.List;

public class OrderCountResponse {
    private String message;

    private Integer status;
    @SerializedName("data")
    private List<OrderCountModel> orderCountlist;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<OrderCountModel> getOrderCountlist() {
        return orderCountlist;
    }
}
