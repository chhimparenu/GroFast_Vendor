package com.wits.grofast_vendor.Adapter;

import static com.wits.grofast_vendor.Api.Retrofirinstance.domain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofast_vendor.Api.Model.ProductModel;
import com.wits.grofast_vendor.Details.EditProduct;
import com.wits.grofast_vendor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.listViewHolder> {
    private List<ProductModel> productList = new ArrayList<>();
    private Context context;
    private final String TAG = "AllProductAdapter";
    private SparseBooleanArray expandedItems;
    private int expandedPosition = -1;

    public AllProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        this.expandedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public AllProductAdapter.listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new listViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductAdapter.listViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductModel item = productList.get(position);
        holder.id.setText(item.getId().toString());
        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());
        if (item.getProductStatus() != null) {
            holder.status.setText(item.getProductStatus().getLabel());
            try {
                holder.status.setTextColor(Color.parseColor(item.getProductStatus().getColor()));
            } catch (IllegalArgumentException e) {
                holder.status.setTextColor(context.getResources().getColor(R.color.default_color));
            }
        }
        Glide.with(context).load(item.getImage()).placeholder(R.drawable.add_product).into(holder.imageView);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editproduct(item);
            }
        });

        //All details

        holder.detailid.setText("" + item.getId().toString());
        holder.detailname.setText(item.getName());
        holder.detailprice.setText(item.getPrice());
        if (item.getProductStatus() != null) {
            holder.detailstatus.setText(item.getProductStatus().getLabel());
            try {
                holder.detailstatus.setTextColor(Color.parseColor(item.getProductStatus().getColor()));
            } catch (IllegalArgumentException e) {
                holder.detailstatus.setTextColor(context.getResources().getColor(R.color.default_color));
            }
        }
        holder.detaildate.setText(item.getCreated_at());
        holder.detailcategories.setText(item.getCategory_name());
        holder.detailTax.setText(item.getTax_id());
        holder.detailper.setText(item.getPer());
        holder.detailunit.setText(item.getUnit_id());
        holder.detaildiscount.setText(item.getDiscount());
        holder.detaildetail.setText(item.getProduct_detail());

        if (item.getStock_status() == 1) {
            holder.detailstock.setText("In Stock");
        } else {
            holder.detailstock.setText("Out Stock");
        }

        if (item.getReturn_policy() == 1) {
            holder.detailreturn.setText("True");
        } else {
            holder.detailreturn.setText("False");
        }

        Glide.with(context).load(item.getImage()).placeholder(R.drawable.add_product).into(holder.detailimage);

        Log.d(TAG, "Return " + item.getReturn_policy());
        Log.d(TAG, "Image " + item.getImage());


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
    }

    private void editproduct(ProductModel item) {
        Intent intent = new Intent(context, EditProduct.class);
        intent.putExtra("Product_Item", item);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
//        Log.e("TAG", "getItemCount: size " + productList.size());
        return productList.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name, price, status, detailid, detailname, detailcategories, detailprice, detailstatus, detaildate, detailTax, detailper, detailunit, detaildiscount, detaildetail, detailreturn, detailstock;
        ImageView imageView, detailimage;
        LinearLayout detailsView, detailshide;
        AppCompatButton edit, delete;

        public listViewHolder(@NonNull View v) {
            super(v);
            imageView = v.findViewById(R.id.pro_image);
            id = v.findViewById(R.id.pro_product_id);
            name = v.findViewById(R.id.proname);
            price = v.findViewById(R.id.proprice);
            status = v.findViewById(R.id.pro_status);
            detailid = v.findViewById(R.id.all_detail_product_id);
            detailname = v.findViewById(R.id.all_detail_product_name);
            detailcategories = v.findViewById(R.id.all_detail_product_categories);
            detailprice = v.findViewById(R.id.all_detail_product_price);
            detailstatus = v.findViewById(R.id.all_detail_product_status);
            detaildate = v.findViewById(R.id.all_detail_product_date);
            detailimage = v.findViewById(R.id.all_detail_prodict_image);
            detailsView = v.findViewById(R.id.details_view);
            detailshide = v.findViewById(R.id.show_some_deatils);
            detailTax = v.findViewById(R.id.all_detail_product_tax);
            detailper = v.findViewById(R.id.all_detail_product_per);
            detailunit = v.findViewById(R.id.all_detail_product_unit);
            detaildiscount = v.findViewById(R.id.all_detail_product_discount);
            detaildetail = v.findViewById(R.id.all_detail_product_detail);
            detailreturn = v.findViewById(R.id.all_detail_product_return);
            detailstock = v.findViewById(R.id.all_detail_product_stock);
            edit = v.findViewById(R.id.edit_product);
            delete = v.findViewById(R.id.delete_product);
        }
    }
}