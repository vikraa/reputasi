package com.reputasi.library.rest.response;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseResponse;

/**
 * Created by vikraa on 6/24/2015.
 */
public class CategoryNumber extends BaseResponse {
    @SerializedName("categoryId")
    private String mCategoryId;
    @SerializedName("categoryName")
    private String mCategoryName;
    @SerializedName("categoryRegisteredCount")
    private int mRegisteredCount;

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String categoryId) {
        this.mCategoryId = categoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.mCategoryName = categoryName;
    }

    public int getRegisteredCount() {
        return mRegisteredCount;
    }

    public void setRegisteredCount(int registeredCount) {
        this.mRegisteredCount = registeredCount;
    }
}
