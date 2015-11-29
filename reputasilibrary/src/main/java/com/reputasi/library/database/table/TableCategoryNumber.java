package com.reputasi.library.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.reputasi.library.database.BaseTable;
import com.reputasi.library.database.DatabaseConstants;

/**
 * Created by vikraa on 6/24/2015.
 */
public class TableCategoryNumber implements BaseTable {

    public static final String CATEGORY_ID = "categorynumberid";
    public static final String CATEGORY_NAME = "categorynumbername";
    public static final String CATEGORY_REGISTERED_COUNT = "categoryregistredcount";
    public static final String CATEGORY_ACTIVE_STATUS = "categoryactivestatus";
    public static final String TABLE_NAME = "categorynumber";
    public static final int TABLE_CODE = 20;
    public static final Uri PATH = Uri.withAppendedPath(DatabaseConstants.DATABASE_URI, DatabaseConstants.DATABASE_NAME + "/" + TABLE_NAME);
    public static final int CATEGORY_ACTIVE = 1;
    public static final int CATEGORY_NOT_ACTIVE = 0;


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                CATEGORY_ID + " TEXT NOT NULL PRIMARY KEY," +
                CATEGORY_NAME + " TEXT," +
                CATEGORY_REGISTERED_COUNT + " TEXT," +
                CATEGORY_ACTIVE_STATUS + " BIGINT DEFAULT 0 )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
