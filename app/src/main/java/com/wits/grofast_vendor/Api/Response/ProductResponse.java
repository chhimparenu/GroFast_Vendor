package com.wits.grofast_vendor.Api.Response;

import com.wits.grofast_vendor.Api.Model.ProductModel;

import java.util.List;

public class ProductResponse {
    private String message;

    private Integer status;

    private List<ProductModel> product;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public List<ProductModel> getProduct() {
        return product;
    }
}
