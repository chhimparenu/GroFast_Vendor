package com.wits.grofast_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.R;

import java.util.List;
import java.util.Map;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.ViewHolder> {
    private List<Map<String, Object>> earningitem;
    private Context context;

    public EarningAdapter(Context context, List<Map<String, Object>> earningitem) {
        this.context = context;
        this.earningitem = earningitem;
    }

    @NonNull
    @Override
    public EarningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.earning_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EarningAdapter.ViewHolder holder, int position) {
        Map<String, Object> item = earningitem.get(position);
        holder.oredr_id.setText((String) item.get("Id"));
        holder.order_amount.setText((String) item.get("Amount"));
    }

    @Override
    public int getItemCount() {
        return earningitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView oredr_id, order_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            oredr_id = itemView.findViewById(R.id.earning_order_id);
            order_amount = itemView.findViewById(R.id.earning_order_amount);
        }
    }
}
