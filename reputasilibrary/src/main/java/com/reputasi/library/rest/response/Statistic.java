package com.reputasi.library.rest.response;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseResponse;

/**
 * Created by vikraa on 7/6/2015.
 */
public class Statistic extends BaseResponse {
    @SerializedName("legendName")
    private String mLegendName;
    @SerializedName("value")
    private long mValue;

    public String getLegendName() {
        return mLegendName;
    }

    public void setLegendName(String mLegendName) {
        this.mLegendName = mLegendName;
    }

    public long getValue() {
        return mValue;
    }

    public void setValue(long mValue) {
        this.mValue = mValue;
    }
}
