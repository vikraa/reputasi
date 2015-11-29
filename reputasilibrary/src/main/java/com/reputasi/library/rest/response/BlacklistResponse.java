package com.reputasi.library.rest.response;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseResponse;

/**
 * Created by Vikraa on 7/15/2015.
 */
public class BlacklistResponse extends BaseResponse {
    @SerializedName("username")
    private String mUserName;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("numberName")
    private String mNumberName;
    @SerializedName("category")
    private String mCategoryId;
    @SerializedName("specialFlag")
    private int mFlagType;

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

    public String getNumberName() {
        return mNumberName;
    }

    public void setNumberName(String mNumberName) {
        this.mNumberName = mNumberName;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public int getFlagType() {
        return mFlagType;
    }

    public void setFlagType(int mFlagType) {
        this.mFlagType = mFlagType;
    }
}
