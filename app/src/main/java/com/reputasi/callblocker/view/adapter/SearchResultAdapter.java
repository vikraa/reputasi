package com.reputasi.callblocker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.database.record.SearchResultItem;

import java.util.List;

/**
 * Created by vikraa on 8/1/2015.
 */
public class SearchResultAdapter extends ArrayAdapter<SearchResultItem> {
    private List<SearchResultItem> mValues;

    public SearchResultAdapter(List<SearchResultItem> values) {
        super(GlobalApplication.getContext(), R.layout.adapter_search_result);
        mValues = values;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            holder = new ViewHolder();
            v = LayoutInflater.from(GlobalApplication.getContext()).inflate(R.layout.adapter_search_result, null);
            holder.mCategoryName = (ReputasiTextView)v.findViewById(R.id.tv_category_name);
            holder.mOwnerName = (ReputasiTextView)v.findViewById(R.id.tv_caller_name);
            holder.mPhoneNumber = (ReputasiTextView)v.findViewById(R.id.tv_caller_category);
            holder.mNumberScore = (ReputasiTextView)v.findViewById(R.id.tv_caller_score);
            holder.mThumbUpScore = (ReputasiTextView)v.findViewById(R.id.tv_caller_thumbup_score);
            holder.mThumbDownScore = (ReputasiTextView)v.findViewById(R.id.tv_caller_thumbdown_score);
            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }
        holder.mCategoryName.setText(mValues.get(position).getCategory());
        holder.mOwnerName.setText(mValues.get(position).getOwnerName());
        holder.mPhoneNumber.setText(mValues.get(position).getPhoneNumber());
        holder.mNumberScore.setText(mValues.get(position).getScore());
        holder.mThumbUpScore.setText(mValues.get(position).getThumbUpScore());
        holder.mThumbDownScore.setText(mValues.get(position).getThumbDownScore());
        return v;
    }

    private static class ViewHolder {
        ReputasiTextView mOwnerName, mPhoneNumber, mNumberScore, mThumbUpScore, mThumbDownScore, mCategoryName;
    }

}
