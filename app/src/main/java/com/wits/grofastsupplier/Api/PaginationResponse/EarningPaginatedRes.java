package com.wits.grofastsupplier.Api.PaginationResponse;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.Model.EarningModel;

import java.util.List;

public class EarningPaginatedRes {
    @SerializedName("data")
    private List<EarningModel> list;

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

    public List<EarningModel> getList() {
        return list;
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
