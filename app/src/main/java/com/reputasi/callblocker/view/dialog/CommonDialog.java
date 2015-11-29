package com.reputasi.callblocker.view.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.callblocker.view.custom.ReputasiViewPager;
import com.reputasi.callblocker.view.fragment.BlacklistFragment;
import com.reputasi.callblocker.view.fragment.DialogSubmitCategoryFragment;
import com.reputasi.callblocker.view.fragment.DialogSubmitInternal;
import com.reputasi.callblocker.view.fragment.DialogSubmitManualFragment;
import com.reputasi.callblocker.view.utilities.AppConstant;
import com.reputasi.library.ReputasiConstants;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.record.BlackListItem;
import com.reputasi.library.database.record.ContactBookItem;
import com.reputasi.library.database.record.RecentNumberItem;
import com.reputasi.library.database.table.TableBlackList;
import com.reputasi.library.database.table.TableContactBook;
import com.reputasi.library.manager.BlacklistManager;
import com.reputasi.library.manager.ContactManager;
import com.reputasi.library.manager.ContributeManager;
import com.reputasi.library.manager.ManagerListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikraa on 5/17/2015.
 */
public class CommonDialog {

    public static class SubmitRecentCalls extends BaseDialog implements FragmentPagerListener {
        private ReputasiViewPager mPager;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View vwDialog = inflater.inflate(R.layout.dialog_common_submit, null);
            ReputasiTextView tvTitle = (ReputasiTextView)vwDialog.findViewById(R.id.tv_title);
            ImageView ivClose = (ImageView)vwDialog.findViewById(R.id.tv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tvTitle.setText(getResources().getString(R.string.title_common_dialog_add_blacklist));
            setCancelable(true);
            mPager = (ReputasiViewPager)vwDialog.findViewById(R.id.pager_dialog);
            Fragment pageHistoryList = new DialogSubmitInternal().setListener(this);
            Bundle arg = new Bundle();
            arg.putString(AppConstant.COMMON_DIALOG_TYPE, AppConstant.COMMON_DIALOG_RECENTCALLS);
            if (getArguments() != null) {
                arg.putBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB, getArguments().getBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB, true));
                String title = getArguments().getString(AppConstant.COMMON_DIALOG_TITLE);
                if (title != null) {
                    tvTitle.setText(title);
                }
            }
            pageHistoryList.setArguments(arg);
            Fragment[] pagers = { pageHistoryList, new DialogSubmitCategoryFragment().setListener(this)};
            mPager.setup(this, pagers, new String[]{"", ""});
            mPager.setWrapContent(true);
            mPager.setSwipeEnabled(false);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            return vwDialog;//super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onPagerEvent(Object obj, int tag) {
            if (tag == AppConstant.COMMON_ITEM_RECENTCALL_ITEM_SELECTED) {
                BlackListItem blackListItem = new BlackListItem();
                if (obj instanceof RecentNumberItem) {
                    RecentNumberItem item = (RecentNumberItem)obj;
                    String localName = ContactManager.getInstance().getContactBookName(item.getPhoneNumber());
                    blackListItem.setName(TextUtils.isEmpty(localName) ? item.getPhoneNumber() : localName);
                    blackListItem.setNumber(ReputasiUtils.validateNumber(item.getPhoneNumber()));
                    blackListItem.setCategoryNumberName("Lain Lain");
                }
                blackListItem.setSynchronizeStatus(ReputasiConstants.SYNCHRONIZE_STATUS_FAILED);
                BlacklistManager.getInstance().addBlacklist(blackListItem, "900001");
                if (mListener != null) {
                    mListener.OnItemClick(this, AppConstant.COMMON_ITEM_RECENTCALL_ITEM_SELECTED);
                }
            }
        }
    }

    public static class SubmitPhonebook extends BaseDialog implements FragmentPagerListener {
        private ReputasiViewPager mPager;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View vwDialog = inflater.inflate(R.layout.dialog_common_submit, null);
            ReputasiTextView tvTitle = (ReputasiTextView)vwDialog.findViewById(R.id.tv_title);
            ImageView ivClose = (ImageView)vwDialog.findViewById(R.id.tv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            setCancelable(true);
            mPager = (ReputasiViewPager)vwDialog.findViewById(R.id.pager_dialog);
            Fragment pagePhonebookList = new DialogSubmitInternal().setListener(this);
            Bundle arg = new Bundle();
            arg.putString(AppConstant.COMMON_DIALOG_TYPE, AppConstant.COMMON_DIALOG_PHONEBOOK);
            if (getArguments() != null) {
                arg.putBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB, getArguments().getBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB, true));
                String title = getArguments().getString(AppConstant.COMMON_DIALOG_TITLE);
                if (title != null) {
                    tvTitle.setText(title);
                }
            }
            pagePhonebookList.setArguments(arg);
            Fragment[] pagers = { pagePhonebookList,
                    new DialogSubmitCategoryFragment().setListener(this)};
            mPager.setup(this, pagers, new String[]{"", ""});
            mPager.setWrapContent(true);
            mPager.setSwipeEnabled(false);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            return vwDialog;//super.onCreateView(inflater, container, savedInstanceState);
        }



        @Override
        public void onPagerEvent(Object obj, int tag) {
            if (tag == AppConstant.COMMON_ITEM_PHONBOOK_ITEM_SELECTED) {
                BlackListItem blackListItem = new BlackListItem();
                if (obj instanceof ContactBookItem) {
                    ContactBookItem item = (ContactBookItem)obj;
                    blackListItem.setName(item.getContactName());
                    blackListItem.setNumber(ReputasiUtils.validateNumber(item.getContactNumber()));
                    blackListItem.setCategoryNumberName("Lain Lain");
                }
                blackListItem.setSynchronizeStatus(ReputasiConstants.SYNCHRONIZE_STATUS_FAILED);
                BlacklistManager.getInstance().addBlacklist(blackListItem, "900001");
                if (mListener != null) {
                    mListener.OnItemClick(this, AppConstant.COMMON_ITEM_PHONBOOK_ITEM_SELECTED);
                }
            }
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
        }
    }

    public static class SubmitManualDialog extends BaseDialog implements FragmentPagerListener {
        private ReputasiViewPager mPager;
        private Map<String, String> mContributeMap;

        public static String KEY_CONTRIBUTE_PHONE_NUMBER = "contribute_phone_number";
        public static String KEY_CONTRIBUTE_OWNER_NAME = "contribute_owner_name";
        public static String KEY_CONTRIBUTE_CATEGORY_ID = "contribute_category_id";
        public static String KEY_CONTRIBUTE_DESCRIPTION = "contribute_description";
        public static String KEY_CONTRIBUTE_REPUTATION = "contribute_reputation";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mContributeMap = new HashMap<>();
            View vwDialog = inflater.inflate(R.layout.dialog_common_submit, null);
            ImageView ivClose = (ImageView)vwDialog.findViewById(R.id.tv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            setCancelable(true);
            mPager = (ReputasiViewPager)vwDialog.findViewById(R.id.pager_dialog);
            Fragment[] pagers = { new DialogSubmitManualFragment().setListener(this),
                                  new DialogSubmitCategoryFragment().setListener(this)};
            mPager.setup(this, pagers, new String[]{"", ""});
            mPager.setWrapContent(true);
            mPager.setSwipeEnabled(false);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            return vwDialog;//super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onPagerEvent(Object obj, int tag) {
            if (tag == AppConstant.COMMON_DIALOG_NEXT_PAGE) {
                mContributeMap = (Map<String, String>)obj;
                mPager.nextPage();
            } else if (tag == AppConstant.COMMON_DIALOG_SKIP) {
                dismiss();
            } else if (tag == AppConstant.COMMON_DIALOG_SUBMIT) {
                Map<String, String> mapValues = (Map<String, String>)obj;
                mContributeMap.put(KEY_CONTRIBUTE_CATEGORY_ID, mapValues.get(KEY_CONTRIBUTE_CATEGORY_ID));
                mContributeMap.put(KEY_CONTRIBUTE_DESCRIPTION, mapValues.get(KEY_CONTRIBUTE_DESCRIPTION));
                ContributeManager.getInstance().addContributeNumber(mContributeMap.get(KEY_CONTRIBUTE_PHONE_NUMBER),
                        mContributeMap.get(KEY_CONTRIBUTE_OWNER_NAME),
                        mContributeMap.get(KEY_CONTRIBUTE_CATEGORY_ID),
                        mContributeMap.get(KEY_CONTRIBUTE_DESCRIPTION),
                        Integer.parseInt(mContributeMap.get(KEY_CONTRIBUTE_REPUTATION)));
                Toast.makeText(GlobalApplication.getContext(), "Submitted", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }

}
