package com.reputasi.callblocker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.database.record.SpammerNumberItem;

import java.util.List;

/**
 * Created by vikraa on 6/6/2015.
 */
public class SpammerAdapter extends ArrayAdapter<SpammerNumberItem> {
    private List<SpammerNumberItem> mValues;
    private Context mContext;

    public SpammerAdapter(Context context, List<SpammerNumberItem> values) {
        super(context, R.layout.adapter_spammers);
        mContext = context;
        mValues = values;
    }

    @Override
    public SpammerNumberItem getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(GlobalApplication.getContext()).inflate(R.layout.adapter_spammers, null);
            holder = new ViewHolder();
            holder.mInitial = (ReputasiTextView)v.findViewById(R.id.tv_initial);
            holder.mName = (ReputasiTextView)v.findViewById(R.id.tv_title);
            holder.mNumber = (ReputasiTextView)v.findViewById(R.id.tv_number);
            holder.mCategoryName = (ReputasiTextView)v.findViewById(R.id.tv_category_info);
            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }

        //set values
        holder.mName.setText(mValues.get(position).getSpammerName());
        String[] nameSplit = mValues.get(position).getSpammerName().split(" ");
        String initial = "";
        for (String i : nameSplit) {
            initial += i.substring(0).toUpperCase();
        }
        holder.mInitial.setText(initial.length() == 1 ? initial.toUpperCase() : initial.substring(0,initial.length()).toUpperCase() );
        holder.mNumber.setText(mValues.get(position).getSpammerNumber());
        holder.mCategoryName.setText(mValues.get(position).getSpammerCategoryNumberName());
        return v;
    }

    private static class ViewHolder {
        public ReputasiTextView mInitial, mName, mNumber, mCategoryName;
    }
}
