package com.wits.grofastsupplier.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wits.grofastsupplier.Api.Model.SpinnerModel;
import com.wits.grofastsupplier.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerModel> {
    private final LayoutInflater mInflater;
    List<SpinnerModel> spinnerItemList = new ArrayList<>();
    private final Context mContext;
    private final int mResource;
    private final String hintMessage;

    public CustomSpinnerAdapter(@NonNull Context context, @NonNull List<SpinnerModel> spinnerItemList, String hint) {
        super(context, R.layout.spinner_layout, spinnerItemList);
        this.mInflater = LayoutInflater.from(context);
        this.spinnerItemList = spinnerItemList;
        this.mContext = context;
        this.mResource = R.layout.spinner_layout;
        hintMessage = hint;
    }

    @Override
    public int getCount() {
        return spinnerItemList.size() + 1;
    }

    @Override
    public SpinnerModel getItem(int position) {
        // Return null if the position is the first item (hint)
        return position == 0 ? null : spinnerItemList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            // Hint for the main spinner view
            return getHintView(parent);
        } else {
            return getCustomView(position, convertView, parent);
        }
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            // Hint for the dropdown view
            return new View(mContext);
        } else {
            return getCustomView(position, convertView, parent);
        }
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView textView = view.findViewById(R.id.text1);

        if (!spinnerItemList.isEmpty()) {
            textView.setText(spinnerItemList.get(position-1).getName());
        }

        return view;
    }

    private View getHintView(ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView textView = view.findViewById(R.id.text1);
        textView.setText(hintMessage);
        textView.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        return view;
    }
}