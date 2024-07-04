package com.wits.grofast_vendor.Adapter;

import static com.wits.grofast_vendor.CommonUtilities.formatDate;
import static com.wits.grofast_vendor.CommonUtilities.getDateFromTimestamp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_vendor.Api.Interface.OrderInterface;
import com.wits.grofast_vendor.Api.Model.OrderItemModel;
import com.wits.grofast_vendor.Api.Model.OrderModel;
import com.wits.grofast_vendor.Api.Model.OrderStatusModel;
import com.wits.grofast_vendor.Api.Response.OrderStatusResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllResentOrderAdapter extends RecyclerView.Adapter<AllResentOrderAdapter.ViewHolder> {
    private List<OrderModel> orderList;
    private Context context;
    private final String TAG = "AllHistoryAdapter";
    private SupplierActivitySession supplierActivitySession;
    private static final Map<String, Integer> STATUS_MAP = new HashMap<String, Integer>() {{
        put("Processing", 1);
        put("Pending", 2);
        put("Rejected", 3);
        put("Delivered", 5);
    }};
    private static final String[] ALL_STATUSES = {"Processing", "Pending", "Delivered", "Rejected"};
    private int expandedPosition = -1;

    public AllResentOrderAdapter(Context context, List<OrderModel> orderList) {
        this.orderList = orderList;
        this.context = context;
    }


    @NonNull
    @Override
    public AllResentOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_resent_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllResentOrderAdapter.ViewHolder holder, int position) {
        supplierActivitySession = new SupplierActivitySession(context);
        OrderModel model = orderList.get(position);
        holder.order_id.setText("" + model.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.date.setText(getDateFromTimestamp(model.getCreated_at()));
        } else holder.date.setText(model.getCreated_at());
        holder.price.setText(model.getTotal_amount());

        //Order status
        OrderStatusModel orderStatus = model.getOrderStatus();
        holder.status.setText(orderStatus.getLabel());
        holder.status.setTextColor(Color.parseColor(orderStatus.getColor()));

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

        //View all details
        holder.all_detail_id.setText("" + model.getId());
        holder.all_detail_price.setText(model.getTotal_amount());

        holder.all_detail_status.setText(orderStatus.getLabel());
        holder.all_detail_status.setTextColor(Color.parseColor(orderStatus.getColor()));

        //Recycleview
        List<OrderItemModel> orderItems = model.getOrderItems();
        AllInnrerResentOrderAdapter allInnerHistoryAdapter = new AllInnrerResentOrderAdapter(context, orderItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(allInnerHistoryAdapter);
        holder.recyclerView.setTag(position);

        // Spinner setup
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, ALL_STATUSES);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.status_spinner.setAdapter(spinnerAdapter);
        holder.status_spinner.setSelection(getStatusPosition(orderStatus.getLabel()));

        // Add listener for spinner selection
        holder.status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(pos);
                int statusValue = STATUS_MAP.get(selectedStatus);
                updateOrderStatus(model.getId(), statusValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Delivery Date
        String DeliveryDate = formatDate(model.getDelivery_date(), "yyyy-MM-dd", "dd-MM-yy");
        if (model.getDelivery_date() != null && !model.getDelivery_date().isEmpty()) {
            holder.delivery_date_show.setText(DeliveryDate);
            holder.delivery_date_layout.setVisibility(View.VISIBLE);
            holder.delivery_date_add.setVisibility(View.GONE);
        } else {
            holder.delivery_date_add.setVisibility(View.VISIBLE);
            holder.delivery_date_layout.setVisibility(View.GONE);
            holder.delivery_date_add.setOnClickListener(v -> showDatePickerDialog(holder, model));
        }
        holder.delivery_date_change.setOnClickListener(v -> showDatePickerDialog(holder, model));
    }

    private void showDatePickerDialog(ViewHolder holder, OrderModel model) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
            String selectedDate = dateFormat.format(selectedDateCalendar.getTime());
            UpdateDeliverydate(model.getId(), selectedDate);
            holder.delivery_date_show.setText(selectedDate);
            holder.delivery_date_layout.setVisibility(View.VISIBLE);
            holder.delivery_date_add.setVisibility(View.GONE);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void UpdateDeliverydate(int orderId, String selectedDate) {
        Call<OrderStatusResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(OrderInterface.class).updateDeliveryDate(orderId, selectedDate);
        call.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse> response) {
                if (response.isSuccessful()) {
                    OrderStatusResponse orderResponse = response.body();
                    Log.e(TAG, "onResponse: Message : " + orderResponse.getMessage());
                    Log.e(TAG, "onResponse: status : " + orderResponse.getStatus());
                    Log.e(TAG, "onResponse: Delivery Date : " + orderResponse.getOrder().getDelivery_date());
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private int getStatusPosition(String statusLabel) {
        for (int i = 0; i < ALL_STATUSES.length; i++) {
            if (ALL_STATUSES[i].equalsIgnoreCase(statusLabel)) {
                return i;
            }
        }
        return 0;
    }

    private void updateOrderStatus(int orderId, int status) {
        Call<OrderStatusResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(OrderInterface.class).updateOrderStatus(orderId, status);
        call.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse> response) {
                if (response.isSuccessful()) {
                    OrderStatusResponse orderResponse = response.body();
                    Log.e(TAG, "onResponse: Message : " + orderResponse.getMessage());
                    Log.e(TAG, "onResponse: status : " + orderResponse.getStatus());
                    Log.e(TAG, "onResponse: order status : " + orderResponse.getOrder().getOrderStatus().getLabel());
                    Log.e(TAG, "onResponse: order value : " + orderResponse.getOrder().getOrderStatus().getValue());

                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id, date, status, price, all_detail_id, delivery_date_show, all_detail_price, all_detail_status;
        LinearLayout detailsView, detailshide, delivery_date_layout;
        RecyclerView recyclerView;
        AppCompatSpinner status_spinner;
        AppCompatButton delivery_date_add;
        ImageView delivery_date_change;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.resent_order_id);
            date = itemView.findViewById(R.id.resent_order_date);
            price = itemView.findViewById(R.id.resent_order_customer_price);
            status = itemView.findViewById(R.id.resent_order_status);
            status_spinner = itemView.findViewById(R.id.Update_order_status_Spinner);
            all_detail_id = itemView.findViewById(R.id.history_order_id);
            all_detail_status = itemView.findViewById(R.id.history_order_status);
            all_detail_price = itemView.findViewById(R.id.history_oredr_price);

            delivery_date_show = itemView.findViewById(R.id.delivery_date_show);
            delivery_date_add = itemView.findViewById(R.id.delivery_date_add);
            delivery_date_layout = itemView.findViewById(R.id.history_delivery_date_layout);
            delivery_date_change = itemView.findViewById(R.id.delivery_date_change);

            detailsView = itemView.findViewById(R.id.details_view);
            detailshide = itemView.findViewById(R.id.show_some_deatils);
            recyclerView = itemView.findViewById(R.id.history_product_inner_recycleview);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    public interface OnOrderStatusChangeListener {
        void onOrderStatusChanged();
    }
}
