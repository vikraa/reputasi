package com.reputasi.library.rest.request;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseRequest;

/**
 * Created by vikraa on 6/23/2015.
 */
public class Blacklist /*extends BaseRequest*/ {

    @SerializedName("incomingNumber")
    private String mPhoneNumber;
/*
    @SerializedName("userName")
    private String mUserName;
    @SerializedName("numberName")
    private String mNumberName;
    @SerializedName("category")
    private String mCategoryId;
    @SerializedName("specialFlag")
    private int mSpecialFlag;
*/

//    public String getUserName() {
//        return mUserName;
//    }
//
//    public void setUserName(String userName) {
//        this.mUserName = userName;
//    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

//    public String getNumberName() {
//        return mNumberName;
//    }

//    public void setNumberName(String numberName) {
//        this.mNumberName = numberName;
//    }

//    public String getCategoryId() {
//        return mCategoryId;
//    }

//    public void setCategoryId(String categoryId) {
//        this.mCategoryId = categoryId;
//    }

//    public int getSpecialFlag() {
//        return mSpecialFlag;
//    }

//    public void setSpecialFlag(int specialFlag) {
//        this.mSpecialFlag = specialFlag;
//    }
}
