package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.LoginResponse;
import com.wits.grofast_vendor.Api.Response.OtpResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface userinterface {

    //Login
    @POST("supplier-otp")
    Call<LoginResponse> Login(@Query("mobile_number") String Phone_no);

    @POST("verify-supplier-otp")
    Call<OtpResponse> Otp(@Query("mobile_number") String Phone_no, @Query("otp")  Integer Otp );
}