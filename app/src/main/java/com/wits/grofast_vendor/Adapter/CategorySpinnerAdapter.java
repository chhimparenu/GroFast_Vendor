package com.wits.grofast_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wits.grofast_vendor.Api.Model.CategoryModel;
import com.wits.grofast_vendor.R;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryModel> {
    private final LayoutInflater mInflater;
    private final List<CategoryModel> categoryList;
    private final Context mContext;
    private final int mResource;

    public CategorySpinnerAdapter(@NonNull Context context, @NonNull List<CategoryModel> objects) {
        super(context, R.layout.spinner_layout, objects);
        this.mInflater = LayoutInflater.from(context);
        this.categoryList = objects;
        this.mContext = context;
        this.mResource = R.layout.spinner_layout;
    }

    @Override
    public int getCount() {
        return categoryList.size()+1;
    }

    @Override
    public CategoryModel getItem(int position) {
        // Return null if the position is the first item (hint)
        return position == 0 ? null : categoryList.get(position - 1);
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
        textView.setText(categoryList.get(position - 1).getCategory_name());
        return view;
    }

    private View getHintView(ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView textView = view.findViewById(R.id.text1);
        textView.setText(R.string.select_category);
        textView.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        return view;
    }
}