package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.UnitResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UnitInterface {
    @GET("fetch-product-unit")
    Call<UnitResponse> fetchproductunit();
}
