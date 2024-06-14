package com.wits.grofast_vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.R;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductlistAdapter extends RecyclerView.Adapter<ProductlistAdapter.listViewHolder> {

    private Context context;
    private List<Map<String, Object>> itemList;

    public ProductlistAdapter(Context context, List<Map<String, Object>> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ProductlistAdapter.listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new listViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductlistAdapter.listViewHolder holder, int position) {
        Map<String, Object> item = itemList.get(position);
        holder.name.setText((String) item.get("Name"));
        holder.description.setText((String) item.get("description"));
        holder.price.setText((String) item.get("Price"));
        holder.quantitiy.setText((String) item.get("Quantity"));
        holder.image.setImageResource((int) item.get("image"));

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position);
            }
        });
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.product_edit, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_edit) {
                    editItem(position);
                    return true;
                } else if (id == R.id.menu_delete) {
//                    deleteItem(position);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void editItem(int position) {
//        Map<String, Object> item = itemList.get(position);
//        Intent intent = new Intent(context, AddProduct.class);
//        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: size " + itemList.size());
        return itemList.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price, quantitiy;
        ImageView icon;
        CircleImageView image;

        public listViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.proname);
            description = v.findViewById(R.id.prodescription);
            price = v.findViewById(R.id.proprice);
            quantitiy = v.findViewById(R.id.proquantiy);
            icon = v.findViewById(R.id.icon);
            image = v.findViewById(R.id.proimage);
        }
    }
}