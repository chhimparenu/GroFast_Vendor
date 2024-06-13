package com.wits.grofast_vendor.Homepage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wits.grofast_vendor.Adapter.BannerAdapter;
import com.wits.grofast_vendor.Adapter.HomeOrderHistoryAdapter;
import com.wits.grofast_vendor.Details.AllResentOrderHistory;
import com.wits.grofast_vendor.Model.HomeOrderHistoryModel;
import com.wits.grofast_vendor.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home_Fragment extends Fragment {
    RecyclerView bannerrecyclview, recentorderrecycleview;
    BannerAdapter bannerAdapter;
    private List<Integer> bannerImages;
    private int currentBannerPosition = 0;
    private Handler handler = new Handler();
    private HomeOrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<HomeOrderHistoryModel> orderHistoryModels;
    AppCompatButton view_all_order_history;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_, container, false);
        view_all_order_history = root.findViewById(R.id.home_page_view_all);
        //Banner Recycleview
        bannerrecyclview = root.findViewById(R.id.banner_home_recycleview);
        bannerImages = Arrays.asList(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3);
        bannerAdapter = new BannerAdapter(getContext(), bannerImages);
        bannerrecyclview.setAdapter(bannerAdapter);
        bannerrecyclview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        startAutoScroll();

        //Table layout
        orderHistoryModels = new ArrayList<>();
        recentorderrecycleview = root.findViewById(R.id.home_recent_order_recycleview);
        OrderList();
        orderHistoryAdapter = new HomeOrderHistoryAdapter(getContext(), orderHistoryModels);
        recentorderrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recentorderrecycleview.setAdapter(orderHistoryAdapter);

        Log.d("Home_page", "Product list size: " + orderHistoryModels.size());

        view_all_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), AllResentOrderHistory.class);
                startActivity(in);
            }
        });

        return root;
    }

    private void OrderList() {
        orderHistoryModels.add(new HomeOrderHistoryModel("#15678", "Priya singh 25858695", "30/12/1999", "₹.200"));
        orderHistoryModels.add(new HomeOrderHistoryModel("#25679", "Ravi Kumar 12548896", "15/01/2000", "₹300"));
        orderHistoryModels.add(new HomeOrderHistoryModel("#35680", "Asha Rani 45671258", "20/02/2001", "₹400"));
        orderHistoryModels.add(new HomeOrderHistoryModel("#45681", "Rahul Verma 78945612", "25/03/2002", "₹500"));
        orderHistoryModels.add(new HomeOrderHistoryModel("#55682", "Anjali Roy 32147895", "30/04/2003", "₹600"));
        orderHistoryModels.add(new HomeOrderHistoryModel("#25679", "Ravi Kumar 12548896", "15/01/2000", "₹300"));
        orderHistoryModels.add(new HomeOrderHistoryModel("#35680", "Asha Rani 45671258", "20/02/2001", "₹400"));
        Log.d("Home_page", "Products added: " + orderHistoryModels.size());
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