package com.wits.grofast_vendor.Api.Interface;

import com.wits.grofast_vendor.Api.Response.OrderResponse;
import com.wits.grofast_vendor.Api.Response.OrderStatusResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderInterface {
    @GET("fetch-new-orders")
    Call<OrderResponse> fetchOrders(@Query("page") int page);

    @POST("update-order-status")
    @FormUrlEncoded
    Call<OrderStatusResponse> updateOrderStatus(
            @Field("order_id") int orderId,
            @Field("status") int status
    );

    @POST("update-order-status")
    @FormUrlEncoded
    Call<OrderStatusResponse> updateDeliveryDate(
            @Field("order_id") int orderId,
            @Field("delivery_date") String delivery_date
    );
}
