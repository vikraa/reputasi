package com.reputasi.callblocker.view.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTabPager;

/**
 * Created by Vikraa on 2/11/2015.
 */
public class AccountFragment extends ReputasiTabPager {
    private Fragment[] mPagers;

    public AccountFragment prepared(Activity activity) {
        mPagers = new Fragment[] { new AccountReputasiFragment()/*, new AccountProfileFragment()*/ };
        setup(activity.getResources().getStringArray(R.array.account_tab), mPagers, new CustomPagerListener() {
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
