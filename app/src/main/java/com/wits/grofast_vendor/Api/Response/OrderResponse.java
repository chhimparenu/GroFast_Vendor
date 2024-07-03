package com.wits.grofast_vendor.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.Model.OrderModel;
import com.wits.grofast_vendor.Api.PaginationResponse.OrderPaginatedRes;
import com.wits.grofast_vendor.Api.PaginationResponse.ProductPaginatedRes;

import java.util.List;

public class OrderResponse {
    private String status;
    private String message;
    @SerializedName("orders")
    private OrderPaginatedRes paginatedOrder;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public OrderPaginatedRes getPaginatedOrder() {
        return paginatedOrder;
    }
}
