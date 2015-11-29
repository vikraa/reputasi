package com.reputasi.library.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vikraa on 8/3/2015.
 */
public class ContactHash {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("needUpdate")
    private boolean mNeedUpdate;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public boolean isNeedUpdate() {
        return mNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.mNeedUpdate = needUpdate;
    }
}
