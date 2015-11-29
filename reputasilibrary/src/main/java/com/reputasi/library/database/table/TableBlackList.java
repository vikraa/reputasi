package com.reputasi.library.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.reputasi.library.database.BaseTable;
import com.reputasi.library.database.DatabaseConstants;

/**
 * Created by vikraa on 6/27/2015.
 */
public class TableBlackList implements BaseTable {
    public static final String TABLE_NAME = "blacklist";
    public static final int TABLE_CODE = 40;
    public static final Uri PATH = Uri.withAppendedPath(DatabaseConstants.DATABASE_URI, DatabaseConstants.DATABASE_NAME + "/" + TABLE_NAME);

    public static final String BLACKLIST_NUMBER = "blacklistnumber";
    public static final String BLACKLIST_NAME = "blacklistname";
    public static final String BLACKLIST_CATEGORY_NUMBER_ID = "blacklistcategorynumberid";
    public static final String BLACKLIST_SYNCHRONIZE_STATUS = "synchronizestatus";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                BLACKLIST_NUMBER + " TEXT NOT NULL PRIMARY KEY," +
                BLACKLIST_NAME + " TEXT," +
                BLACKLIST_CATEGORY_NUMBER_ID  + " TEXT," +
                BLACKLIST_SYNCHRONIZE_STATUS + " BIGINT ) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
