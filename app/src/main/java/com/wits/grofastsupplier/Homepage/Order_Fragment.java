package com.wits.grofastsupplier.Homepage;

import static com.wits.grofastsupplier.CommonUtilities.handleApiError;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofastsupplier.Adapter.AllResentOrderAdapter;
import com.wits.grofastsupplier.Api.Interface.OrderInterface;
import com.wits.grofastsupplier.Api.Model.OrderModel;
import com.wits.grofastsupplier.Api.PaginationResponse.OrderPaginatedRes;
import com.wits.grofastsupplier.Api.Response.OrderResponse;
import com.wits.grofastsupplier.Api.Retrofirinstance;
import com.wits.grofastsupplier.R;
import com.wits.grofastsupplier.session.SupplierActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Fragment extends Fragment  {
    RecyclerView recyclerView;
    AllResentOrderAdapter resentOrderAdapter;
    private List<OrderModel> orderList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    SupplierActivitySession supplierActivitySession;
    private static String TAG = "Order Fragment";
    LinearLayout no_oredr_layout, order_layout;
    TextView nomsg1, nomsg2;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int currentPage = 1;
    private int lastPage = 1;
    private int visibleThreshold = 4;
    private Call<OrderResponse> call;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.Resent_order_history));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        supplierActivitySession = new SupplierActivitySession(getContext());
        no_oredr_layout = view.findViewById(R.id.no_order_layout);
        order_layout = view.findViewById(R.id.show_order_data);
        nomsg1 = view.findViewById(R.id.no_order_text1);
        nomsg2 = view.findViewById(R.id.no_order_text2);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_order);

        ShowPageLoader();

        //Order Item List
        recyclerView = view.findViewById(R.id.all_resent_order_recycleview);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(OrderInterface.class).fetchOrders(currentPage);
        loadOrder(call);

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
                    call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(OrderInterface.class).fetchOrders(currentPage);
                    loadOrder(call);
                }
            }
        });

        return view;
    }

    private void loadOrder(Call<OrderResponse> call) {
        Log.e("TAG", "getProducts:     last page  " + lastPage);
        Log.e("TAG", "getProducts: curremnt page  " + currentPage);
        if (lastPage >= currentPage) {
            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    HidePageLoader();
                    isLoading = false;
                    if (response.isSuccessful()) {
                        OrderResponse orderResponse = response.body();
                        OrderPaginatedRes orderPaginatedRes = orderResponse.getPaginatedOrder();
                        if (currentPage == 1) {
                            orderList = orderPaginatedRes.getOrderlist();
                            resentOrderAdapter = new AllResentOrderAdapter(getContext(), orderList);
                            recyclerView.setAdapter(resentOrderAdapter);
                        } else {
                            List<OrderModel> list = orderPaginatedRes.getOrderlist();
                            for (OrderModel model : list) {
                                orderList.add(model);
                                recyclerView.setAdapter(resentOrderAdapter);
                            }
                        }
                        currentPage++;
                        lastPage = orderPaginatedRes.getLast_page();
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
                        if (isAdded()) handleApiError(TAG, response, getContext());
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    isLoading = false;
                    HidePageLoader();
                    t.printStackTrace();
                }
            });
        }
    }

    private void showNoOrderMessage(String message, String errorMessage) {
        order_layout.setVisibility(View.GONE);
        no_oredr_layout.setVisibility(View.VISIBLE);
        nomsg1.setText(errorMessage);
        nomsg2.setText(message);
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        order_layout.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        order_layout.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
