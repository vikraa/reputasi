package com.reputasi.library.database.record;

import android.content.ContentValues;

import com.reputasi.library.database.BaseItem;

/**
 * Created by vikraa on 6/24/2015.
 */
public class SpammerNumberItem implements BaseItem {

    private String mSpammerName;
    private String mSpammerNumber;
    private String mSpammerCategoryNumberName;

    public String getSpammerName() {
        return mSpammerName;
    }

    public void setSpammerName(String spammerName) {
        this.mSpammerName = spammerName;
    }

    public String getSpammerNumber() {
        return mSpammerNumber;
    }

    public void setSpammerNumber(String spammerNumber) {
        this.mSpammerNumber = spammerNumber;
    }

    public String getSpammerCategoryNumberName() {
        return mSpammerCategoryNumberName;
    }

    public void setSpammerCategoryNumberName(String categoryName) {
        this.mSpammerCategoryNumberName = categoryName;
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }

    @Override
    public BaseItem fromContentValues(ContentValues contentValues) {
        return null;
    }
}
