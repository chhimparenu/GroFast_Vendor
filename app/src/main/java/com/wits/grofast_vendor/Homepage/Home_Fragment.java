package com.wits.grofast_vendor.Homepage;

import static android.view.View.GONE;
import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wits.grofast_vendor.Adapter.BannerAdapter;
import com.wits.grofast_vendor.Api.Interface.BannerInterface;
import com.wits.grofast_vendor.Api.Interface.HomeInterface;
import com.wits.grofast_vendor.Api.Model.BannerModel;
import com.wits.grofast_vendor.Api.Model.OrderCountModel;
import com.wits.grofast_vendor.Api.Response.BannerResponse;
import com.wits.grofast_vendor.Api.Response.OrderCountResponse;
import com.wits.grofast_vendor.Api.Response.OrderResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.Details.EarningPage;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Fragment extends Fragment {
    RecyclerView bannerrecyclview;
    BannerAdapter bannerAdapter;
    private int currentBannerPosition = 0;
    private Handler handler = new Handler();
    BarChart barChart;
    SupplierActivitySession supplierActivitySession;
    private static String TAG = "Home_Fragment";
    private ShimmerFrameLayout shimmerFrameLayout;
    NestedScrollView nestedScrollView;
    LinearLayout product_layout, earning_layout, order_layout;
    TextView total_product, total_earning, total_order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_, container, false);

        supplierActivitySession = new SupplierActivitySession(getContext());
        nestedScrollView = root.findViewById(R.id.home_page_all_data);
        shimmerFrameLayout = root.findViewById(R.id.shimmer_layout_home);

        //Linear layouts
        product_layout = root.findViewById(R.id.home_total_product_layout);
        earning_layout = root.findViewById(R.id.home_total_earning_layout);
        order_layout = root.findViewById(R.id.home_total_order_layout);

        //Text view
        total_product = root.findViewById(R.id.home_total_product_text);
        total_earning = root.findViewById(R.id.home_total_earning_text);
        total_order = root.findViewById(R.id.home_total_order_text);

        //Banner Recycleview
        bannerrecyclview = root.findViewById(R.id.banner_home_recycleview);
        bannerrecyclview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        getbanner();

        //Bar chart
        barChart = root.findViewById(R.id.home_bar_chart);
        fetchOrderCountData();
        ShowPageLoader();

        product_layout.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.venfragmentn, new Product_Fragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        order_layout.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.venfragmentn, new Order_Fragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        earning_layout.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), EarningPage.class);
            getContext().startActivity(in);
        });

        return root;
    }

    private void fetchOrderCountData() {
        Call<OrderCountResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(HomeInterface.class).OrderCount();
        call.enqueue(new Callback<OrderCountResponse>() {
            @Override
            public void onResponse(Call<OrderCountResponse> call, Response<OrderCountResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    OrderCountResponse orderResponse = response.body();
                    List<OrderCountModel> orderCountModels = orderResponse.getOrderCountlist();
                    Log.e(TAG, "onResponse: status : " + orderResponse.getStatus());
                    Log.e(TAG, "onResponse: Message : " + orderResponse.getMessage());
                    total_order.setText(""+orderResponse.getTotal_orders());
                    total_earning.setText(""+orderResponse.getTotal_earnings());
                    total_product.setText(""+orderResponse.getTotal_products());
                    if (orderCountModels != null) {
                        Log.e(TAG, "onResponse: Order count models size: " + orderCountModels.size());
                        for (OrderCountModel model : orderCountModels) {
                            Log.e(TAG, "OrderCountModel: Status: " + model.getStatus() + ", Count: " + model.getCount());
                        }
                        updateBarChart(orderCountModels);
                    } else {
                        Log.d(TAG, "Order count list is null");
                    }

                } else {
                    if (isAdded()) handleApiError(TAG, response, getContext());
                }
            }

            @Override
            public void onFailure(Call<OrderCountResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateBarChart(List<OrderCountModel> orderCountModels) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        String[] labels = new String[orderCountModels.size()];

        for (int i = 0; i < orderCountModels.size(); i++) {
            barEntries.add(new BarEntry(i, orderCountModels.get(i).getCount()));
            Log.d(TAG, "updateBarChart: Adding entry: " + orderCountModels.get(i).getCount() + " at index " + i);
            labels[i] = orderCountModels.get(i).getStatus();
            colors.add(Color.parseColor(orderCountModels.get(i).getColor()));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Order Status");
        barDataSet.setColors(colors);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(R.dimen.hint_text_size);

        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        // Set labels for x-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.animateY(1000);
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    private void getbanner() {
        Call<BannerResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(BannerInterface.class).fetchbanner();
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                if (response.isSuccessful()) {
                    List<BannerModel> banners = response.body().getBanners();
                    bannerAdapter = new BannerAdapter(getContext(), banners);
                    bannerrecyclview.setAdapter(bannerAdapter);
                    bannerAdapter.notifyDataSetChanged();
                    startAutoScroll();
                } else {
                    handleApiError(TAG, response, getContext());
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentBannerPosition == bannerAdapter.getItemCount()) {
                    currentBannerPosition = 0;
                }
                bannerrecyclview.smoothScrollToPosition(currentBannerPosition++);
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }
}