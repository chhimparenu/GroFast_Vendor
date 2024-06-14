package com.wits.grofast_vendor.Api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofirinstance {

    public static String domain = "https://grofast.in/";

    public static Retrofit getClient(){
        return new Retrofit.Builder().baseUrl(domain + "api/supplier").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getUnAuthorizedClient(){
        return new Retrofit.Builder().baseUrl(domain + "api/").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getClient(String token){
        OkHttpClient okHttpClient= new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originslRquest = chain.request();
                Request newRequest = originslRquest.newBuilder().header("Authorization","Bearer " + token).header("Accept", "application/json").build();
                return chain.proceed(newRequest);
            }
        }).build();
        return new Retrofit.Builder().baseUrl(domain + "api/supplier-otp").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();

    }
}
