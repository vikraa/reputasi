package com.reputasi.library.rest.request;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseRequest;

/**
 * Created by vikraa on 6/24/2015.
 */
public class IncomingNumber extends BaseRequest {
    @SerializedName("username")
    private String mUserName; // email name
    @SerializedName("phoneNumber")
    private String mPhoneNumber;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }
}
