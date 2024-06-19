package com.wits.grofast_vendor.Api.PaginationResponse;

import com.google.gson.annotations.SerializedName;
import com.wits.grofast_vendor.Api.Model.ProductModel;

import java.util.List;

public class ProductPaginatedRes {
    @SerializedName("data")
    private List<ProductModel> productList;

    private Integer current_page;

    private Integer last_page;

    private Integer from;

    private Integer to;
    private String next_page_url;

    private Integer total;

    private Integer per_page;

    public String getNext_page_url() {
        return next_page_url;
    }

    public List<ProductModel> getProductList() {
        return productList;
    }

    public Integer getCurrent_page() {
        return current_page;
    }

    public Integer getLast_page() {
        return last_page;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPer_page() {
        return per_page;
    }
}
