package com.wits.grofast_vendor.Homepage;

import static android.view.View.GONE;

import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.os.Build;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_vendor.Adapter.AllResentOrderAdapter;
import com.wits.grofast_vendor.Api.Interface.OrderInterface;
import com.wits.grofast_vendor.Api.Model.OrderModel;
import com.wits.grofast_vendor.Api.Response.OrderResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;
import com.wits.grofast_vendor.session.SupplierDetailSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Fragment extends Fragment implements AllResentOrderAdapter.OnOrderStatusChangeListener{
    RecyclerView recyclerView;
    AllResentOrderAdapter resentOrderAdapter;
    private List<OrderModel> orderList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    SupplierActivitySession supplierActivitySession;
    private static String TAG = "Order Fragment";
    LinearLayout no_oredr_layout, order_layout;
    TextView nomsg1, nomsg2;
    private ShimmerFrameLayout shimmerFrameLayout;

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
        loadOrder();

        return view;
    }

    private void loadOrder() {
        Call<OrderResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(OrderInterface.class).fetchProducts();
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    OrderResponse orderResponse = response.body();
                    List<OrderModel> list = orderResponse.getOrderList();
                    for (OrderModel model : list) {
                        orderList.add(model);
                        resentOrderAdapter = new AllResentOrderAdapter(getContext(), orderList);
                        recyclerView.setAdapter(resentOrderAdapter);
                        Log.e(TAG, "onResponse: order message " + orderResponse.getMessage());
                        Log.e(TAG, "onResponse: order status " + orderResponse.getStatus());
                        Log.e(TAG, "onResponse: order model Id " + model.getId());
                    }
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

            }
        });
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

    @Override
    public void onOrderStatusChanged() {
        ShowPageLoader();
        loadOrder();
    }
}
