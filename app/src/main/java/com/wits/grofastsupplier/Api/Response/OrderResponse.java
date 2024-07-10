package com.wits.grofastsupplier.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.PaginationResponse.OrderPaginatedRes;

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
