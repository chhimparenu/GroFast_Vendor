package com.wits.grofast_vendor.Details;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.Adapter.EarningAdapter;
import com.wits.grofast_vendor.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarningPage extends AppCompatActivity {
    RecyclerView recyclerView;
    EarningAdapter earningAdapter;
    LinearLayoutManager linearLayoutManager;
    List<Map<String, Object>> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.earning_page));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        setContentView(R.layout.activity_earning_page);

        //Resent Cart Item
        recyclerView = findViewById(R.id.total_earning_recycleview);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        item = new ArrayList<>();
        loadItem();
        earningAdapter = new EarningAdapter(getApplicationContext(), item);
        recyclerView.setAdapter(earningAdapter);


    }

    private void loadItem() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("Id", "5");
        item1.put("Amount", "4");

        Map<String, Object> item2 = new HashMap<>();
        item2.put("Id", "50");
        item2.put("Amount", "4");

        item.add(item1);
        item.add(item2);
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