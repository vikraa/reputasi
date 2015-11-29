package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
//import com.reputasi.callblocker.listener.FragmentPagerListener;
//import com.reputasi.callblocker.model.SpammerModel;
//import com.reputasi.callblocker.utilities.AppConstant;
import com.reputasi.callblocker.view.adapter.SpammerAdapter;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.database.record.SpammerNumberItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikraa on 2/11/2015.
 */
public class DiscoverTopSpammerFragment extends Fragment {

    private List<SpammerNumberItem> mValues;
    private ListView mListView;
    private LinearLayout mLayoutSpammer, mLayoutDetailSpammer, mLayoutBackListSpammer, mRefreshSpammerList;
    private ReputasiTextView mTvSpammerDetailName;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover_topspammers, null);
        mListView = (ListView)v.findViewById(R.id.lv_spammers);
        mLayoutSpammer = (LinearLayout)v.findViewById(R.id.ll_listspammer);
        mLayoutDetailSpammer = (LinearLayout)v.findViewById(R.id.ll_spammer_detail);
        mLayoutBackListSpammer = (LinearLayout)v.findViewById(R.id.ll_spammer_detail_back);
        mRefreshSpammerList = (LinearLayout)v.findViewById(R.id.ll_spamer_refresh);
        mTvSpammerDetailName = (ReputasiTextView)v.findViewById(R.id.tv_spammer_detail_name);
        mLayoutSpammer.setVisibility(View.VISIBLE);
        mLayoutDetailSpammer.setVisibility(View.GONE);
        createDummy();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final SpammerAdapter adapter = new SpammerAdapter(GlobalApplication.getContext(), mValues);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(GlobalApplication.getContexts(), "test", Toast.LENGTH_SHORT).show();
                mLayoutSpammer.setVisibility(View.GONE);
                mLayoutDetailSpammer.setVisibility(View.VISIBLE);
                mTvSpammerDetailName.setText(adapter.getItem(position).getSpammerName());
            }
        });
        mLayoutBackListSpammer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutSpammer.setVisibility(View.VISIBLE);
                mLayoutDetailSpammer.setVisibility(View.GONE);
            }
        });
        mRefreshSpammerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GlobalApplication.getContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDummy() {
        mValues = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SpammerNumberItem model = new SpammerNumberItem();
            model.setSpammerName("Dummy Test");
            model.setSpammerNumber("087880820196");
            model.setSpammerCategoryNumberName("Telemarketing");
            mValues.add(model);
        }
    }

}
