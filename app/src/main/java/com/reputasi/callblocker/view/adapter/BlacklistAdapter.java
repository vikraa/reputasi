package com.reputasi.callblocker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reputasi.callblocker.R;
import com.reputasi.library.database.record.BlackListItem;

import java.util.List;

/**
 * Created by vikraa on 2/14/2015.
 */
public class BlacklistAdapter extends ArrayAdapter<BlackListItem> {

    private Context mContext;
    private List<BlackListItem> mValues;

    public BlacklistAdapter(Context context, List<BlackListItem> values) {
        super(context, R.layout.adapter_blocklist);
        mContext = context;
        mValues = values;
    }

    public void setValues(List<BlackListItem> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public View getView(final int row, View convertView, ViewGroup parent) {
        View v;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            v = ((Activity)mContext).getLayoutInflater().inflate(R.layout.adapter_blocklist, null);
            holder.mTvNumber = (TextView)v.findViewById(R.id.tv_number);
            holder.mTvName = (TextView)v.findViewById(R.id.tv_name);
            holder.mTvCategory = (TextView)v.findViewById(R.id.tv_category);
            v.setTag(holder);
        } else {
            v = convertView;
            holder = (ViewHolder)v.getTag();
        }


        holder.mTvNumber.setText(mValues.get(row).getNumber());
        holder.mTvName.setText(mValues.get(row).getName());
        holder.mTvCategory.setText(mValues.get(row).getCategorNumberName());
        return v;
    }

    @Override
    public BlackListItem getItem(int position) {
        return mValues.get(position);
    }

    private static class ViewHolder {
        TextView mTvNumber, mTvName, mTvCategory;
        ImageView mIvImage;
        LinearLayout mSurfaceView, mDeleteView;
    }

}
