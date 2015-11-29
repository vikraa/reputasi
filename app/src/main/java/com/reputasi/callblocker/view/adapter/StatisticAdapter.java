package com.reputasi.callblocker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.rest.response.Statistic;

import java.util.List;

/**
 * Created by vikraa on 6/13/2015.
 */
public class StatisticAdapter extends ArrayAdapter<Statistic> {
    List<Statistic> mValues;
    Context mContext;

    public StatisticAdapter(Context context, List<Statistic> values) {
        super(context, android.R.layout.simple_list_item_1);
        mContext = context;
        mValues = values;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = LayoutInflater.from(mContext).inflate(R.layout.row_statistic, null);
            holder.mName = (ReputasiTextView)v.findViewById(R.id.tv_legend_title);
            holder.mValue = (ReputasiTextView)v.findViewById(R.id.tv_legend_info);
            holder.mIcon = (ImageView)v.findViewById(R.id.iv_legend_icon);
            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }

        holder.mName.setText(mValues.get(position).getLegendName());
        holder.mValue.setText(""+mValues.get(position).getValue());
        holder.mIcon.setImageResource(R.drawable.pin);
        holder.mIcon.setVisibility(View.INVISIBLE);

        return v;//super.getView(position, convertView, parent);
    }

    @Override
    public Statistic getItem(int position) {
        return mValues.get(position);
    }

    private static class ViewHolder {
        public ImageView mIcon;
        public ReputasiTextView mName, mValue;
    }
}
