package com.reputasi.library.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vikraa on 8/3/2015.
 */
public class BaseResponseFunction<T> {
    @SerializedName("result")
    private T mResult;

    public T getResult() {
        return mResult;
    }

    public void setResult(T result) {
        this.mResult = result;
    }
}
