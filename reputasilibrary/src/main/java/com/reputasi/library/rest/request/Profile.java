package com.reputasi.library.rest.request;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseRequest;

/**
 * Created by vikraa on 6/23/2015.
 */
public class Profile extends BaseRequest {

    @SerializedName("username")
    private String mName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("authData")
    private Object mAuthData;
    @SerializedName("displayName")
    private String mDisplayName;
    @SerializedName("deviceInfo")
    private String mDeviceInfo;
    @SerializedName("simcardId")
    private String mSimCardId;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("gender")
    private String mGender;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    /*public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }*/

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public Object getAuthData() {
        return mAuthData;
    }

    public void setAuthData(Object mAuthData) {
        this.mAuthData = mAuthData;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getDeviceInfo() {
        return mDeviceInfo;
    }

    public void setDeviceInfo(String mDeviceInfo) {
        this.mDeviceInfo = mDeviceInfo;
    }

    public String getSimCardId() {
        return mSimCardId;
    }

    public void setSimCardId(String mSimCardId) {
        this.mSimCardId = mSimCardId;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }
}
