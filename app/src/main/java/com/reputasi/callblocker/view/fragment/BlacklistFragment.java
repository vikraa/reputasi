package com.reputasi.callblocker.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.adapter.BlacklistAdapter;
import com.reputasi.callblocker.view.dialog.BaseDialog;
import com.reputasi.callblocker.view.dialog.BlacklistDialog;
import com.reputasi.callblocker.view.dialog.CommonDialog;
import com.reputasi.callblocker.view.utilities.AppConstant;
import com.reputasi.callblocker.view.utilities.DialogUtils;
import com.reputasi.library.database.record.BlackListItem;
import com.reputasi.library.manager.BlacklistManager;
import com.reputasi.library.manager.ManagerListener;
import com.reputasi.library.rest.BaseRequest;
import com.reputasi.library.rest.BaseResponse;
import com.reputasi.library.rest.BaseResponseQueries;
import com.reputasi.library.rest.request.Blacklist;
import com.reputasi.library.rest.response.BlacklistResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikraa on 2/11/2015.
 */
public class BlacklistFragment extends Fragment implements BaseDialog.BaseDialogListener, ManagerListener {

    private BlacklistAdapter mAdapter;
    private Activity mActivity;
    private List<BlackListItem> mValues;
    private ListView mListView;
    private int mPositionSelected;
    private static String BLACKLIST_DIALOG_TITLE = "Add Blacklist";

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blacklist, null);
        View viewAddNew = v.findViewById(R.id.add_new);
        viewAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlacklistDialog.OptionsAddBlacklistItem dialog = new BlacklistDialog.OptionsAddBlacklistItem();
                dialog.setListener(BlacklistFragment.this);
                dialog.show(getFragmentManager(), "");
            }
        });
        mListView = (ListView)v.findViewById(R.id.lv_blacklist);
        mValues = new ArrayList<>();
        mAdapter = new BlacklistAdapter(getActivity(), mValues);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int row, long id) {
                mPositionSelected = row;
                BlacklistDialog.OptionsBlacklistItem dialog = new BlacklistDialog.OptionsBlacklistItem();
                dialog.setListener(BlacklistFragment.this);
                Bundle bundle = new Bundle();
                bundle.putString("title", mValues.get(row).getNumber());
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "");
                return true;
            }
        });
        BlacklistManager.getInstance().setListener(this).fetchBlacklist();
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void OnItemClick(BaseDialog dialog, int optionId) {
        dialog.dismiss();
        switch (optionId) {
            case AppConstant.BLACKLIST_DIALOG_REMOVE_ITEM:
                DialogUtils.showAlertDialog(getActivity(),
                        true, "Confirm removal",
                        "Are you sure want to remove " + mValues.get(mPositionSelected).getNumber() + " from blacklist ?",
                        "Confirmed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BlacklistManager.getInstance().removeBlacklist(mValues.get(mPositionSelected));
                                mValues.remove(mPositionSelected);
                                mAdapter.setValues(mValues);
                                //mAdapter.notifyDataSetChanged();
                            }
                        }, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                break;
            case AppConstant.BLACKLIST_DIALOG_NUMBER_DESCRIPTION:
                break;
            case AppConstant.BLACKLIST_DIALOG_DATABASE_CATEGORY:
                break;
            case AppConstant.BLACKLIST_DIALOG_REPORT_NUMBER:
                break;
            case AppConstant.BLACKLST_DIALOG_ADD_FROM_PHONE_BOOK:
                CommonDialog.SubmitPhonebook submitPhonebook = new CommonDialog.SubmitPhonebook();
                Bundle bundlePhonebook = new Bundle();
                bundlePhonebook.putString("title", BLACKLIST_DIALOG_TITLE);
                bundlePhonebook.putBoolean("enableThumb", false);
                submitPhonebook.setArguments(bundlePhonebook);
                submitPhonebook.setListener(this);
                submitPhonebook.show(getFragmentManager(), AppConstant.COMMON_DIALOG_PHONEBOOK);
                break;
            case AppConstant.BLACKLIST_DIALOG_ADD_FROM_RECENT_NUMBER:
                CommonDialog.SubmitRecentCalls submitRecentCalls = new CommonDialog.SubmitRecentCalls();
                Bundle bundleRecentCall = new Bundle();
                bundleRecentCall.putString("title", BLACKLIST_DIALOG_TITLE);
                bundleRecentCall.putBoolean("enableThumb", false);
                submitRecentCalls.setArguments(bundleRecentCall);
                submitRecentCalls.setListener(this);
                submitRecentCalls.show(getFragmentManager(), AppConstant.COMMON_DIALOG_RECENTCALLS);
                break;
            case AppConstant.COMMON_ITEM_RECENTCALL_ITEM_SELECTED:
            case AppConstant.COMMON_ITEM_PHONBOOK_ITEM_SELECTED:
                mValues = BlacklistManager.getInstance().getBlacklistCache();
                mAdapter.setValues(mValues);
                break;
        }
    }

    @Override
    public void onPreBackgroundTask(int id) {

    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {
        /*if (id == BlacklistManager.POPULATE_BLACKLIST_FROM_LOCAL) {
            mValues = (List<BlackListItem>)ob;
            if (mValues != null) {
                mAdapter = new BlacklistAdapter (mActivity, mValues);
                mListView.setAdapter(mAdapter);
                mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int row, long id) {
                        mPositionSelected = row;
                        BlacklistDialog.OptionsBlacklistItem dialog = new BlacklistDialog.OptionsBlacklistItem();
                        dialog.setListener(BlacklistFragment.this);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", mValues.get(row).getNumber());
                        dialog.setArguments(bundle);
                        dialog.show(getFragmentManager(), "");
                        return true;
                    }
                });
            }
        }*/
    }

    @Override
    public void onEvent(Object ob, int id) {

    }

    @Override
    public void onSuccess(Object ob, int id) {
        if (!isRemoving()) {
            switch (id) {
                case BlacklistManager.POPULATE_BLACKLIST_FROM_SERVER:
                    mValues = BlacklistManager.getInstance().getBlacklistCache();
                    if (mAdapter != null) {
                        mAdapter.setValues(mValues);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailed(Object ob, int id) {
        switch (id) {
            case BlacklistManager.POPULATE_BLACKLIST_FROM_SERVER:
                break;
        }
    }

}
