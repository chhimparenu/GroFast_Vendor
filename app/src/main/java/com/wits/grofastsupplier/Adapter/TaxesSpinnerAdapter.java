package com.wits.grofastsupplier.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.wits.grofastsupplier.Api.Model.TaxModel;
import com.wits.grofastsupplier.R;

import java.util.List;

public class TaxesSpinnerAdapter extends ArrayAdapter<TaxModel> {
    private final LayoutInflater mInflater;
    private final List<TaxModel> taxModelList;
    private final Context mContext;
    private final int mResource;

    public TaxesSpinnerAdapter(@NonNull Context context, @NonNull List<TaxModel> objects) {
        super(context, R.layout.spinner_layout, objects);
        this.mInflater = LayoutInflater.from(context);
        this.taxModelList = objects;
        this.mContext = context;
        this.mResource = R.layout.spinner_layout;
    }

    @Override
    public int getCount() {
        return taxModelList.size()+1;
    }

    @Override
    public TaxModel getItem(int position) {
        // Return null if the position is the first item (hint)
        return position == 0 ? null : taxModelList.get(position - 1);
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
        textView.setText(taxModelList.get(position - 1).getName());
        return view;
    }

    private View getHintView(ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView textView = view.findViewById(R.id.text1);
        textView.setText(R.string.select_tax);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.default_color));
        textView.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        return view;
    }
}
