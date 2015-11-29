package com.reputasi.library.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.reputasi.library.database.table.TableBlackList;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.database.table.TableContactBook;
import com.reputasi.library.database.table.TableSpammer;


/**
 * Created by vikraa on 6/25/2015.
 */
public class DataContentProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDb;

    static  {
        mUriMatcher.addURI(DatabaseConstants.DATABASE_AUTHORITY, DatabaseConstants.DATABASE_NAME + "/" + TableContactBook.TABLE_NAME, TableContactBook.TABLE_CODE);
        mUriMatcher.addURI(DatabaseConstants.DATABASE_AUTHORITY, DatabaseConstants.DATABASE_NAME + "/" + TableCategoryNumber.TABLE_NAME, TableCategoryNumber.TABLE_CODE);
        mUriMatcher.addURI(DatabaseConstants.DATABASE_AUTHORITY, DatabaseConstants.DATABASE_NAME + "/" + TableSpammer.TABLE_NAME, TableSpammer.TABLE_CODE);
        mUriMatcher.addURI(DatabaseConstants.DATABASE_AUTHORITY, DatabaseConstants.DATABASE_NAME + "/" + TableBlackList.TABLE_NAME, TableBlackList.TABLE_CODE);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        mDb = mDatabaseHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor cursor = null;

        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case TableContactBook.TABLE_CODE :
                queryBuilder.setTables(TableContactBook.TABLE_NAME);
                 break;
            case TableCategoryNumber.TABLE_CODE :
                queryBuilder.setTables(TableCategoryNumber.TABLE_NAME);
                break;
            case TableSpammer.TABLE_CODE :
                queryBuilder.setTables(TableSpammer.TABLE_NAME);
                break;
            case TableBlackList.TABLE_CODE:
                queryBuilder.setTables(TableBlackList.TABLE_NAME);
                break;
        }

        cursor = queryBuilder.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return uri != null ? uri.toString() : null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case TableContactBook.TABLE_CODE :
                id = mDb.insert(TableContactBook.TABLE_NAME, null, values);
                break;
            case TableCategoryNumber.TABLE_CODE :
                id = mDb.insert(TableCategoryNumber.TABLE_NAME, null, values);
                break;
            case TableSpammer.TABLE_CODE :
                id = mDb.insert(TableSpammer.TABLE_NAME, null, values);
                break;
            case TableBlackList.TABLE_CODE:
                id = mDb.insert(TableBlackList.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("");
        }

        return Uri.parse(DatabaseConstants.DATABASE_NAME + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id = 0;
        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case TableContactBook.TABLE_CODE :
                id  = mDb.delete(TableContactBook.TABLE_NAME, selection, selectionArgs);
                break;
            case TableCategoryNumber.TABLE_CODE :
                id  = mDb.delete(TableCategoryNumber.TABLE_NAME, selection, selectionArgs);
                break;
            case TableSpammer.TABLE_CODE :
                id  = mDb.delete(TableSpammer.TABLE_NAME, selection, selectionArgs);
                break;
            case TableBlackList.TABLE_CODE:
                id = mDb.delete(TableBlackList.TABLE_NAME, selection, selectionArgs);
                break;
        }
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int id = 0;
        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case TableContactBook.TABLE_CODE :
                id = mDb.update(TableContactBook.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TableCategoryNumber.TABLE_CODE :
                id = mDb.update(TableCategoryNumber.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TableSpammer.TABLE_CODE :
                id = mDb.update(TableSpammer.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TableBlackList.TABLE_CODE:
                id = mDb.update(TableBlackList.TABLE_NAME, values, selection, selectionArgs);
                break;
        }
        return id;
    }
}
