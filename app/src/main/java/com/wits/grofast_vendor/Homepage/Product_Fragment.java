package com.wits.grofast_vendor.Homepage;

import static android.view.View.GONE;
import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_vendor.Adapter.AllProductAdapter;
import com.wits.grofast_vendor.Api.Interface.ProductInterface;
import com.wits.grofast_vendor.Api.Model.ProductModel;
import com.wits.grofast_vendor.Api.PaginationResponse.ProductPaginatedRes;
import com.wits.grofast_vendor.Api.Response.FetchProductResponse;
import com.wits.grofast_vendor.Api.Response.ProductResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.Details.AddProduct;
import com.wits.grofast_vendor.KeyboardUtil;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_Fragment extends Fragment {
    RecyclerView productrecycleview;
    FloatingActionButton addproduct;
    private AllProductAdapter allProductAdapter;
    private List<ProductModel> productList = new ArrayList<>();
    SupplierActivitySession supplierActivitySession;
    private final String TAG = "Product Fragment";
    private boolean isLoading = false;
    private boolean hasMoreProducts = true;
    private int currentPage = 1;
    NestedScrollView product_layout;
    LinearLayout no_product_layout;
    TextView nomsg1, nomsg2;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public void onResume() {
        super.onResume();
        ShowPageLoader();
        getProducts(currentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_, container, false);

        addproduct = root.findViewById(R.id.new_product_add);
        supplierActivitySession = new SupplierActivitySession(getContext());
        no_product_layout = root.findViewById(R.id.no_product_layout);
        product_layout = root.findViewById(R.id.scroll_product_layout);
        nomsg1 = root.findViewById(R.id.no_product_text1);
        nomsg2 = root.findViewById(R.id.no_product_text2);
        shimmerFrameLayout = root.findViewById(R.id.shimmer_layout_product);

        ShowPageLoader();

        //Product Item
        productrecycleview = root.findViewById(R.id.product_list);
        productrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        allProductAdapter = new AllProductAdapter(getContext(), productList);
        productrecycleview.setAdapter(allProductAdapter);

        productrecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == productList.size() - 1 && !isLoading && hasMoreProducts) {
                    currentPage++;
                    getProducts(currentPage);
                }
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), AddProduct.class);
                startActivity(in);
            }
        });

        final View rootLayout = root.findViewById(R.id.fram_product);
        KeyboardUtil.setKeyboardVisibilityListener(rootLayout, new KeyboardUtil.KeyboardVisibilityListener() {
            @Override
            public void onKeyboardVisibilityChanged(boolean isVisible) {
                if (isVisible) {
                    addproduct.setVisibility(View.GONE);
                } else {
                    addproduct.setVisibility(View.VISIBLE);
                }
            }
        });
        return root;
    }

    private void getProducts(int page) {
        isLoading = true;
        Call<FetchProductResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(ProductInterface.class).fetchProducts(page);
        call.enqueue(new Callback<FetchProductResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<FetchProductResponse> call, Response<FetchProductResponse> response) {
                HidePageLoader();
                isLoading = false;
                if (response.isSuccessful()) {
                    FetchProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        ProductPaginatedRes paginatedResponse = productResponse.getPaginatedProducts();
                        if (paginatedResponse != null) {
                            List<ProductModel> Products = paginatedResponse.getProductList();
                            if (Products != null && !Products.isEmpty()) {
                                productList.clear();
                                allProductAdapter.addProducts(Products);
                            }
                            hasMoreProducts = paginatedResponse.getNext_page_url() != null;
                            Log.d(TAG, "onResponse: getProducts message " + productResponse.getMessage());
                            Log.d(TAG, "onResponse: total products " + paginatedResponse.getTotal());
                            Log.d(TAG, "onResponse: fetched products " + paginatedResponse.getTo());
                        } else {
                            hasMoreProducts = false;
                        }
                    } else {
                        hasMoreProducts = false;
                    }
                } else if (response.code() == 422) {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Gson gson = new Gson();
                        JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                        String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                        String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                        showNoProductMessage(message, errorMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (isAdded()) handleApiError(TAG, response, getContext());
                    hasMoreProducts = false;
                }
            }

            @Override
            public void onFailure(Call<FetchProductResponse> call, Throwable t) {
                t.printStackTrace();
                isLoading = false;
            }
        });
    }

    private void showNoProductMessage(String message, String errorMessage) {
        product_layout.setVisibility(View.GONE);
        no_product_layout.setVisibility(View.VISIBLE);
        nomsg1.setText(errorMessage);
        nomsg2.setText(message);
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        product_layout.setVisibility(View.GONE);
        addproduct.setVisibility(GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        product_layout.setVisibility(View.VISIBLE);
        addproduct.setVisibility(View.VISIBLE);
    }

}

