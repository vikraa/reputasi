package com.reputasi.callblocker.view.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTabPager;

/**
 * Created by vikraa on 2/9/2015.
 */
public class BlockFragment extends ReputasiTabPager {

    private Fragment[] mPagers;

    public BlockFragment prepared(Activity activity) {
        mPagers = new Fragment[] { new BlacklistFragment(), new BlacklistCategoryFragment() };
        setup(activity.getResources().getStringArray(R.array.block_tab), mPagers, new CustomPagerListener() {
            @Override
            public void OnPagerScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void OnPagerSelected(int position) {

            }

            @Override
            public void OnPagerScrollStateChanged(int state) {

            }

            @Override
            public void OnTabClicked(View v, int tabPosition) {

            }
        });

        return this;
    }
}
