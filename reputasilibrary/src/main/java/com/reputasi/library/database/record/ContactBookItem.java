package com.reputasi.library.database.record;

import android.content.ContentValues;

import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.table.TableContactBook;

/**
 * Created by vikraa on 6/24/2015.
 */
public class ContactBookItem implements BaseItem {

    private String mContactId;
    private String mContactName;
    private String mContactNumber;
    private int mSynchronizeStatus;

    public String getContactId() {
        return mContactId;
    }

    public void setContactId(String mContactId) {
        this.mContactId = mContactId;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public String getContactNumber() {
        return mContactNumber;
    }

    public void setContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }

    public int getSynchronizeStatus() {
        return mSynchronizeStatus;
    }

    public void setSynchronizeStatus(int mSynchronizeStatus) {
        this.mSynchronizeStatus = mSynchronizeStatus;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TableContactBook.CONTACT_ID, getContactId());
        values.put(TableContactBook.CONTACT_NAME, getContactName());
        values.put(TableContactBook.CONTACT_NUMBER, getContactNumber());
        values.put(TableContactBook.CONTACT_SYNCHRONIZE_STATUS, getSynchronizeStatus());
        return values;
    }

    @Override
    public BaseItem fromContentValues(ContentValues contentValues) {
        setContactId(contentValues.getAsString(TableContactBook.CONTACT_ID));
        setContactName(contentValues.getAsString(TableContactBook.CONTACT_NAME));
        setContactNumber(contentValues.getAsString(TableContactBook.CONTACT_NUMBER));
        setSynchronizeStatus(contentValues.getAsInteger(TableContactBook.CONTACT_SYNCHRONIZE_STATUS));
        return this;
    }
}
