package com.wits.grofastsupplier.Api.Response;

import com.google.gson.annotations.SerializedName;
import com.wits.grofastsupplier.Api.PaginationResponse.EarningPaginatedRes;

public class EarningResponse {
    private Integer status;
    private String message;
    private Integer total_earning;
    @SerializedName("earnings")
    private EarningPaginatedRes paginatedRes;

    public Integer getStatus() {
        return status;
    }

    public Integer getTotal_earning() {
        return total_earning;
    }

    public String getMessage() {
        return message;
    }

    public EarningPaginatedRes getPaginatedRes() {
        return paginatedRes;
    }
}
