package com.reputasi.callblocker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.database.record.CategoryNumberItem;

import java.util.List;

/**
 * Created by vikraa on 5/10/2015.
 */
public class BlockCategoryAdapter extends ArrayAdapter<CategoryNumberItem> {
    private Context mContext;
    private List<CategoryNumberItem> mValues;
    public BlockCategoryAdapter(Context context, List<CategoryNumberItem> values) {
        super(context, R.layout.adapter_block_category);
        mContext = context;
        mValues = values;
    }

    public void setValues(List<CategoryNumberItem> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public CategoryNumberItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v;
        if (convertView == null) {
            v = ((Activity)mContext).getLayoutInflater().inflate(R.layout.adapter_block_category, null);
            holder = new ViewHolder();
            holder.mTitle = (ReputasiTextView)v.findViewById(R.id.tv_category_title);
            holder.mInfo = (ReputasiTextView)v.findViewById(R.id.tv_category_info);
            v.setTag(holder);
        } else {
            v = convertView;
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mTitle.setText(mValues.get(position).getCategoryName());
        holder.mInfo.setText(mValues.get(position).getRegisteredCount() + " Registered");
        return v;
    }


    private static class ViewHolder {
        public ReputasiTextView mTitle, mInfo;
    }
}
