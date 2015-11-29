package com.reputasi.library.database.record;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.database.table.TableContactBook;
import com.reputasi.library.rest.response.CategoryNumber;

/**
 * Created by vikraa on 6/24/2015.
 */
public class CategoryNumberItem implements BaseItem {
    private String mCategoryId;
    private String mCategoryName;
    private int mRegisteredCount;
    private int mActiveStatus;

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public int getRegisteredCount() {
        return mRegisteredCount;
    }

    public void setRegisteredCount(int mRegisteredCount) {
        this.mRegisteredCount = mRegisteredCount;
    }

    public int getActiveStatus() {
        return mActiveStatus;
    }

        public void setActiveStatus(int activeStatus) {
        this.mActiveStatus = activeStatus;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TableCategoryNumber.CATEGORY_ID, getCategoryId());
        values.put(TableCategoryNumber.CATEGORY_NAME, getCategoryName());
        values.put(TableCategoryNumber.CATEGORY_REGISTERED_COUNT, getRegisteredCount());
        values.put(TableCategoryNumber.CATEGORY_ACTIVE_STATUS, getActiveStatus());
        return values;
    }

    @Override
    public BaseItem fromContentValues(ContentValues contentValues) {
        setCategoryId(contentValues.getAsString(TableCategoryNumber.CATEGORY_ID));
        setCategoryName(contentValues.getAsString(TableCategoryNumber.CATEGORY_NAME));
        setRegisteredCount(contentValues.getAsInteger(TableCategoryNumber.CATEGORY_REGISTERED_COUNT));
        setActiveStatus(contentValues.getAsInteger(TableCategoryNumber.CATEGORY_ACTIVE_STATUS));
        return this;
    }
}
