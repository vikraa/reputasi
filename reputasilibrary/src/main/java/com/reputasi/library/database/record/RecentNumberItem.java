package com.reputasi.library.database.record;

import android.content.ContentValues;

import com.reputasi.library.database.BaseItem;

/**
 * Created by vikraa on 6/24/2015.
 */
public class RecentNumberItem implements BaseItem {
    private String mPhoneNumber;
    private long mStartTimestamp;
    private long mEndTimestamp;
    private long mDurationTime;
    private int mCallType;

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public long getStartTimestamp() {
        return mStartTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.mStartTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return mEndTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.mEndTimestamp = endTimestamp;
    }

    public long getDurationTime() {
        return mDurationTime;
    }

    public void setDurationTime(long durationTime) {
        this.mDurationTime = durationTime;
    }

    public int getCallType() {
        return mCallType;
    }

    public void setCallType(int callType) {
        this.mCallType = callType;
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }

    @Override
    public BaseItem fromContentValues(ContentValues contentValues) {
        return null;
    }
}
