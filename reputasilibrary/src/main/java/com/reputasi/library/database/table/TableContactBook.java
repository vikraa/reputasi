package com.reputasi.library.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.reputasi.library.database.BaseTable;
import com.reputasi.library.database.DatabaseConstants;

/**
 * Created by vikraa on 6/23/2015.
 */
public class TableContactBook implements BaseTable {

    public static final String CONTACT_ID = "contactbookid";
    public static final String CONTACT_NAME = "contacbookname";
    public static final String CONTACT_NUMBER = "contactbooknumber";
    public static final String CONTACT_SYNCHRONIZE_STATUS = "syncronizestatus";
    public static final String TABLE_NAME = "contactbook";
    public static final int TABLE_CODE = 10;
    public static final Uri PATH = Uri.withAppendedPath(DatabaseConstants.DATABASE_URI, DatabaseConstants.DATABASE_NAME + "/" + TABLE_NAME);

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                CONTACT_ID + " TEXT NOT NULL," +
                CONTACT_NUMBER + " TEXT NOT NULL," +
                CONTACT_NAME + " TEXT," +
                CONTACT_SYNCHRONIZE_STATUS + " BIGINT DEFAULT 0," +
                "PRIMARY KEY ( " + CONTACT_ID + " , " + CONTACT_NUMBER + " ))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
