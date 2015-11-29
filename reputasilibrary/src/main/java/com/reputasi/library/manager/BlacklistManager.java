package com.reputasi.library.manager;

import android.text.TextUtils;
import android.util.Log;

import com.reputasi.library.ReputasiConstants;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.DataContentProvider;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.record.BlackListItem;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.database.table.TableBlackList;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.preference.PreferenceManager;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.BaseResponse;
import com.reputasi.library.rest.BaseResponseQueries;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;
import com.reputasi.library.rest.request.Blacklist;
import com.reputasi.library.rest.response.BlacklistResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 7/6/2015.
 */
public class BlacklistManager extends BaseManager<Void, List<BlackListItem>> {

    public static final int POPULATE_NONE = 0;
    public static final int POPULATE_BLACKLIST_FROM_SERVER = 10;
    public static final int SYNCHRONIZE_BLACKLIST = 20;
    public static final int POPULATE_BLACKLIST_FROM_LOCAL = 30;
    public static final int ADD_BLACKLIST_ITEM = 40;
    public static final int REMOVE_BLACKLIST_ITEM = 50;

    private static BlacklistManager mInstance;

    private ManagerListener mListener;
    private int mProgressRunning;

    public static BlacklistManager getInstance() {
        if (mInstance == null) {
            mInstance = new BlacklistManager();
        }
        return mInstance;
    }

    @Override
    protected void onPreBackgroundTask() {
        super.onPreBackgroundTask();
        switch (mInstance.mProgressRunning) {
            case POPULATE_BLACKLIST_FROM_LOCAL:
                break;
            case POPULATE_BLACKLIST_FROM_SERVER:
                break;
            case SYNCHRONIZE_BLACKLIST:
                break;
        }
    }

    @Override
    protected List<BlackListItem> onBackgroundTask(Integer... params) {
        List<BlackListItem> result = new ArrayList<>();
        switch (params[0]) {
            case POPULATE_BLACKLIST_FROM_LOCAL:
                if (mInstance.mListener != null) {
                    List<BaseItem> items = DataContentProviderHelper.getInstance().queries(TableBlackList.TABLE_CODE, null, null, null);
                    if(items != null) {
                        for (BaseItem bi : items) {
                            BlackListItem blacklistItem = (BlackListItem)bi;
                            result.add(blacklistItem);
                        }
                    }
                }
                break;
            case SYNCHRONIZE_BLACKLIST:
                break;
        }
        return result;
    }

    @Override
    protected void onPostBackgroundTask(List<BlackListItem> blacklistItems) {
        if (mInstance.mListener != null) {
            mInstance.mListener.onPostBackgroundTask(blacklistItems, mInstance.mProgressRunning);
        }
        mInstance.mProgressRunning = POPULATE_NONE;
    }

