package com.wits.grofastsupplier.Api.Interface;

import com.wits.grofastsupplier.Api.Response.UnitResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UnitInterface {
    @GET("fetch-product-unit")
    Call<UnitResponse> fetchproductunit();
}
