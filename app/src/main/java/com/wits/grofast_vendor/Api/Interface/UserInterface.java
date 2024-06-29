package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.SupplierProfileResponse;
import com.wits.grofast_vendor.Api.Response.LoginResponse;
import com.wits.grofast_vendor.Api.Response.OtpResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserInterface {

    @POST("updateSupplierDetails")
    @Multipart
    Call<SupplierProfileResponse> updateProfile(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile_number") RequestBody mobile_number,
            @Part("description") RequestBody description,
            @Part("store_address") RequestBody store_address,
            @Part("pin_code") RequestBody pin_code,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("country") RequestBody country,
            @Part("store_name") RequestBody store_name,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part image
    );

    //Login
    @POST("supplier-otp")
    Call<LoginResponse> Login(@Query("mobile_number") String Phone_no);

    @POST("verify-supplier-otp")
    Call<OtpResponse> Otp(@Query("mobile_number") String Phone_no, @Query("otp") Integer Otp);

    @POST("send-phone-update-otp")
    Call<LoginResponse> sendPhoneUpdateOtp(@Query("mobile_number") String Phone_no);

    @POST("verifyOtp-Change-Phone")
    Call<LoginResponse> verifyPhoneUpdateOtp(@Query("mobile_number") String Phone_no, @Query("otp") int Otp);
}
