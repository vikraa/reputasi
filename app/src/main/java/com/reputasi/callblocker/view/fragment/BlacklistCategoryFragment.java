package com.reputasi.callblocker.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.reputasi.callblocker.R;
/*
import com.reputasi.callblocker.managers.SpecialNumbersManager;
import com.reputasi.callblocker.model.RecordCategoryNumberModel;
*/
import com.reputasi.callblocker.view.adapter.BlockCategoryAdapter;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.manager.CategoryManager;
import com.reputasi.library.manager.ManagerListener;

import java.util.List;

/**
 * Created by Vikraa on 2/11/2015.
 */
public class BlacklistCategoryFragment extends Fragment implements ManagerListener {

    private BlockCategoryAdapter mAdapter;
    private Activity mActivity;
    private List<CategoryNumberItem> mValues;
    private ListView mListView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blacklist_category, null);
        mListView = (ListView)v.findViewById(R.id.lv_categorylist);
        mAdapter = new BlockCategoryAdapter(mActivity, CategoryManager.getInstance().getCategoryCache(CategoryManager.CATEGORY_TYPE_BLACKLIST));
        mListView.setAdapter(mAdapter);
        CategoryManager.getInstance().setListener(this).fetchCategoryNumber();
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onPreBackgroundTask(int id) {
        if (!isRemoving()) {
            switch (id) {
                case CategoryManager.POPULATE_CATEGORY_NUMBER:
                    break;
            }
        }
    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {
        if (!isRemoving()) {
            switch (id) {
                case CategoryManager.POPULATE_CATEGORY_NUMBER:
                    //mAdapter = new BlockCategoryAdapter(mActivity, (List<CategoryNumberItem>)ob);
                    mAdapter.setValues((List<CategoryNumberItem>)ob);
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
