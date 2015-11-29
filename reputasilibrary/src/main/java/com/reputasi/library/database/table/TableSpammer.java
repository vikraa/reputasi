package com.reputasi.library.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.reputasi.library.database.BaseTable;
import com.reputasi.library.database.DatabaseConstants;

/**
 * Created by vikraa on 6/24/2015.
 */
public class TableSpammer implements BaseTable {

    public static final String SPAMMER_NUMBER = "spammernumber";
    public static final String SPAMMER_NAME = "spammername";
    public static final String SPAMMER_CATEGORY_NUMBER_ID = "spammercategorynumberid";
    public static final String TABLE_NAME = "spammer";
    public static final int TABLE_CODE = 30;
    public static final Uri PATH = Uri.withAppendedPath(DatabaseConstants.DATABASE_URI, DatabaseConstants.DATABASE_NAME + "/" + TABLE_NAME);

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                SPAMMER_NUMBER + " TEXT NOT NULL PRIMARY KEY," +
                SPAMMER_NAME + " TEXT, " +
                SPAMMER_CATEGORY_NUMBER_ID + " TEXT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
