package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Model.SpinnerItemModel;
import com.wits.grofast_vendor.Api.Response.CategoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AddressInterface {
    @GET("fetch-country-list")
    Call<List<SpinnerItemModel>> getCountries();

    @POST("fetch-state-list")
    Call<List<SpinnerItemModel>> getStates(@Query("countryId") int countryId);

    @POST("fetch-city-list")
    Call<List<SpinnerItemModel>> getCities(@Query("stateId") int stateId);

    @POST("fetch-pincode-list")
    Call<List<SpinnerItemModel>> getPincodes(@Query("cityId") int cityId);
}
