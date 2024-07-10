package com.wits.grofastsupplier.Api.Model;

import com.google.gson.annotations.SerializedName;

public class OrderItemModel {
    private Integer id;
    private Integer supplier_order_id;
    private Integer product_id;
    private Integer quantity;
    private Integer subtotal;
    private String price;
    @SerializedName("product")
    private ProductModel product;

    public Integer getId() {
        return id;
    }

    public Integer getSupplier_order_id() {
        return supplier_order_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public String getPrice() {
        return price;
    }

    public ProductModel getProduct() {
        return product;
    }
}
