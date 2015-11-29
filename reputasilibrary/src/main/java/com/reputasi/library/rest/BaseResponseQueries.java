package com.reputasi.library.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vikraa on 6/23/2015.
 */
public class BaseResponseQueries<T extends BaseResponse> {
    @SerializedName("results")
    private List<T> mResults;

    @SerializedName("result")
    private List<T> mData;

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public List<T> getResults() {
        return mResults;
    }

    public void setResults(List<T> results) {
        this.mResults = results;
    }
}
