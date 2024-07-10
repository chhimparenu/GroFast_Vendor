package com.wits.grofastsupplier.Api.Interface;

import com.wits.grofastsupplier.Api.Response.OrderCountResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HomeInterface {
    @GET("get-order-count")
    Call<OrderCountResponse> OrderCount();
}
