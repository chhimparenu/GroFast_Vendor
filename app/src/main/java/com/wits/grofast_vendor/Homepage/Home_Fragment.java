package com.wits.grofast_vendor.Homepage;

import android.graphics.Color;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wits.grofast_vendor.Adapter.BannerAdapter;
import com.wits.grofast_vendor.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home_Fragment extends Fragment {
    RecyclerView bannerrecyclview;
    BannerAdapter bannerAdapter;
    private List<Integer> bannerImages;
    private int currentBannerPosition = 0;
    private Handler handler = new Handler();
    BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_, container, false);

        //Banner Recycleview
        bannerrecyclview = root.findViewById(R.id.banner_home_recycleview);
        bannerImages = Arrays.asList(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3);
        bannerAdapter = new BannerAdapter(getContext(), bannerImages);
        bannerrecyclview.setAdapter(bannerAdapter);
        bannerrecyclview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        startAutoScroll();

        //Bar chart
        barChart = root.findViewById(R.id.home_bar_chart);
        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014, 420));
        visitors.add(new BarEntry(2015, 450));
        visitors.add(new BarEntry(2016, 490));
        visitors.add(new BarEntry(2017, 520));
        visitors.add(new BarEntry(2018, 420));
        visitors.add(new BarEntry(2019, 450));
        visitors.add(new BarEntry(2020, 490));
        visitors.add(new BarEntry(2021, 320));
        BarDataSet barDataSet = new BarDataSet(visitors, "visitors");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData bardata = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(bardata);
        barChart.getDescription().setText("Bar chart");

        barChart.animateY(2000);

        return root;
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