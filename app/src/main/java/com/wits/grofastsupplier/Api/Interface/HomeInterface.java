package com.wits.grofastsupplier.Api.Interface;

import com.wits.grofastsupplier.Api.Response.EarningResponse;
import com.wits.grofastsupplier.Api.Response.OrderCountResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HomeInterface {
    @GET("get-order-count")
    Call<OrderCountResponse> OrderCount();

    @GET("total-earning")
    Call<EarningResponse> getEarning(@Query("page") int page);
}
