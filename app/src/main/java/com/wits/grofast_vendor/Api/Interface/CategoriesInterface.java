package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesInterface {
    @GET("fetchAllCategories")
    Call<CategoryResponse> fetchCategories();
}
