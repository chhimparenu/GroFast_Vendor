package com.wits.grofastsupplier.Details;

import static com.wits.grofastsupplier.CommonUtilities.handleApiError;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofastsupplier.Adapter.EarningAdapter;
import com.wits.grofastsupplier.Api.Interface.HomeInterface;
import com.wits.grofastsupplier.Api.Interface.OrderInterface;
import com.wits.grofastsupplier.Api.Model.EarningModel;
import com.wits.grofastsupplier.Api.Model.OrderModel;
import com.wits.grofastsupplier.Api.PaginationResponse.EarningPaginatedRes;
import com.wits.grofastsupplier.Api.PaginationResponse.OrderPaginatedRes;
import com.wits.grofastsupplier.Api.Response.EarningResponse;
import com.wits.grofastsupplier.Api.Response.OrderResponse;
import com.wits.grofastsupplier.Api.Retrofirinstance;
import com.wits.grofastsupplier.R;
import com.wits.grofastsupplier.session.SupplierActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningPage extends AppCompatActivity {
    RecyclerView recyclerView;
    EarningAdapter earningAdapter;
    LinearLayoutManager layoutManager;
    private List<EarningModel> earningList = new ArrayList<>();
    SupplierActivitySession supplierActivitySession;
    private static String TAG = "Earning";
    LinearLayout no_layout, amount_layout;
    TextView nomsg1, nomsg2;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int currentPage = 1;
    private int lastPage = 1;
    private int visibleThreshold = 4;
    private Call<EarningResponse> call;
    private boolean isLoading = false;
    TextView total_amount_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.earning_page));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        setContentView(R.layout.activity_earning_page);

        supplierActivitySession = new SupplierActivitySession(getApplicationContext());
        no_layout = findViewById(R.id.no_earning_layout);
        nomsg1 = findViewById(R.id.no_earning_text1);
        nomsg2 = findViewById(R.id.no_earning_text2);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout_earning);
        amount_layout = findViewById(R.id.total_earning_amount_layout);
        total_amount_text = findViewById(R.id.total_earning_text);

        //Resent Cart Item
        recyclerView = findViewById(R.id.total_earning_recycleview);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ShowPageLoader();

        call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(HomeInterface.class).getEarning(currentPage);
        loadearning(call);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount < lastVisibleItem + visibleThreshold) {
                    Log.e("TAG", "onScrolled: firstVisibleItem : " + firstVisibleItemPosition);
                    Log.e("TAG", "onScrolled: lastVisibleItem : " + lastVisibleItem);
                    Log.e("TAG", "onScrolled:  totalItemCount : " + totalItemCount);
                    Log.e("TAG", "onScrolled: lastVisibleItem + visibleThreshold : " + (lastVisibleItem + visibleThreshold));
                    Log.e("TAG", "onScrolled: current page " + currentPage);

                    isLoading = true;
                    call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(HomeInterface.class).getEarning(currentPage);
                    loadearning(call);
                }
            }
        });
    }

    private void loadearning(Call<EarningResponse> call) {
        Log.e("TAG", "getProducts:     last page  " + lastPage);
        Log.e("TAG", "getProducts: curremnt page  " + currentPage);
        if (lastPage >= currentPage) {
            call.enqueue(new Callback<EarningResponse>() {
                @Override
                public void onResponse(Call<EarningResponse> call, Response<EarningResponse> response) {
                    HidePageLoader();
                    isLoading = false;
                    if (response.isSuccessful()) {
                        EarningResponse earningResponse = response.body();
                        EarningPaginatedRes earningPaginatedRes = earningResponse.getPaginatedRes();
                        total_amount_text.setText("" + earningResponse.getTotal_earning());
                        if (currentPage == 1) {
                            earningList = earningPaginatedRes.getList();
                            earningAdapter = new EarningAdapter(getApplicationContext(), earningList);
                            recyclerView.setAdapter(earningAdapter);
                        } else {
                            List<EarningModel> list = earningPaginatedRes.getList();
                            for (EarningModel model : list) {
                                earningList.add(model);
                                recyclerView.setAdapter(earningAdapter);
                            }
                        }
                        currentPage++;
                        lastPage = earningPaginatedRes.getLast_page();
                    } else if (response.code() == 422) {
                        try {
                            String errorBodyString = response.errorBody().string();
                            Gson gson = new Gson();
                            JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                            String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                            String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                            showNoOrderMessage(message, errorMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        handleApiError(TAG, response, getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<EarningResponse> call, Throwable t) {
                    isLoading = false;
                    HidePageLoader();
                    t.printStackTrace();
                }
            });
        }
    }

    private void showNoOrderMessage(String message, String errorMessage) {
        recyclerView.setVisibility(View.GONE);
        no_layout.setVisibility(View.VISIBLE);
        nomsg1.setText(errorMessage);
        nomsg2.setText(message);
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        amount_layout.setVisibility(View.GONE);
        total_amount_text.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        recyclerView.setVisibility(View.VISIBLE);
        amount_layout.setVisibility(View.VISIBLE);
        total_amount_text.setVisibility(View.VISIBLE);
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