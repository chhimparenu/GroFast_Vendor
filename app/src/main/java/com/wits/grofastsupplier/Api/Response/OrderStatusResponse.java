package com.wits.grofastsupplier.Api.Response;

import com.wits.grofastsupplier.Api.Model.OrderModel;

public class OrderStatusResponse {
    private String message;
    private int status;

    private OrderModel order;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public OrderModel getOrder() {
        return order;
    }
}
