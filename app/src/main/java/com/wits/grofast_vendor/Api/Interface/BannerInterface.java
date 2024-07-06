package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.BannerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BannerInterface {
    @GET("fetch-banner-for-supplier")
    Call<BannerResponse> fetchbanner();
}
