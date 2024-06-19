package com.wits.grofast_vendor.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.PaginationResponse.ProductPaginatedRes;

public class FetchProductResponse {
    private String message;

    private Integer status;

    @SerializedName("products")
    private ProductPaginatedRes paginatedProducts;
    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public ProductPaginatedRes getPaginatedProducts() {
        return paginatedProducts;
    }
}
