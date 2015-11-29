package com.reputasi.library.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by vikraa on 6/24/2015.
 */
public interface BaseTable {
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
