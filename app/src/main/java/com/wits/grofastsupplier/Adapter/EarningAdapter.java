package com.wits.grofastsupplier.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofastsupplier.Api.Model.OrderModel;
import com.wits.grofastsupplier.R;
import com.wits.grofastsupplier.session.SupplierActivitySession;

import java.util.List;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.ViewHolder> {
    private List<OrderModel> earningitem;
    private Context context;

    private final String TAG = "AllHistoryAdapter";
    private SupplierActivitySession supplierActivitySession;

    public EarningAdapter(Context context,List<OrderModel> earningitem) {
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
        supplierActivitySession = new SupplierActivitySession(context);
        OrderModel model = earningitem.get(position);
        holder.oredr_id.setText("" + model.getId());
        holder.order_amount.setText("" + model.getTotal_amount());
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
