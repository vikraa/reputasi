package com.reputasi.library.database;

import android.content.ContentValues;

/**
 * Created by vikraa on 6/24/2015.
 */
public interface BaseItem {
    public ContentValues toContentValues();
    public BaseItem fromContentValues(ContentValues contentValues);
}
