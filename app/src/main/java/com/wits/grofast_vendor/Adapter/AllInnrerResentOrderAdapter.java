package com.wits.grofast_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofast_vendor.Api.Model.OrderItemModel;
import com.wits.grofast_vendor.Api.Model.ProductModel;
import com.wits.grofast_vendor.R;

import java.util.List;
import java.util.Map;

public class AllInnrerResentOrderAdapter extends RecyclerView.Adapter<AllInnrerResentOrderAdapter.MyViewHolder> {

    private List<OrderItemModel> orderItems;
    private Context context;

    public AllInnrerResentOrderAdapter(Context context, List<OrderItemModel> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public AllInnrerResentOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_vendor_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllInnrerResentOrderAdapter.MyViewHolder holder, int position) {
        OrderItemModel item = orderItems.get(position);
        holder.quantity.setText("" + item.getQuantity());
        ProductModel product = item.getProduct();

        if (product != null) {
            holder.productname.setText("" + product.getName());
            Glide.with(context).load(product.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);
        }
        holder.price.setText(""+item.getSubtotal());
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView quantity, productname, price;
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
