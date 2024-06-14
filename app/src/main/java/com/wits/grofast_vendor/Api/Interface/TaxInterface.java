package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.TaxReponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaxInterface {
    @GET("fetch-taxes")
    Call<TaxReponse> fetchtaxes();
}
