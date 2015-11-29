package com.reputasi.callblocker.view.custom;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by vikraa on 2/10/2015.
 */
public class ReputasiViewPager extends ViewPager {

    private boolean mScrollingEnable, mWrapContent;
    private CustomAdapter mAdapter;
    private String[] mTitlePages;

    public ReputasiViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScrollingEnable = true;
    }

    public void setup(Fragment fragmentContainer, Fragment[] fragmentPages, String[] titles) {
        mAdapter = new CustomAdapter(fragmentContainer.getChildFragmentManager(), fragmentPages);
        mTitlePages = titles;
        setAdapter(mAdapter);
    }

    public void setup(FragmentManager manager, Fragment[] fragmentPages, String[] titles) {
        mAdapter = new CustomAdapter(manager, fragmentPages);
        mTitlePages = titles;
        setAdapter(mAdapter);
    }

    public void nextPage() {
        if (getCurrentItem() == mAdapter.getCount() - 1) {
            return;
        }
        setCurrentItem(getCurrentItem() + 1);
    }

    public void previousPage() {
        if (getCurrentItem() == 0) {
            return;
        }
        setCurrentItem(getCurrentItem() - 1);
    }

    public void firstPage() {
        if (mAdapter.getCount() == 1 || getCurrentItem() == 0) {
            return;
        }
        setCurrentItem(0);
    }

    public void lastPage() {
        if (mAdapter.getCount() == 1 || getCurrentItem() == mAdapter.getCount() - 1) {
            return;
        }
        setCurrentItem(mAdapter.getCount() - 1);
    }

    public String getTitlePage(int position) {
        if (position <= mTitlePages.length) {
            return mTitlePages[position];
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mScrollingEnable) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mScrollingEnable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setWrapContent(boolean wrapContent) {
        this.mWrapContent = wrapContent;
    }
    public void setSwipeEnabled(boolean enabled) {
        this.mScrollingEnable = enabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWrapContent) {
            int height = 0;
            for(int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if(h > height) height = h;
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private static class CustomAdapter extends FragmentPagerAdapter {
        private Fragment[] mFragmentPages;

        public CustomAdapter(FragmentManager fm, Fragment[] pages) {
            super(fm);
            mFragmentPages = pages;
        }

        @Override
        public Fragment getItem(int position) {
            if (position <= mFragmentPages.length) {
                return mFragmentPages[position];
            }
            return null;
        }

        @Override
        public int getCount() {
            return mFragmentPages == null ? 0 : mFragmentPages.length;
        }
    }

}
