package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_vendor.Adapter.NotificationAdapter;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.Notification.InAppNotificationModel;
import com.wits.grofast_vendor.Notification.InAppNotificationPaginatedRes;
import com.wits.grofast_vendor.Notification.InAppNotificationResponse;
import com.wits.grofast_vendor.Notification.NotificationInterface;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<InAppNotificationModel> notificationModelslist = new ArrayList<>();
    SupplierActivitySession supplierActivitySession;
    private static String TAG = "Notification";
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout no_notification_layout;
    TextView no_text1, no_text2;
    private int currentPage = 1;
    private int lastPage = 1;
    private int visibleThreshold = 4;
    private Call<InAppNotificationResponse> call;
    private boolean isLoading = false;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.notification_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_notification);

        supplierActivitySession = new SupplierActivitySession(getApplicationContext());

        shimmerFrameLayout = findViewById(R.id.shimmer_layout_notification);
        no_notification_layout = findViewById(R.id.no_notifictaion_layout);
        no_text1 = findViewById(R.id.no_notification_text1);
        no_text2 = findViewById(R.id.no_notification_text2);

        //Notification Item
        recyclerView = findViewById(R.id.notification_recycle);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShowPageLoader();
        call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(NotificationInterface.class).inappnotification(currentPage);
        fetchNotifications(call);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount < lastVisibleItem + visibleThreshold) {
                    Log.e("TAG", "onScrolled: firstVisibleItem : " + firstVisibleItemPosition);
                    Log.e("TAG", "onScrolled: lastVisibleItem : " + lastVisibleItem);
                    Log.e("TAG", "onScrolled:  totalItemCount : " + totalItemCount);
                    Log.e("TAG", "onScrolled: lastVisibleItem + visibleThreshold : " + (lastVisibleItem + visibleThreshold));
                    Log.e("TAG", "onScrolled: current page " + currentPage);

                    isLoading = true;
                    call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(NotificationInterface.class).inappnotification(currentPage);
                    fetchNotifications(call);
                }
            }
        });
    }

    private void fetchNotifications(Call<InAppNotificationResponse> call) {
        Log.e("TAG", "getProducts:     last page  " + lastPage);
        Log.e("TAG", "getProducts: curremnt page  " + currentPage);
        if (lastPage >= currentPage) {
            call.enqueue(new Callback<InAppNotificationResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<InAppNotificationResponse> call, Response<InAppNotificationResponse> response) {
                    HidePageLoader();
                    isLoading = false;
                    if (response.isSuccessful()) {
                        InAppNotificationResponse notificationResponse = response.body();
                     InAppNotificationPaginatedRes paginatedResponse = notificationResponse.getInAppNotificationPaginatedRes();
                        if (currentPage == 1) {
                            notificationModelslist = paginatedResponse.getInAppNotificationModels();
                            notificationAdapter = new NotificationAdapter(getApplicationContext(), notificationModelslist);
                            recyclerView.setAdapter(notificationAdapter);
                        } else {
                            List<InAppNotificationModel> list = paginatedResponse.getInAppNotificationModels();
                            for (InAppNotificationModel model : list) {
                                notificationModelslist.add(model);
                                notificationAdapter.notifyItemInserted(notificationModelslist.size());
                            }
                        }
                        currentPage++;
                        lastPage = paginatedResponse.getLast_page();
                    } else if (response.code() == 422) {
                        try {
                            String errorBodyString = response.errorBody().string();
                            Gson gson = new Gson();
                            JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                            String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                            String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                            showNoNotificationMessage(errorMessage, message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        handleApiError(TAG, response, getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<InAppNotificationResponse> call, Throwable t) {
                    t.printStackTrace();
                    HidePageLoader();
                }
            });
        }
    }

    private void showNoNotificationMessage(String errormessage, String message) {
        recyclerView.setVisibility(View.GONE);
        no_notification_layout.setVisibility(View.VISIBLE);
        no_text1.setText(message);
        no_text2.setText(errormessage);
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}