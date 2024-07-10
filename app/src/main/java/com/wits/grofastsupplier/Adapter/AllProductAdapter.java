package com.wits.grofastsupplier.Adapter;

import static com.wits.grofastsupplier.CommonUtilities.getDateFromTimestamp;
import static com.wits.grofastsupplier.CommonUtilities.handleApiError;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofastsupplier.Api.Interface.ProductInterface;
import com.wits.grofastsupplier.Api.Model.ProductModel;
import com.wits.grofastsupplier.Api.Response.ProductResponse;
import com.wits.grofastsupplier.Api.Retrofirinstance;
import com.wits.grofastsupplier.Details.EditProduct;
import com.wits.grofastsupplier.R;
import com.wits.grofastsupplier.session.SupplierActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.listViewHolder> {
    private List<ProductModel> productList = new ArrayList<>();
    private Context context;
    private final String TAG = "AllProductAdapter";
    private SparseBooleanArray expandedItems;
    private int expandedPosition = -1;
    SupplierActivitySession supplierActivitySession;

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
        supplierActivitySession = new SupplierActivitySession(context);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.detaildate.setText(getDateFromTimestamp(item.getCreated_at()));
        } else holder.detaildate.setText(item.getCreated_at());
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

        holder.delete.setOnClickListener(v -> {
            DeleteProductConfirmation(item.getUuid());
        });
    }

    private void DeleteProductConfirmation(String uuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogButtonsView = LayoutInflater.from(context).inflate(R.layout.confirmationdelete, null);
        builder.setView(dialogButtonsView);

        // Find the buttons in the custom layout
        TextView title = dialogButtonsView.findViewById(R.id.delete_confirmation_title);
        TextView msg = dialogButtonsView.findViewById(R.id.delete_confirmation_msg);
        Button btnNo = dialogButtonsView.findViewById(R.id.btnNo);
        Button btnYes = dialogButtonsView.findViewById(R.id.btnYes);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        title.setText(R.string.delete_product_confirmation);
        msg.setText(R.string.are_you_sure_delete_product);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(uuid);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void deleteProduct(String uuid) {
        Call<ProductResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(ProductInterface.class).deleteProduct(uuid);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    Log.e(TAG, "onResponse: status : " + productResponse.getStatus());
                    Log.e(TAG, "onResponse: Message : " + productResponse.getMessage());
                    Log.e(TAG, "onResponse: uuid : " + uuid);
                    showCustomeDialog(productResponse.getMessage());

                    // Remove the deleted product from the list
                    int position = getProductPositionByUuid(uuid);
                    if (position != -1) {
                        productList.remove(position);
                        notifyItemRemoved(position);
                    }
                }else if (response.code() == 422) {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Gson gson = new Gson();
                        JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                        String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                        showCustomeDialog(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    handleApiError(TAG, response, context);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showCustomeDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogButtonsView = LayoutInflater.from(context).inflate(R.layout.confirmationdelete, null);
        builder.setView(dialogButtonsView);

        // Find the buttons in the custom layout
        TextView title = dialogButtonsView.findViewById(R.id.delete_confirmation_title);
        TextView msg = dialogButtonsView.findViewById(R.id.delete_confirmation_msg);
        Button btnNo = dialogButtonsView.findViewById(R.id.btnNo);
        Button btnYes = dialogButtonsView.findViewById(R.id.btnYes);
        Button btnOkay = dialogButtonsView.findViewById(R.id.btnOkay);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        title.setText(R.string.product_delete_title);
        msg.setText(message);
        btnNo.setVisibility(View.GONE);
        btnYes.setVisibility(View.GONE);
        btnOkay.setVisibility(View.VISIBLE);

        btnOkay.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private int getProductPositionByUuid(String uuid) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    private void editproduct(ProductModel item) {
        Intent intent = new Intent(context, EditProduct.class);
        intent.putExtra("Product_Item", item);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
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