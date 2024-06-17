package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.ProductResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProductInterface {
    @POST("add-supplier-product")
    @Multipart
    Call<ProductResponse> addProduct(
            @Part("name") RequestBody name,
            @Part("category_id") RequestBody category_id,
            @Part("unit_id") RequestBody unit_id,
            @Part("tax_id") RequestBody tax_id,
            @Part("quantity") RequestBody quantity,
            @Part("price") RequestBody price,
            @Part("discount") RequestBody discount,
            @Part("return_policy") RequestBody return_policy,
            @Part("product_detail") RequestBody product_detail,
            @Part("stock_status") RequestBody stock_status,
            @Part("per") RequestBody per,
            @Part MultipartBody.Part image
    );
}
