package com.reputasi.callblocker.view.custom;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.reputasi.callblocker.R;

/**
 * Created by Vikraa on 2/11/2015.
 */
public class ReputasiTabPager extends Fragment {

    private Activity mActivity;
    private String[] mTabTitles;
    private ReputasiViewPager mViewPager;
    private CustomPagerListener mListener;
    private LinearLayout mTabLayout;
    private Fragment[] mPagerFragments;
    private View[] mVwTabs, mVwSparators, /*mVwIndicatorsNormal,*/ mVwIndicatorsSelected, mVwTabTitles;
    private View mVwIndicatorNormal;

    public static interface CustomPagerListener {
        public void OnPagerScrolled(int position, float positionOffset, int positionOffsetPixels);
        public void OnPagerSelected(int position);
        public void OnPagerScrollStateChanged(int state);
        public void OnTabClicked(View v, int tabPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_custom_tabpager, null);
        mTabLayout = (LinearLayout)v.findViewById(R.id.ll_tab_indicator);

        mVwTabs = new View[] { v.findViewById(R.id.rl_tab_1), v.findViewById(R.id.rl_tab_2),
                v.findViewById(R.id.rl_tab_3), v.findViewById(R.id.rl_tab_4)};
        mVwSparators = new View[] { v.findViewById(R.id.vw_sparator_1), v.findViewById(R.id.vw_sparator_2),
                v.findViewById(R.id.vw_sparator_3)};
       /* mVwIndicatorsNormal = new View[] { v.findViewById(R.id.vw_indicator_normal_1), v.findViewById(R.id.vw_indicator_normal_2),
                v.findViewById(R.id.vw_indicator_normal_3), v.findViewById(R.id.vw_indicator_normal_4)};*/
        mVwIndicatorNormal = v.findViewById(R.id.vw_indicator_normal);
        mVwIndicatorsSelected = new View[] { v.findViewById(R.id.vw_indicator_selected_1), v.findViewById(R.id.vw_indicator_selected_2),
                v.findViewById(R.id.vw_indicator_selected_3), v.findViewById(R.id.vw_indicator_selected_4)};
        mVwTabTitles = new View[] { v.findViewById(R.id.tab_title_1), v.findViewById(R.id.tab_title_2),
                v.findViewById(R.id.tab_title_3), v.findViewById(R.id.tab_title_4)};
        mViewPager = (ReputasiViewPager)v.findViewById(R.id.pager);
        mViewPager.setup(this, mPagerFragments, mTabTitles);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (mListener != null) {
                    mListener.OnPagerScrolled(i, v, i2);
                }
            }

            @Override
            public void onPageSelected(int i) {
                updateSelectedTab(i);
                if (mListener != null) {
                    mListener.OnPagerSelected(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (mListener != null) {
                    mListener.OnPagerScrollStateChanged(i);
                }
            }
        });

        for (View vw : mVwTabs) {
            vw.setVisibility(View.GONE);
        }
        for (View vw : mVwSparators) {
            vw.setVisibility(View.GONE);
        }

        if (mTabTitles != null) {
           int tabCount = mTabTitles.length;
           if (tabCount > 0) {
               for (int i = 0; i < tabCount; i++) {
                   final int position = i;
                   mVwTabs[i].setVisibility(View.VISIBLE);
                   /*mVwIndicatorsNormal[i].setVisibility(View.VISIBLE);*/
                   mVwIndicatorsSelected[i].setVisibility(View.GONE);

                   final ReputasiTextView tabTv = (ReputasiTextView)mVwTabTitles[i];
                   tabTv.setText(mTabTitles[i]);
                   tabTv.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           /*mVwIndicatorsNormal[position].setVisibility(View.GONE);*/
                           mVwIndicatorsSelected[position].setVisibility(View.VISIBLE);
                           if (mListener != null) {
                               updateSelectedTab(position);
                               mViewPager.setCurrentItem(position);
                               mListener.OnTabClicked(tabTv, position);
                           }
                       }
                   });
               }
               int sparatorCount = tabCount - 1;
               for (int i = 0; i < sparatorCount; i++) {
                   mVwSparators[i].setVisibility(View.VISIBLE);
               }
           }

           mViewPager.setCurrentItem(0);
           updateSelectedTab(0);
        }

        return v;
    }

    public void setup(String[] titles, Fragment[] pagerFragments, CustomPagerListener listener) {
        mTabTitles = titles;
        mListener = listener;
        mPagerFragments = pagerFragments;
    }

    public void setHeaderVisible(boolean visibility) {
        if (visibility) {
            mVwIndicatorNormal.setVisibility(View.VISIBLE);
            if (mVwTabs != null) {
                for (View vw : mVwTabs) {
                    vw.setVisibility(View.VISIBLE);
                }
            }
            if (mVwSparators != null) {
                for (View vw : mVwSparators) {
                    vw.setVisibility(View.VISIBLE);
                }
            }
            if (mVwIndicatorsSelected != null) {
                for (View vw : mVwIndicatorsSelected) {
                    vw.setVisibility(View.VISIBLE);
                }
            }
            if (mVwTabTitles != null) {
                for (View vw : mVwTabTitles) {
                    vw.setVisibility(View.VISIBLE);
                }
            }
        } else {
            mVwIndicatorNormal.setVisibility(View.GONE);
            if (mVwTabs != null) {
                for (View vw : mVwTabs) {
                    vw.setVisibility(View.GONE);
                }
            }
            if (mVwSparators != null) {
                for (View vw : mVwSparators) {
                    vw.setVisibility(View.GONE);
                }
            }
            if ( mVwIndicatorsSelected != null) {
                for (View vw : mVwIndicatorsSelected) {
                    vw.setVisibility(View.GONE);
                }
            }
            if (mVwTabTitles != null) {
                for (View vw : mVwTabTitles) {
                    vw.setVisibility(View.GONE);
                }
            }
        }

    }


    private void updateSelectedTab(int position) {
        for (int i = 0; i < mTabTitles.length; i++) {
            ReputasiTextView tv = (ReputasiTextView)mVwTabTitles[i];
            //tv.setTextAppearance(mActivity, R.style.customTabbarTitleTextBold);
            if (i == position) {
                /*mVwIndicatorsNormal[i].setVisibility(View.GONE);*/
                mVwIndicatorsSelected[i].setVisibility(View.VISIBLE);
            } else {
                /*mVwIndicatorsNormal[i].setVisibility(View.VISIBLE);*/
                mVwIndicatorsSelected[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
