package com.wits.grofast_vendor.Homepage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wits.grofast_vendor.Adapter.ProductlistAdapter;
import com.wits.grofast_vendor.Details.AddProduct;
import com.wits.grofast_vendor.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_Fragment extends Fragment {
    RecyclerView productrecycleview;
    AppCompatButton addproduct;
    private ProductlistAdapter productAdapter;
    private List<Map<String, Object>> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_, container, false);

        addproduct = root.findViewById(R.id.new_product_add);

        productrecycleview = root.findViewById(R.id.product_list);
        productrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        itemList = new ArrayList<>();
        loaddata();
        productAdapter = new ProductlistAdapter(getContext(), itemList);
        productrecycleview.setAdapter(productAdapter);

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), AddProduct.class);
                startActivity(in);
            }
        });

        return root;
    }

    private void loaddata() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("Name", "Ankleshwar");
        item1.put("description", "Your order from Fresh Picks Mart has been delivered. Feel free to tio the delivery partner.");
        item1.put("Price", "3");
        item1.put("Quantity", "3Kg");
        item1.put("image", R.drawable.gobhi_image);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("Name", "Ankleshwar");
        item2.put("description", "Your order from Fresh Picks Mart has been delivered. Feel free to tio the delivery partner.");
        item2.put("Price", "3");
        item2.put("Quantity", "3Kg");
        item2.put("image", R.drawable.gobhi_image);

        Map<String, Object> item3 = new HashMap<>();
        item3.put("Name", "Ankleshwar");
        item3.put("description", "Your order from Fresh Picks Mart has been delivered. Feel free to tio the delivery partner.");
        item3.put("Price", "3");
        item3.put("Quantity", "3Kg");
        item3.put("image", R.drawable.gobhi_image);

        Map<String, Object> item4 = new HashMap<>();
        item4.put("Name", "Ankleshwar");
        item4.put("description", "Your order from Fresh Picks Mart has been delivered. Feel free to tio the delivery partner.");
        item4.put("Price", "3");
        item4.put("Quantity", "3Kg");
        item4.put("image", R.drawable.gobhi_image);

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
    }
}

