package com.wits.grofast_vendor.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.Model.OrderModel;

import java.util.List;

public class OrderResponse {
    private String status;
    private String message;
    @SerializedName("orders")
    private List<OrderModel> orderList;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderModel> getOrderList() {
        return orderList;
    }
}
