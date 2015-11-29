package com.reputasi.callblocker.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.callblocker.view.utilities.AppConstant;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vikraa on 5/17/2015.
 */
public class BlacklistDialog {

    public static class OptionsBlacklistItem extends BaseDialog {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View vw = inflater.inflate(R.layout.dialog_option_blacklist, null);
            ReputasiTextView vwTitle = (ReputasiTextView)vw.findViewById(R.id.tv_title);
            vwTitle.setText(getArguments().getString("title"));
            View vwOption1 = vw.findViewById(R.id.option_menu_1);
            //View vwOption2 = vw.findViewById(R.id.option_menu_2);
            //View vwOption3 = vw.findViewById(R.id.option_menu_3);
            //View vwOption4 = vw.findViewById(R.id.option_menu_4);

            ((ImageView)vwOption1.findViewById(R.id.iv_option_menu)).setImageResource(R.drawable.icon_remove);
            //((ImageView)vwOption2.findViewById(R.id.iv_option_menu)).setImageResource(R.drawable.icon_description);
            //((ImageView)vwOption3.findViewById(R.id.iv_option_menu)).setImageResource(R.drawable.icon_database);
            //((ImageView)vwOption4.findViewById(R.id.iv_option_menu)).setImageResource(R.drawable.icon_report);

            vwOption1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(OptionsBlacklistItem.this, AppConstant.BLACKLIST_DIALOG_REMOVE_ITEM);
                }
            });
            /*vwOption2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(OptionsBlacklistItem.this, AppConstant.BLACKLIST_DIALOG_NUMBER_DESCRIPTION);
                }
            });*/
            /*vwOption3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(OptionsBlacklistItem.this, AppConstant.BLACKLIST_DIALOG_DATABASE_CATEGORY);
                }
            });
            vwOption4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(OptionsBlacklistItem.this, AppConstant.BLACKLIST_DIALOG_REPORT_NUMBER);
                }
            });*/
            List<String> options = Arrays.asList(getActivity().getResources().getStringArray(R.array.blacklist_popup));
            ((ReputasiTextView)vwOption1.findViewById(R.id.tv_option_name)).setText(options.get(0));
            //((ReputasiTextView)vwOption2.findViewById(R.id.tv_option_name)).setText(options.get(1));
            /*((ReputasiTextView)vwOption3.findViewById(R.id.tv_option_name)).setText(options.get(2));
            ((ReputasiTextView)vwOption4.findViewById(R.id.tv_option_name)).setText(options.get(3));*/
            return vw;
        }

    }

    public static class OptionsAddBlacklistItem extends BaseDialog {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View vw = inflater.inflate(R.layout.dialog_addnew_blacklist, null);
            ReputasiTextView vwTitle = (ReputasiTextView)vw.findViewById(R.id.tv_title);
            vwTitle.setText("Insert From");
            View vwOption1 = vw.findViewById(R.id.option_menu_1);
            View vwOption2 = vw.findViewById(R.id.option_menu_2);
            ((ImageView)vwOption1.findViewById(R.id.iv_option_menu)).setImageResource(R.drawable.icon_book);
            ((ImageView)vwOption2.findViewById(R.id.iv_option_menu)).setImageResource(R.drawable.icon_phone);
            ((ReputasiTextView)vwOption1.findViewById(R.id.tv_option_name)).setText("Phone book");
            ((ReputasiTextView)vwOption2.findViewById(R.id.tv_option_name)).setText("Recent calls");
            vwOption1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(OptionsAddBlacklistItem.this,AppConstant.BLACKLST_DIALOG_ADD_FROM_PHONE_BOOK);
                }
            });
            vwOption2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClick(OptionsAddBlacklistItem.this,AppConstant.BLACKLIST_DIALOG_ADD_FROM_RECENT_NUMBER);
                }
            });
            return vw;
        }
    }

}
