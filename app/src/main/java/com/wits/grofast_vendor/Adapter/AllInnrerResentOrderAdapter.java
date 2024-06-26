package com.wits.grofast_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.R;

import java.util.List;
import java.util.Map;

public class AllInnrerResentOrderAdapter extends RecyclerView.Adapter<AllInnrerResentOrderAdapter.MyViewHolder>{

    private List<Map<String, Object>> historyinnerItem;
    private Context context;

    public AllInnrerResentOrderAdapter(Context context, List<Map<String, Object>> historyinnerItem) {
        this.context = context;
        this.historyinnerItem = historyinnerItem;
    }
    @NonNull
    @Override
    public AllInnrerResentOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_vendor_product, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull AllInnrerResentOrderAdapter.MyViewHolder holder, int position) {
        Map<String, Object> item = historyinnerItem.get(position);
        holder.quantity.setText((String) item.get("Quantity"));
        holder.productname.setText((String) item.get("Product_Name"));
        holder.price.setText((String) item.get("Price"));
        holder.image.setImageResource((int) item.get("image"));
    }
    @Override
    public int getItemCount() {
        return historyinnerItem.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView quantity, productname,price;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.inner_product_quantity);
            productname = itemView.findViewById(R.id.inner_product_name);
            image = itemView.findViewById(R.id.inner_product_image);
            price = itemView.findViewById(R.id.inner_product_price);
        }
    }
}
