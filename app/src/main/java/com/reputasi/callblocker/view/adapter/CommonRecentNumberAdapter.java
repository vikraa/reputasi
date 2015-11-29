package com.reputasi.callblocker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.callblocker.view.utilities.AppConstant;
import com.reputasi.library.database.record.ContactBookItem;
import com.reputasi.library.database.record.RecentNumberItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by vikraa on 7/4/2015.
 */
public class CommonRecentNumberAdapter extends ArrayAdapter<RecentNumberItem> {
    private List<RecentNumberItem> mValues;
    private Context mContext;
    private FragmentPagerListener mListener;
    private boolean mEnableThumb;

    public CommonRecentNumberAdapter(List<RecentNumberItem> values) {
        super(GlobalApplication.getContext(), R.layout.adapter_internal_item);
        mValues = values;
        mContext = GlobalApplication.getContext();
    }

    public void setListener(FragmentPagerListener listener){
        mListener = listener;
    }

    public void setEnableThumb(boolean enableThumb) {
        mEnableThumb = enableThumb;
    }

    @Override
    public RecentNumberItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        ViewHolder viewHolder;

        if (convertView == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.adapter_internal_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTvTitle = (ReputasiTextView)v.findViewById(R.id.tv_phone_number);
            viewHolder.mTvInfo = (ReputasiTextView)v.findViewById(R.id.tv_phone_info);
            v.setTag(viewHolder);
            viewHolder.mThumbUp = (ImageView)v.findViewById(R.id.iv_thumbs_up);
            viewHolder.mThumbDown = (ImageView)v.findViewById(R.id.iv_thumbs_down);
        } else {
            v = convertView;
            viewHolder = (ViewHolder)v.getTag();
        }

        if (mEnableThumb) {
            viewHolder.mThumbUp.setVisibility(View.VISIBLE);
            viewHolder.mThumbDown.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mThumbUp.setVisibility(View.GONE);
            viewHolder.mThumbDown.setVisibility(View.GONE);
        }
        viewHolder.mTvTitle.setText(mValues.get(position).getPhoneNumber());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                        Date date = new Date(mValues.get(position).getStartTimestamp());
                        String callDate = sdf.format(date);
        viewHolder.mTvInfo.setText(callDate);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPagerEvent(mValues.get(position), AppConstant.COMMON_ITEM_RECENTCALL_ITEM_SELECTED);
                }
            }
        });
        return v;//super.getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ReputasiTextView mTvTitle, mTvInfo;
        ImageView mThumbUp, mThumbDown;
    }

}
