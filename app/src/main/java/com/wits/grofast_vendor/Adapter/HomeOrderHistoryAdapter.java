package com.wits.grofast_vendor.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.Api.Model.HomeOrderHistoryModel;
import com.wits.grofast_vendor.R;

import java.util.ArrayList;

public class HomeOrderHistoryAdapter extends RecyclerView.Adapter<HomeOrderHistoryAdapter.ViewHolders> {

    private Context context;
    private ArrayList<HomeOrderHistoryModel> List;

    public HomeOrderHistoryAdapter(Context context, ArrayList<HomeOrderHistoryModel> List) {
        this.context = context;
        this.List = List;
    }

    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeOrderHistoryAdapter.ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders holder, int position) {
        HomeOrderHistoryModel model = List.get(position);

        holder.mSNo.setText(model.getsNo());
        holder.mProduct.setText(model.getProduct());
        holder.mCategory.setText(model.getCategory());
        holder.mPrice.setText(model.getPrice());

    }

    @Override
    public int getItemCount() {
        Log.e("OrderHistoryAdapter", "getItemCount: size " + List.size());
        return List.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView mSNo, mProduct, mCategory, mPrice;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);

            mSNo = itemView.findViewById(R.id.sNo);
            mProduct = itemView.findViewById(R.id.product);
            mCategory = itemView.findViewById(R.id.category);
            mPrice = itemView.findViewById(R.id.price);
        }
    }
}
