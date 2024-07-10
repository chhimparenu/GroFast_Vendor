package com.wits.grofastsupplier.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.Model.ProductModel;
import com.wits.grofastsupplier.Api.PaginationResponse.ProductPaginatedRes;

public class ProductResponse {
    private String message;

    private Integer status;

    private ProductModel product;

    @SerializedName("products")
    private ProductPaginatedRes paginatedProducts;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public ProductModel getProduct() {
        return product;
    }

    public ProductPaginatedRes getPaginatedProducts() {
        return paginatedProducts;
    }
}
