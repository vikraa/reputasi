package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
import com.reputasi.callblocker.view.adapter.CommonContactBookAdapter;
import com.reputasi.callblocker.view.adapter.CommonRecentNumberAdapter;
import com.reputasi.callblocker.view.utilities.AppConstant;
import com.reputasi.library.database.record.ContactBookItem;
import com.reputasi.library.database.record.RecentNumberItem;
import com.reputasi.library.manager.CallManager;
import com.reputasi.library.manager.ContactManager;
import com.reputasi.library.manager.ManagerListener;

import java.util.List;

/**
 * Created by Vikraa on 5/20/2015.
 */
public class DialogSubmitInternal extends Fragment implements ManagerListener {

    private FragmentPagerListener mListener;
    private List<ContactBookItem> mContactBookItems;
    private List<RecentNumberItem> mRecentNumberItems;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_pager_input_internal, null);
        mListView = (ListView)v.findViewById(R.id.lv_internal);
        if (getArguments().getString(AppConstant.COMMON_DIALOG_TYPE).equalsIgnoreCase(AppConstant.COMMON_DIALOG_PHONEBOOK)) {
            ContactManager.getInstance().setListener(this).startSynchronizeLocal();
        } else if (getArguments().getString(AppConstant.COMMON_DIALOG_TYPE).equalsIgnoreCase(AppConstant.COMMON_DIALOG_RECENTCALLS)) {
            CallManager.getInstance().setListener(this).startPopulateRecentCall();
        }
        return v;
    }


    public DialogSubmitInternal setListener(FragmentPagerListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public void onPreBackgroundTask(int id) {

    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {
        if (!isRemoving()) {
            switch(id) {
                case CallManager.POPULATE_RECENTCALL_ITEMS:
                    mRecentNumberItems = (List<RecentNumberItem>)ob;
                    final CommonRecentNumberAdapter recentNumberAdapter = new CommonRecentNumberAdapter(mRecentNumberItems);
                    recentNumberAdapter.setEnableThumb(getArguments().getBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB,true));
                    recentNumberAdapter.setListener(mListener);
                    mListView.setAdapter(recentNumberAdapter);
                    break;
                case ContactManager.SYNCHRONIZE_CONTACT_LOCAL_DATABASE:
                    mContactBookItems = (List<ContactBookItem>)ob;
                    final CommonContactBookAdapter contactAdapter = new CommonContactBookAdapter(mContactBookItems);
                    contactAdapter.setEnableThumb(getArguments().getBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB, true));
                    contactAdapter.setListener(mListener);
                    mListView.setAdapter(contactAdapter);
                    break;
            }

        }

    }

    @Override
    public void onEvent(Object ob, int id) {

    }

    @Override
    public void onSuccess(Object ob, int id) {

    }

    @Override
    public void onFailed(Object ob, int id) {

    }
}
