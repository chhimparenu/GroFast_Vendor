package com.wits.grofast_vendor.Homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.Adapter.AllResentOrderAdapter;
import com.wits.grofast_vendor.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order_Fragment extends Fragment {
    RecyclerView recyclerView;
    AllResentOrderAdapter resentOrderAdapter;
    List<Map<String, Object>> resentorderlist;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.Resent_order_history));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        recyclerView = view.findViewById(R.id.all_resent_order_recycleview);
        resentorderlist = new ArrayList<>();
        loadResetOredrItem();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        resentOrderAdapter = new AllResentOrderAdapter(getContext(), resentorderlist);
        recyclerView.setAdapter(resentOrderAdapter);

        return view;
    }

    private void loadResetOredrItem() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("Order id", "1258");
        item1.put("Customer name", "priya");
        item1.put("Phone number", "2589632514");
        item1.put("Date", transformDate("30 MARCH 2024"));
        item1.put("Price", "2000");

        // Inner item 1
        List<Map<String, Object>> innerData1 = new ArrayList<>();
        Map<String, Object> innerItem1 = new HashMap<>();
        innerItem1.put("Quantity", "3");
        innerItem1.put("Product_Name", "Apple");
        innerItem1.put("Price", "20");
        innerItem1.put("image", R.drawable.gobhi_image);
        innerData1.add(innerItem1);

        Map<String, Object> innerItem2 = new HashMap<>();
        innerItem2.put("Quantity", "4");
        innerItem2.put("Product_Name", "Banana");
        innerItem2.put("Price", "20");
        innerItem2.put("image", R.drawable.gobhi_image);
        innerData1.add(innerItem2);

        Map<String, Object> innerItem3 = new HashMap<>();
        innerItem3.put("Quantity", "5");
        innerItem3.put("Product_Name", "Mango apple banana ");
        innerItem3.put("Price", "20");
        innerItem3.put("image", R.drawable.gobhi_image);
        innerData1.add(innerItem3);

        item1.put("InnerData", innerData1);

        // Other Inner Items
        List<Map<String, Object>> inData1 = new ArrayList<>();
        Map<String, Object> otherInnerItem1 = new HashMap<>();
        otherInnerItem1.put("Name", "Apple");
        otherInnerItem1.put("SubName", "4");
        inData1.add(otherInnerItem1);

        Map<String, Object> otherInnerItem2 = new HashMap<>();
        otherInnerItem2.put("Name", "Apple");
        otherInnerItem2.put("SubName", "3");
        inData1.add(otherInnerItem2);

        Map<String, Object> otherInnerItem3 = new HashMap<>();
        otherInnerItem3.put("Name", "Apple");
        otherInnerItem3.put("SubName", "2");
        inData1.add(otherInnerItem3);

        Map<String, Object> otherInnerItem4 = new HashMap<>();
        otherInnerItem4.put("Name", "Apple");
        otherInnerItem4.put("SubName", "0");
        inData1.add(otherInnerItem4);

        item1.put("OtherInnerData", inData1);

        // Item 2
        Map<String, Object> item2 = new HashMap<>();
        item2.put("Order id", "1288");
        item2.put("Customer name", "Riya");
        item2.put("Phone number", "9638251444");
        item2.put("Date", transformDate("30 MARCH 2024"));
        item2.put("Price", "2000");

        List<Map<String, Object>> innerData2 = new ArrayList<>();
        Map<String, Object> innerItem5 = new HashMap<>();
        innerItem5.put("Quantity", "6");
        innerItem5.put("Product_Name", "Orange hena khana hai");
        innerItem5.put("Price", "20");
        innerItem5.put("image", R.drawable.gobhi_image);
        innerData2.add(innerItem2);
        item2.put("InnerData", innerData2);

        // Item 3
        Map<String, Object> item3 = new HashMap<>();
        item3.put("Order id", "1258");
        item3.put("Customer name", "priya");
        item3.put("Phone number", "2589632514");
        item3.put("Date", transformDate("30 MARCH 2024"));
        item3.put("Price", "2000");

        List<Map<String, Object>> innerData3 = new ArrayList<>();
        Map<String, Object> innerItem10 = new HashMap<>();
        innerItem10.put("Quantity", "1");
        innerItem10.put("Product_Name", "Pizza");
        innerItem10.put("Price", "20");
        innerItem10.put("image", R.drawable.gobhi_image);
        innerData3.add(innerItem3);
        item3.put("InnerData", innerData3);

        resentorderlist.add(item1);
        resentorderlist.add(item2);
        resentorderlist.add(item3);
    }

    private String transformDate(String date) {
        String[] parts = date.split(" ");

        String part1 = parts[0]; // "DAY"
        String part2 = parts[1]; // "MONTH"
        String part3 = parts[2]; // "YEAR"

        // Simulate 10 dp space with newlines (approximation)
        String spacing1 = "\n\n";

        // Simulate 2 dp space with a single space (approximation)
        String spacing2 = "\n\n";

        return part1 + spacing1 + part2 + spacing2 + part3;
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
