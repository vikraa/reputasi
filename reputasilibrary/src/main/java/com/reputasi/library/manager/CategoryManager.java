package com.reputasi.library.manager;

import android.util.Log;

import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.BaseResponseQueries;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;
import com.reputasi.library.rest.response.CategoryNumber;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by vikraa on 7/12/2015.
 */
public class CategoryManager extends BaseManager<Void, List<CategoryNumberItem>> {

    public static final int POPULATE_NONE = 0;
    public static final int POPULATE_CATEGORY_NUMBER = 40;
    public static final int CATEGORY_TYPE_BLACKLIST = 50;
    public static final int CATEGORY_TYPE_WHITELIST = 60;
    public static final int CATEGORY_TYPE_ALL = 70;

    private static CategoryManager mInstance;
    private static int mProgressRunning;
    private ManagerListener mListener;

    public static CategoryManager getInstance() {
        if (mInstance == null) {
            mInstance = new CategoryManager();
            mInstance.mProgressRunning = POPULATE_NONE;
        }
        return mInstance;
    }

    public CategoryManager setListener(ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    public List<CategoryNumberItem> getCategoryCache(int categoryType) {
        List<CategoryNumberItem> itemList = new ArrayList<>();
        List<BaseItem> items = null;
        switch (categoryType) {
            case CATEGORY_TYPE_ALL:
                items = DataContentProviderHelper.getInstance().queries(TableCategoryNumber.TABLE_CODE, TableCategoryNumber.CATEGORY_ACTIVE_STATUS + " = ? ",
                        new String[] { String.valueOf(TableCategoryNumber.CATEGORY_ACTIVE) }, null);
                break;
            case CATEGORY_TYPE_BLACKLIST:
                items = DataContentProviderHelper.getInstance().queries(TableCategoryNumber.TABLE_CODE, TableCategoryNumber.CATEGORY_ACTIVE_STATUS + " = ? AND " + TableCategoryNumber.CATEGORY_ID + " >= ? AND " +TableCategoryNumber.CATEGORY_ID + " < ?",
                        new String[] { String.valueOf(TableCategoryNumber.CATEGORY_ACTIVE), "100000","200000" }, null);
                break;
            case CATEGORY_TYPE_WHITELIST:
                items = DataContentProviderHelper.getInstance().queries(TableCategoryNumber.TABLE_CODE, TableCategoryNumber.CATEGORY_ACTIVE_STATUS + " = ? AND " + TableCategoryNumber.CATEGORY_ID + " >= ?",
                        new String[] { String.valueOf(TableCategoryNumber.CATEGORY_ACTIVE), "200000" }, null);
                break;
        }
        if (items != null) {
            for (BaseItem it : items) {
                CategoryNumberItem categoryItem  = (CategoryNumberItem)it;
                itemList.add(categoryItem);
            }
        }
        return itemList;
    }

    public void fetchCategoryNumber() {
        if (startBackgroundTask(POPULATE_CATEGORY_NUMBER)) {
            mInstance.mProgressRunning = POPULATE_CATEGORY_NUMBER;
        }
    }

    @Override
    protected List<CategoryNumberItem> onBackgroundTask(Integer... params) {
        List<CategoryNumberItem> categoryNumberItems = new ArrayList<>();
        switch (params[0]) {
            case POPULATE_CATEGORY_NUMBER:
                if (ReputasiUtils.isNetworkConnected()) {
                    try {
                        RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
                        BaseResponseQueries<CategoryNumber> response = client.requestAPI().rptsGetBlacklistCategory(ReputasiUtils.getDummyJson());
                        if (response != null) {
                            DataContentProviderHelper.getInstance().clear(TableCategoryNumber.TABLE_CODE);
                            List<CategoryNumber> categoryNumber = response.getData();
                            for (CategoryNumber cn : categoryNumber) {
                                CategoryNumberItem item = new CategoryNumberItem();
                                item.setCategoryId(cn.getCategoryId());
                                item.setCategoryName(cn.getCategoryName());
                                item.setRegisteredCount(cn.getRegisteredCount());
                                item.setActiveStatus(TableCategoryNumber.CATEGORY_ACTIVE);
                                categoryNumberItems.add(item);
                                DataContentProviderHelper.getInstance().insert(item);
                            }
                        }
                    } catch (RetrofitError er) {
                        if (er != null) {
                            if (er.getResponse() != null) {
                                Log.d("Retrofit",er.getResponse().getReason());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                return getCategoryCache(CATEGORY_TYPE_BLACKLIST);
        }

        return getCategoryCache(CATEGORY_TYPE_BLACKLIST);
    }

    @Override
    protected void onPreBackgroundTask() {
        super.onPreBackgroundTask();
        switch (mInstance.mProgressRunning) {
            case POPULATE_CATEGORY_NUMBER:
                if (mListener != null) {
                    mListener.onPreBackgroundTask(POPULATE_CATEGORY_NUMBER);
                }
                break;
        }
    }

    @Override
    protected void onPostBackgroundTask(List<CategoryNumberItem> categoryNumberItems) {
        if (mInstance.mListener != null) {
            mInstance.mListener.onPostBackgroundTask(categoryNumberItems, mInstance.mProgressRunning);
        }
        mInstance.mProgressRunning = POPULATE_NONE;
    }



}
