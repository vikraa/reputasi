package com.reputasi.library.database;

import android.content.ContentResolver;
import android.database.Cursor;

import com.reputasi.library.ReputasiApplication;
import com.reputasi.library.database.record.BlackListItem;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.database.record.ContactBookItem;
import com.reputasi.library.database.record.SpammerNumberItem;
import com.reputasi.library.database.table.TableBlackList;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.database.table.TableContactBook;
import com.reputasi.library.database.table.TableSpammer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikraa on 6/25/2015.
 */
public class DataContentProviderHelper {

    private static DataContentProviderHelper mInstance;
    private ContentResolver mContentResolver;

    public static DataContentProviderHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DataContentProviderHelper();
            mInstance.mContentResolver = ReputasiApplication.getResolver();
        }
        return mInstance;
    }

    public void insert(BaseItem item) {
        synchronized (new Object()) {
            if (item instanceof CategoryNumberItem) {
                CategoryNumberItem model = (CategoryNumberItem)item;
                mContentResolver.insert(TableCategoryNumber.PATH, model.toContentValues());
            } else if (item instanceof ContactBookItem) {
                ContactBookItem model = (ContactBookItem)item;
                mContentResolver.insert(TableContactBook.PATH, model.toContentValues());
            } else if (item instanceof SpammerNumberItem) {
                SpammerNumberItem model = (SpammerNumberItem)item;
                mContentResolver.insert(TableSpammer.PATH, model.toContentValues());
            } else if (item instanceof BlackListItem) {
                BlackListItem model = (BlackListItem)item;
                mContentResolver.insert(TableBlackList.PATH, model.toContentValues());
            }
        }
    }

    public void update(BaseItem item) {
        synchronized (new Object()) {
            if (item instanceof CategoryNumberItem) {
                CategoryNumberItem model = (CategoryNumberItem) item;
                mContentResolver.update(TableCategoryNumber.PATH, model.toContentValues(),
                        TableCategoryNumber.CATEGORY_ID + " = ?",
                        new String[]{model.getCategoryId()});
            } else if (item instanceof ContactBookItem) {
                ContactBookItem model = (ContactBookItem) item;
                mContentResolver.update(TableContactBook.PATH, model.toContentValues(),
                        TableContactBook.CONTACT_ID + " = ? AND " + TableContactBook.CONTACT_NUMBER + " = ?",
                        new String[]{model.getContactId(), model.getContactNumber()});
            } else if (item instanceof SpammerNumberItem) {
                SpammerNumberItem model = (SpammerNumberItem) item;
                mContentResolver.update(TableSpammer.PATH, model.toContentValues(),
                        TableSpammer.SPAMMER_NUMBER + " = ?", new String[]{model.getSpammerNumber()});

            } else if (item instanceof BlackListItem) {
                BlackListItem model = (BlackListItem) item;
                mContentResolver.update(TableBlackList.PATH, model.toContentValues(),
                        TableBlackList.BLACKLIST_NUMBER + " = ?", new String[]{model.getNumber()});
            }
        }
    }

    public void delete(BaseItem item) {
        synchronized (new Object()) {
            if (item instanceof CategoryNumberItem) {
                CategoryNumberItem model = (CategoryNumberItem)item;
                mContentResolver.delete(TableCategoryNumber.PATH,
                        TableCategoryNumber.CATEGORY_ID + " = ?",
                        new String[]{model.getCategoryId()}) ;
            } else if (item instanceof ContactBookItem) {
                ContactBookItem model = (ContactBookItem)item;
                mContentResolver.delete(TableContactBook.PATH,
                        TableContactBook.CONTACT_ID + " = ? AND " + TableContactBook.CONTACT_NUMBER + " = ?",
                        new String[]{model.getContactId(), model.getContactNumber()});
            } else if (item instanceof SpammerNumberItem) {
                SpammerNumberItem model = (SpammerNumberItem)item;
                mContentResolver.delete(TableSpammer.PATH,
                        TableSpammer.SPAMMER_NUMBER + " = ?", new String[]{model.getSpammerNumber()});
            } else if (item instanceof BlackListItem) {
                BlackListItem model  = (BlackListItem)item;
                mContentResolver.delete(TableBlackList.PATH,
                        TableBlackList.BLACKLIST_NUMBER + " = ?", new String[]{model.getNumber()});
            }
        }
    }


    public BaseItem query(int tableCode, String where, String[] whereArgs, String sortOrder) {
        synchronized (new Object()) {
            BaseItem result = null;
            Cursor cursor = null;
            switch (tableCode) {
                case TableCategoryNumber.TABLE_CODE :
                    cursor = mContentResolver.query(TableCategoryNumber.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            CategoryNumberItem model = new CategoryNumberItem();
                            model.setCategoryId(cursor.getString(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_ID)));
                            model.setCategoryName(cursor.getString(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_NAME)));
                            model.setRegisteredCount(cursor.getInt(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_REGISTERED_COUNT)));
                            model.setActiveStatus(cursor.getInt(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_ACTIVE_STATUS)));
                            result = model;
                        }
                        cursor.close();
                    }
                    break;
                case TableContactBook.TABLE_CODE :
                    cursor = mContentResolver.query(TableContactBook.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            ContactBookItem model = new ContactBookItem();
                            model.setContactId(cursor.getString(cursor.getColumnIndex(TableContactBook.CONTACT_ID)));
                            model.setContactName(cursor.getString(cursor.getColumnIndex(TableContactBook.CONTACT_NAME)));
                            model.setContactNumber(cursor.getString(cursor.getColumnIndex(TableContactBook.CONTACT_NUMBER)));
                            model.setSynchronizeStatus(cursor.getInt(cursor.getColumnIndex(TableContactBook.CONTACT_SYNCHRONIZE_STATUS)));
                            result = model;
                        }
                        cursor.close();
                    }
                    break;
                case TableSpammer.TABLE_CODE :
                    cursor = mContentResolver.query(TableSpammer.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            SpammerNumberItem model = new SpammerNumberItem();
                            model.setSpammerNumber(cursor.getString(cursor.getColumnIndex(TableSpammer.SPAMMER_NUMBER)));
                            model.setSpammerName(cursor.getString(cursor.getColumnIndex(TableSpammer.SPAMMER_NAME)));
                            CategoryNumberItem categoryModel = (CategoryNumberItem)query(TableCategoryNumber.TABLE_CODE,
                                    TableCategoryNumber.CATEGORY_ID + " = ?",
                                    new String[] { cursor.getString(cursor.getColumnIndex(TableSpammer.SPAMMER_CATEGORY_NUMBER_ID)) }, null);
                            if (categoryModel != null) {
                                model.setSpammerCategoryNumberName(categoryModel.getCategoryName());
                            } else {
                                model.setSpammerCategoryNumberName(" Unknown ");
                            }
                            result = model;
                        }
                        cursor.close();
                    }
                    break;
                case TableBlackList.TABLE_CODE :
                    cursor = mContentResolver.query(TableBlackList.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            BlackListItem model = new BlackListItem();
                            model.setNumber(cursor.getString(cursor.getColumnIndex(TableBlackList.BLACKLIST_NUMBER)));
                            model.setName(cursor.getString(cursor.getColumnIndex(TableBlackList.BLACKLIST_NAME)));
                            String categoryName = cursor.getString(cursor.getColumnIndex(TableBlackList.BLACKLIST_CATEGORY_NUMBER_ID));
                            CategoryNumberItem categoryModel = (CategoryNumberItem)query(TableCategoryNumber.TABLE_CODE,
                                    TableCategoryNumber.CATEGORY_ID + " = ?",
                                    new String[] {  categoryName == null ? "900001" :  categoryName }, null);

                            if (categoryModel != null) {
                                model.setCategoryNumberName(categoryModel.getCategoryName());
                            } else {
                                model.setCategoryNumberName(" Unknown ");
                            }
                            result = model;
                        }
                        cursor.close();
                    }
                    break;
            }
            return result;
        }
    }

    public List<BaseItem> queries(int tableCode, String where, String[] whereArgs, String sortOrder) {
        synchronized (new Object()) {
            List<BaseItem> result = new ArrayList<>();
            Cursor cursor = null;
            switch (tableCode) {
                case TableCategoryNumber.TABLE_CODE :
                    cursor = mContentResolver.query(TableCategoryNumber.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                CategoryNumberItem model = new CategoryNumberItem();
                                model.setCategoryId(cursor.getString(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_ID)));
                                model.setCategoryName(cursor.getString(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_NAME)));
                                model.setRegisteredCount(cursor.getInt(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_REGISTERED_COUNT)));
                                model.setActiveStatus(cursor.getInt(cursor.getColumnIndex(TableCategoryNumber.CATEGORY_ACTIVE_STATUS)));
                                result.add(model);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }
                    break;
                case TableContactBook.TABLE_CODE :
                    cursor = mContentResolver.query(TableContactBook.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                ContactBookItem model = new ContactBookItem();
                                model.setContactId(cursor.getString(cursor.getColumnIndex(TableContactBook.CONTACT_ID)));
                                model.setContactName(cursor.getString(cursor.getColumnIndex(TableContactBook.CONTACT_NAME)));
                                model.setContactNumber(cursor.getString(cursor.getColumnIndex(TableContactBook.CONTACT_NUMBER)));
                                model.setSynchronizeStatus(cursor.getInt(cursor.getColumnIndex(TableContactBook.CONTACT_SYNCHRONIZE_STATUS)));
                                result.add(model);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }
                    break;
                case TableSpammer.TABLE_CODE :
                    cursor = mContentResolver.query(TableSpammer.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                SpammerNumberItem model = new SpammerNumberItem();
                                model.setSpammerNumber(cursor.getString(cursor.getColumnIndex(TableSpammer.SPAMMER_NUMBER)));
                                model.setSpammerName(cursor.getString(cursor.getColumnIndex(TableSpammer.SPAMMER_NAME)));
                                CategoryNumberItem categoryModel = (CategoryNumberItem)query(TableCategoryNumber.TABLE_CODE,
                                        TableCategoryNumber.CATEGORY_ID + " = ?",
                                        new String[] { cursor.getString(cursor.getColumnIndex(TableSpammer.SPAMMER_CATEGORY_NUMBER_ID)) }, null);
                                if (categoryModel != null) {
                                    model.setSpammerCategoryNumberName(categoryModel.getCategoryName());
                                } else {
                                    model.setSpammerCategoryNumberName(" Unknown ");
                                }
                                result.add(model);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }
                    break;
                case TableBlackList.TABLE_CODE :
                    cursor = mContentResolver.query(TableBlackList.PATH, null, where, whereArgs, sortOrder);
                    if (cursor != null) {
                        cursor.moveToLast();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                BlackListItem model = new BlackListItem();
                                model.setNumber(cursor.getString(cursor.getColumnIndex(TableBlackList.BLACKLIST_NUMBER)));
                                model.setName(cursor.getString(cursor.getColumnIndex(TableBlackList.BLACKLIST_NAME)));
                                String categoryNumberId = cursor.getString(cursor.getColumnIndex(TableBlackList.BLACKLIST_CATEGORY_NUMBER_ID));
                                CategoryNumberItem categoryModel = (CategoryNumberItem)query(TableCategoryNumber.TABLE_CODE,
                                        TableCategoryNumber.CATEGORY_ID + " = ?",
                                        new String[] { categoryNumberId == null ? "900001" :  categoryNumberId }, null);

                                if (categoryModel != null) {
                                    model.setCategoryNumberName(categoryModel.getCategoryName());
                                } else {
                                    model.setCategoryNumberName(" Unknown ");
                                }
                                result.add(model);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }
                    break;
            }
            return result;
        }
    }

    public void clear(int tableCode) {
        synchronized (new Object()) {
            switch (tableCode) {
                case TableContactBook.TABLE_CODE :
                    mContentResolver.delete(TableContactBook.PATH, null, null);
                    break;
                case TableCategoryNumber.TABLE_CODE :
                    mContentResolver.delete(TableCategoryNumber.PATH, null, null);
                    break;
                case TableSpammer.TABLE_CODE :
                    mContentResolver.delete(TableSpammer.PATH, null, null);
                    break;
                case TableBlackList.TABLE_CODE :
                    mContentResolver.delete(TableBlackList.PATH, null, null);
                    break;
            }
        }
    }

}
