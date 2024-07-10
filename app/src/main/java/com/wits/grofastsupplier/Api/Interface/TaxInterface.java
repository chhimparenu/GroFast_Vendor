package com.wits.grofastsupplier.Api.Interface;

import com.wits.grofastsupplier.Api.Response.TaxReponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaxInterface {
    @GET("fetch-taxes")
    Call<TaxReponse> fetchtaxes();
}
