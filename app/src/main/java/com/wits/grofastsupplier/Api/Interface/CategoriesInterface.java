package com.wits.grofastsupplier.Api.Interface;

import com.wits.grofastsupplier.Api.Response.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesInterface {
    @GET("fetchAllCategories")
    Call<CategoryResponse> fetchCategories();
}
