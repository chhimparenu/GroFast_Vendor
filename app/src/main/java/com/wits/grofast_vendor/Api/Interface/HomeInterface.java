package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.OrderCountResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HomeInterface {
    @GET("get-order-count")
    Call<OrderCountResponse> OrderCount();
}
