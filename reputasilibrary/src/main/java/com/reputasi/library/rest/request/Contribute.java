package com.reputasi.library.rest.request;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseRequest;

/**
 * Created by vikraa on 6/23/2015.
 */
public class Contribute {
    @SerializedName("incomingNumber")
    private String mPhoneNumber;
    @SerializedName("ownerName")
    private String mNumberOwner;
    @SerializedName("category")
    private String mCategoryId;
    @SerializedName("notes")
    private String mNotes;
    @SerializedName("reputation")
    private String mThumbUpDown;

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String categoryId) {
        this.mCategoryId = categoryId;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public String getNumberOwner() {
        return mNumberOwner;
    }

    public void setNumberOwner(String numberOwner) {
        this.mNumberOwner = numberOwner;
    }

    public String getThumbUpDown() {
        return mThumbUpDown;
    }

    public void setThumbUpDown(String thumbUpDown) {
        this.mThumbUpDown = thumbUpDown;
    }
}
