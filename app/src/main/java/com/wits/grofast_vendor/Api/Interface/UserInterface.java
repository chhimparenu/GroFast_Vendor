package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.EditProfileResponse;
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

    @POST("editCustomerProfile")
    @Multipart
    Call<EditProfileResponse> updateProfile(
            @Part("phone_no") RequestBody phone_no,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part image
    );

    //Login
    @POST("supplier-otp")
    Call<LoginResponse> Login(@Query("mobile_number") String Phone_no);

    @POST("verify-supplier-otp")
    Call<OtpResponse> Otp(@Query("mobile_number") String Phone_no, @Query("otp")  Integer Otp );
}
