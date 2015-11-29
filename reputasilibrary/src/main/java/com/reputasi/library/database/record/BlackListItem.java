package com.reputasi.library.database.record;

import android.content.ContentValues;

import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.table.TableBlackList;

/**
 * Created by vikraa on 6/27/2015.
 */
public class BlackListItem implements BaseItem {
    
    private String mNumber;
    private String mName;
    private String mCategorNumberName;
    private int mSynchronizeStatus;

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCategorNumberName() {
        return mCategorNumberName;
    }

    public void setCategoryNumberName(String mCategorNumberName) {
        this.mCategorNumberName = mCategorNumberName;
    }

    public int getSynchronizeStatus() {
        return mSynchronizeStatus;
    }

    public void setSynchronizeStatus(int synchronizeStatus) {
        this.mSynchronizeStatus = synchronizeStatus;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TableBlackList.BLACKLIST_NUMBER, getNumber());
        values.put(TableBlackList.BLACKLIST_NAME, getName());
        //values.put(TableBlackList.BLACKLIST_CATEGORY_NUMBER_ID, );
        values.put(TableBlackList.BLACKLIST_SYNCHRONIZE_STATUS, getSynchronizeStatus());
        return values;
    }

    @Override
    public BaseItem fromContentValues(ContentValues contentValues) {
        setName(contentValues.getAsString(TableBlackList.BLACKLIST_NAME));
        setNumber(contentValues.getAsString(TableBlackList.BLACKLIST_NUMBER));
        setSynchronizeStatus(contentValues.getAsInteger(TableBlackList.BLACKLIST_SYNCHRONIZE_STATUS));
        //setCategoryNumberName();
        return null;
    }
}
