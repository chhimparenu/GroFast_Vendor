package com.wits.grofast_vendor.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderModel {
    private Integer id;
    private String uuid;
    private int user_order_id;
    private int supplier_id;
    private String total_amount;
    private String delivery_date;
    private String created_at;
    @SerializedName("status")
    private OrderStatusModel orderStatus;

    @SerializedName("items")
    private List<OrderItemModel> orderItems;

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public int getUser_order_id() {
        return user_order_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }


    public String getDelivery_date() {
        return delivery_date;
    }

    public OrderStatusModel getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public String getCreated_at() {
        return created_at;
    }

}
