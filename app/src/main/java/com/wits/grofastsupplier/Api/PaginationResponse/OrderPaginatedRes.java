package com.wits.grofastsupplier.Api.PaginationResponse;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.Model.OrderModel;

import java.util.List;

public class OrderPaginatedRes {
    @SerializedName("data")
    private List<OrderModel> orderlist;

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

    public List<OrderModel> getOrderlist() {
        return orderlist;
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
