package com.wits.grofast_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.R;
import java.util.List;
import java.util.Map;

public class AllResentOrderAdapter extends RecyclerView.Adapter<AllResentOrderAdapter.ViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    private int expandedPosition = -1;
    public AllResentOrderAdapter(Context context, List<Map<String, Object>> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AllResentOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_resent_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllResentOrderAdapter.ViewHolder holder, int position) {
        Map<String, Object> item = list.get(position);
        holder.order_id.setText((String) item.get("Order id"));
        holder.customername.setText((String) item.get("Customer name"));
        holder.phonenumber.setText((String) item.get("Phone number"));
        holder.date.setText((String) item.get("Date"));
        holder.price.setText((String) item.get("Price"));

        final boolean isExpanded = position == expandedPosition;
        holder.detailsView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.detailshide.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(v -> {
            int previousExpandedPosition = expandedPosition;
            if (expandedPosition == position) {
                expandedPosition = -1;
            } else {
                expandedPosition = position;
            }
            notifyItemChanged(previousExpandedPosition);
            notifyItemChanged(expandedPosition);
        });

        //Recycleview
        List<Map<String, Object>> innerItems = (List<Map<String, Object>>) item.get("InnerData");
        AllInnrerResentOrderAdapter allInnerHistoryAdapter = new AllInnrerResentOrderAdapter(context, innerItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(allInnerHistoryAdapter);
        holder.recyclerView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id, customername, phonenumber, date, price;
        LinearLayout detailsView, detailshide;
        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.resent_order_id);
            customername = itemView.findViewById(R.id.resent_order_customer_name);
            phonenumber = itemView.findViewById(R.id.resent_order_customer_phone);
            date = itemView.findViewById(R.id.resent_order_date);
            price = itemView.findViewById(R.id.resent_order_customer_price);
            detailsView = itemView.findViewById(R.id.details_view);
            detailshide = itemView.findViewById(R.id.show_some_deatils);
            recyclerView = itemView.findViewById(R.id.history_product_inner_recycleview);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

        }
    }
}
