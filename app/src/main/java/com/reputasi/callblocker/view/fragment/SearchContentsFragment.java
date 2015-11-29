package com.reputasi.callblocker.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.SearchResultActivity;
import com.reputasi.callblocker.view.custom.ReputasiEditText;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.callblocker.view.utilities.DialogUtils;
import com.reputasi.library.database.record.SearchResultItem;
import com.reputasi.library.manager.ContactManager;
import com.reputasi.library.manager.ManagerListener;
import com.reputasi.library.manager.SearchManager;
import com.reputasi.library.rest.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 2/11/2015.
 */
public class SearchContentsFragment extends Fragment implements ManagerListener {
    private LinearLayout mLayoutHistory;
    private View[] mHistoryItem;
    private ProgressDialog mProgressDialog;
    private ReputasiEditText mEdtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_contents, null);
        mLayoutHistory = (LinearLayout)v.findViewById(R.id.ll_history);
        mHistoryItem = new View[] { v.findViewById(R.id.history1), v.findViewById(R.id.history2), v.findViewById(R.id.history3)};
        SearchManager.getInstance().setListener(this);
        initLayoutHistory();
        mEdtSearch = (ReputasiEditText)v.findViewById(R.id.edt_search);
        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (event != null &&  ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || (actionId == EditorInfo.IME_ACTION_DONE))) ||  (actionId == EditorInfo.IME_ACTION_DONE) ) {
                    SearchManager.getInstance().addHistory(mEdtSearch.getText().toString());
                    updateLayoutHistory();
                    SearchManager.getInstance().searchNumber(mEdtSearch.getText().toString());
                }
                return false;
            }
        });
        return v;
    }

    private void initLayoutHistory() {
        mLayoutHistory.setVisibility(View.GONE);
        for (View v : mHistoryItem) {
            v.setVisibility(View.GONE);
        }
        if (SearchManager.getInstance().getSearchHistory().size() > 0) {
            updateLayoutHistory();
        }
    }

    private void updateLayoutHistory() {
        List<String> historyList = SearchManager.getInstance().getSearchHistory();
        if (historyList != null && historyList.size() > 0) {
            int cnt = 0;
            for (String strItem : historyList) {
                if (cnt < mHistoryItem.length) {
                    ((ReputasiTextView)mHistoryItem[cnt].findViewById(R.id.tv_option_name)).setText(strItem);
                    mHistoryItem[cnt].setVisibility(View.VISIBLE);
                    mHistoryItem[cnt].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEdtSearch.setText(((ReputasiTextView)v.findViewById(R.id.tv_option_name)).getText().toString());
                            updateLayoutHistory();
                            SearchManager.getInstance().searchNumber(mEdtSearch.getText().toString());
                        }
                    });
                }
                cnt++;
            }
        }
        mLayoutHistory.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPreBackgroundTask(int id) {

    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {

    }

    @Override
    public void onEvent(Object ob, int id) {
        if (!isRemoving()) {
            if (id == SearchManager.SEARCH_START_SEARCHING_ITEM) {
                mProgressDialog = DialogUtils.showProgressDialog(SearchContentsFragment.this.getActivity(), false, null, "Searching");
            }
        }
    }

    @Override
    public void onSuccess(Object ob, int id) {
        if (!isRemoving()) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            if (id == SearchManager.SEARCH_RESULT_SEARCHING) {
                ArrayList<SearchResultItem> resultMap = (ArrayList<SearchResultItem>)ob;
                Intent searchResultIntent = new Intent(GlobalApplication.getContext(), SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("searchItem",resultMap);
                searchResultIntent.putExtra("searchItemList", bundle);
                mEdtSearch.getEditableText().clear();
                startActivity(searchResultIntent);
            }
        }
    }

    @Override
    public void onFailed(Object ob, int id) {
        if (!isRemoving()) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (id == SearchManager.SEARCH_RESULT_SEARCHING) {
                Toast.makeText(SearchContentsFragment.this.getActivity(), "Unable to find number", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
