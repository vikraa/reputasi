package com.reputasi.library.rest.request;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseRequest;

/**
 * Created by vikraa on 6/23/2015.
 */
public class ContactBook /*extends BaseRequest */{
    @SerializedName("contactName")
    private String mContactName;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    /*@SerializedName("deviceId")
    private String mDeviceId;
    @SerializedName("simCardID")
    private String mSimCardId;*/

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String mContactName) {
        this.mContactName = mContactName;
    }

/*
    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }
*/

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

/*
    public String getSimCardId() {
        return mSimCardId;
    }

    public void setSimCardId(String mSimCardId) {
        this.mSimCardId = mSimCardId;
    }
*/
}
