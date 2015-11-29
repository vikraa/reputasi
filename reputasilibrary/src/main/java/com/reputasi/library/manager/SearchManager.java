package com.reputasi.library.manager;

import com.reputasi.library.ReputasiConstants;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.database.record.SearchResultItem;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 8/1/2015.
 */
public class SearchManager extends BaseManager<Void,Void> {

    public static int SEARCH_START_SEARCHING_ITEM = 0;
    public static int SEARCH_RESULT_SEARCHING = 1;

    private static SearchManager mInstance;
    private static List<String> mHistoryList;
    //private static List<SearchResultItem> mSearchResultItemList;
    private ManagerListener mListener;

    public static SearchManager getInstance() {
        if (mInstance == null) {
            mInstance = new SearchManager();
            mHistoryList = new ArrayList<>();
        }
        return mInstance;
    }

    public void setListener(ManagerListener listener) {
        mListener = listener;
    }

    public List<String> getSearchHistory() {
        return mHistoryList;
    }

    public void addHistory(String searchItem) {
        mHistoryList.add(0, searchItem);
    }

    public void searchNumber(String searchItem) {
        HashMap<String, String> searchRequest = new HashMap<>();
        searchRequest.put(ReputasiConstants.SEARCHNUMBER_PARAM_INCOMING_NUMBER, ReputasiUtils.validateNumber(searchItem == null ? "" : searchItem));
        searchRequest.put(ReputasiConstants.SEARCHNUMBER_PARAM_CALL_TYPE, ReputasiConstants.SEARCHNUMBER_TYPE_SEARCH_NUMBER);
        searchRequest.put(ReputasiConstants.SEARCHNUMBER_PARAM_CONNECTION_TYPE, ReputasiUtils.getConnectionType());
        if (mListener != null) {
            mListener.onEvent(null, SEARCH_START_SEARCHING_ITEM);
        }
        if (ReputasiUtils.isNetworkConnected()) {
            RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
            client.requestAPI().rptsPostSearchNumber(searchRequest, new Callback<HashMap<String, HashMap<String, Object>>>() {
                @Override
                public void success(HashMap<String, HashMap<String, Object>> resultMap, Response response) {
                    HashMap<String, Object> itemMap = resultMap.get("result");
                    ArrayList<SearchResultItem> resultItems = new ArrayList<SearchResultItem>();
                    SearchResultItem item = new SearchResultItem();
                    item.setPhoneNumber(itemMap.get("phoneNumber").toString());
                    item.setOwnerName(itemMap.get("ownerName").toString());
                    CategoryNumberItem categoryItem = (CategoryNumberItem)DataContentProviderHelper.getInstance().query(TableCategoryNumber.TABLE_CODE, TableCategoryNumber.CATEGORY_ID + " = ?", new String[] { String.valueOf(itemMap.get("category")) }, null);
                    item.setCategory(categoryItem == null ? "Unknown" : categoryItem.getCategoryName());
                    item.setScore(String.valueOf(itemMap.get("score") == null ? 0 : ((Double) itemMap.get("score")).intValue()));
                    item.setThumbUpScore(String.valueOf(itemMap.get("thumbUp") == null ? 0 : ((Double) itemMap.get("thumbUp")).intValue()));
                    item.setThumbDownScore(String.valueOf(itemMap.get("thumbDown") == null ? 0 : ((Double) itemMap.get("thumbDown")).intValue()));
                    resultItems.add(item);
                    if (mListener != null) {
                        mListener.onSuccess(resultItems, SEARCH_RESULT_SEARCHING);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (mListener != null) {
                        mListener.onFailed(error, SEARCH_RESULT_SEARCHING);
                    }
                }
            });
        } else {
            if (mListener != null) {
                mListener.onFailed(null, SEARCH_RESULT_SEARCHING);
            }
        }
    }


}
