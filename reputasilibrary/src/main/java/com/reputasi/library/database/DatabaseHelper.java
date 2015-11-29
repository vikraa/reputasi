package com.reputasi.library.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.reputasi.library.ReputasiApplication;
import com.reputasi.library.database.table.TableBlackList;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.database.table.TableContactBook;
import com.reputasi.library.database.table.TableSpammer;

/**
 * Created by vikraa on 6/25/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDb;
    private BaseTable[] mTables = { new TableContactBook(), new TableSpammer(), new TableCategoryNumber(), new TableBlackList() };
    //private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/callblocker.db";

    public DatabaseHelper(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
        //super(context, path, null, DatabaseConstants.DATABASE_VERSION);
        if (mDb == null || !mDb.isOpen()) {
            mDb = getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (BaseTable bt : mTables) {
            bt.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (BaseTable bt : mTables) {
            bt.onUpgrade(db, oldVersion, newVersion);
        }
    }

}
