package com.reputasi.callblocker.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
import com.reputasi.callblocker.view.custom.ReputasiViewPager;
import com.reputasi.callblocker.view.fragment.PhoneNumberRegistrationFragment;
import com.reputasi.callblocker.view.fragment.PinRegistrationFragment;
import com.reputasi.callblocker.view.utilities.AppConstant;
import com.reputasi.callblocker.view.utilities.DialogUtils;
import com.reputasi.library.manager.CategoryManager;
import com.reputasi.library.manager.ContactManager;
import com.reputasi.library.manager.ManagerListener;

/**
 * Created by vikraa on 5/30/2015.
 */
public class RegistrationActivity extends FragmentActivity implements FragmentPagerListener, ManagerListener {

    private ReputasiViewPager mPager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mPager = (ReputasiViewPager)findViewById(R.id.fragment_pager);
        final Button btnPrev = (Button)findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.previousPage();
            }
        });
        final Button btnNext = (Button)findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = DialogUtils.showProgressDialog(RegistrationActivity.this, false, getResources().getString(R.string.registration_collect_information), getResources().getString(R.string.please_wait));
                ContactManager.getInstance().setListener(RegistrationActivity.this).startSynchronizeLocal();
            }
        });
        final Fragment[] fragments = { new PhoneNumberRegistrationFragment().setPagerEventListener(RegistrationActivity.this),
                new PinRegistrationFragment().setPagerEventListener(RegistrationActivity.this)};
        mPager.setup(getSupportFragmentManager(), fragments, new String[]{"", ""});
        mPager.setSwipeEnabled(false);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    btnNext.setVisibility(View.INVISIBLE);
                    btnPrev.setVisibility(View.INVISIBLE);
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onPagerEvent(Object obj, int tag) {
        switch (tag) {
            case AppConstant.REGISTRATION_PHONE_NUMBER_SUBMITTED:
                mPager.nextPage();
                break;
            case AppConstant.REGISTRATION_PIN_NUMBER_SUBMITTED:
                break;
        }

    }

    @Override
    public void onPreBackgroundTask(int id) {
        switch (id) {
            case ContactManager.SYNCHRONIZE_CONTACT_LOCAL_DATABASE:
                break;
            case CategoryManager.POPULATE_CATEGORY_NUMBER:
                break;
        }
    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {
        switch (id) {
            case ContactManager.SYNCHRONIZE_CONTACT_LOCAL_DATABASE:
                CategoryManager.getInstance().setListener(this).fetchCategoryNumber();
                break;
            case CategoryManager.POPULATE_CATEGORY_NUMBER:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                mProgressDialog.dismiss();
                finish();
                break;
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
