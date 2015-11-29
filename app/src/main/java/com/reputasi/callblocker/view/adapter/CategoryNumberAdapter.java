package com.reputasi.callblocker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.database.record.CategoryNumberItem;

import java.util.List;

/**
 * Created by vikraa on 6/21/2015.
 */
public class CategoryNumberAdapter extends ArrayAdapter <CategoryNumberItem> {
    private Context mContext;
    private List<CategoryNumberItem> mValues;

    public CategoryNumberAdapter(Context context, List<CategoryNumberItem> values) {
        super(context, R.layout.adapter_spinner_category);
        mContext = context;
        mValues = values;
    }

    @Override
    public CategoryNumberItem getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_spinner_category, null);
        }
        ((ReputasiTextView)convertView.findViewById(R.id.tv_item)).setText(mValues.get(position).getCategoryName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_spinner_category, null);
        }
        ((ReputasiTextView)convertView.findViewById(R.id.tv_item)).setText(mValues.get(position).getCategoryName());
        return convertView;
    }
}
