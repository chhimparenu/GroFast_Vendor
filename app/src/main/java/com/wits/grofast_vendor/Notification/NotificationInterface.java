package com.wits.grofast_vendor.Notification;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationInterface {
    @POST("add-fcm-token")
    Call<Void> storeFcmtoServer(@Query("fcm_token") String token);

    @GET("fetch-supplier-notification-setting")
    Call<NotificationResponse> notificationFetch();

    @POST("supplier-notification-setting")
    Call<NotificationResponse> notificationsettingEnable(
            @Query("enable_all") Integer enable_all,
            @Query("push_notification") Integer push_notification,
            @Query("newsletter_email") Integer newsletter_email,
            @Query("promo_offer_email") Integer promo_offer_email,
            @Query("promo_offer_push") Integer promo_offer_push,
            @Query("social_notification_email") Integer social_notification_email,
            @Query("social_notification_push") Integer social_notification_push
    );

    @POST("supplier-notification-setting")
    Call<NotificationResponse> notificationsetting(
            @Query("push_notification") Integer push_notification,
            @Query("newsletter_email") Integer newsletter_email,
            @Query("promo_offer_email") Integer promo_offer_email,
            @Query("promo_offer_push") Integer promo_offer_push,
            @Query("social_notification_email") Integer social_notification_email,
            @Query("social_notification_push") Integer social_notification_push
    );

    @GET("supplier-inapp-notification")
    Call<InAppNotificationResponse> inappnotification(@Query("page") int page);
}