    public BlacklistManager setListener (ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    public void fetchBlacklist() {
        mInstance.mProgressRunning = POPULATE_BLACKLIST_FROM_SERVER;
        if (ReputasiUtils.isNetworkConnected()) {
            RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
            client.requestAPI().rptsGetBlacklistNumber(ReputasiUtils.getDummyJson(),new Callback<BaseResponseQueries<BlacklistResponse>>() {
                @Override
                public void success(BaseResponseQueries<BlacklistResponse> result, Response response) {
                    List<BlacklistResponse> dataResponse = result.getData();
                    if (!dataResponse.isEmpty()) {
                        for (BlacklistResponse br : dataResponse) {
                            BlackListItem item = new BlackListItem();
                            item.setNumber(br.getPhoneNumber());
                            item.setName(br.getNumberName());
                            BaseItem baseItem = DataContentProviderHelper.getInstance().query(TableCategoryNumber.TABLE_CODE, TableCategoryNumber.CATEGORY_ID + " = ?",
                                    new String[] { TextUtils.isEmpty(br.getCategoryId()) ? "900001" : br.getCategoryId() }, null);
                            if (baseItem != null) {
                                item.setCategoryNumberName(((CategoryNumberItem)baseItem).getCategoryName()) ;
                            } else {
                                item.setCategoryNumberName("Lain Lain");
                            }
                            item.setName(ContactManager.getInstance().getContactBookName(br.getPhoneNumber()));
                            BaseItem blacklistItem = DataContentProviderHelper.getInstance().query(TableBlackList.TABLE_CODE, TableBlackList.BLACKLIST_NUMBER + " = ?", new String[] { br.getPhoneNumber() }, null);
                            if (blacklistItem != null) {
                                DataContentProviderHelper.getInstance().update(item);
                            } else {
                                DataContentProviderHelper.getInstance().insert(item);
                            }
                        }
                    }
                    if (mListener != null) {
                        mListener.onSuccess(null, mInstance.mProgressRunning);
                    }
                    mInstance.mProgressRunning = POPULATE_NONE;
                }

                @Override
                public void failure(RetrofitError error) {
                    if (mListener != null) {
                        mListener.onFailed(error, mInstance.mProgressRunning);
                    }
                    mInstance.mProgressRunning = POPULATE_NONE;
                }
            });
        } else {
            mListener.onSuccess(getBlacklistCache(), mInstance.mProgressRunning);
            mInstance.mProgressRunning = POPULATE_NONE;
        }

    }


    public List<BlackListItem> getBlacklistCache() {
        List<BlackListItem> blacklistItems = new ArrayList<>();
        List<BaseItem> baseItems = DataContentProviderHelper.getInstance().queries(TableBlackList.TABLE_CODE, null, null, null);
        if (baseItems != null) {
            for (BaseItem bi : baseItems) {
                BlackListItem item = (BlackListItem)bi;
                blacklistItems.add(item);
            }
        }
        return blacklistItems;
    }

    public void synchronizeBlacklist() {
        if (startBackgroundTask(SYNCHRONIZE_BLACKLIST)) {
            mInstance.mProgressRunning = SYNCHRONIZE_BLACKLIST;
        }
    }

    public void addBlacklist(final BlackListItem newItem, String categoryId) {
        if (newItem != null) {
            BaseItem item = DataContentProviderHelper.getInstance().query(TableBlackList.TABLE_CODE, TableBlackList.BLACKLIST_NUMBER + " = ?", new String[] { newItem.getNumber() }, null);
            if (item == null) {
                DataContentProviderHelper.getInstance().insert(newItem);
            }
        }
        Blacklist request = new Blacklist();
        request.setPhoneNumber(newItem.getNumber());
        /*request.setNumberName(newItem.getName());
        request.setCategoryId(categoryId);
        request.setUserName(UserPreferenceManager.getUserName());
        request.setSpecialFlag(0);
        HashMap<String, HashMap<String, Boolean>> acl = new HashMap<>();
        HashMap<String, Boolean> publicAcl = new HashMap<>();
        HashMap<String, Boolean> privateAcl = new HashMap<>();
        privateAcl.put("read", true);
        privateAcl.put("write", true);
        acl.put("*", publicAcl);
        acl.put(UserPreferenceManager.getObjectId(), privateAcl);
        request.setACL(acl);*/
        RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
        client.requestAPI().rptsPostBlacklistNumber(request, new Callback<BaseResponse>() {
            @Override
            public void success(BaseResponse baseResponse, Response response) {
                newItem.setSynchronizeStatus(ReputasiConstants.SYNCHRONIZE_STATUS_SUCCESS);
                DataContentProviderHelper.getInstance().update(newItem);
                if (mListener != null) {
                    mListener.onSuccess(baseResponse, ADD_BLACKLIST_ITEM);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                newItem.setSynchronizeStatus(ReputasiConstants.SYNCHRONIZE_STATUS_FAILED);
                DataContentProviderHelper.getInstance().update(newItem);
                if (mListener != null) {
                    mListener.onFailed(error, ADD_BLACKLIST_ITEM);
                }
            }
        });

    }

    public void removeBlacklist(BlackListItem removedItem) {
        Blacklist request = new Blacklist();
        request.setPhoneNumber(removedItem.getNumber());
        RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
        client.requestAPI().rptsPostDeleteBlacklistNumber(request, new Callback<BaseResponse>() {
            @Override
            public void success(BaseResponse baseResponse, Response response) {
                Log.d("removed","success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("removed","failed");
            }
        });
        if (removedItem != null) {
            DataContentProviderHelper.getInstance().delete(removedItem);
        }
    }
}
