package com.wits.grofastsupplier.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.Model.OrderCountModel;

import java.util.List;

public class OrderCountResponse {
    private String message;

    private Integer status;
    @SerializedName("data")
    private List<OrderCountModel> orderCountlist;
    private Integer total_orders;
    private Integer total_products;
    private Integer total_earnings;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<OrderCountModel> getOrderCountlist() {
        return orderCountlist;
    }

    public Integer getTotal_orders() {
        return total_orders;
    }

    public Integer getTotal_products() {
        return total_products;
    }

    public Integer getTotal_earnings() {
        return total_earnings;
    }
}
