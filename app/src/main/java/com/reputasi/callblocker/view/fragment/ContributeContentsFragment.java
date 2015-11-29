package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.callblocker.view.dialog.CommonDialog;
import com.reputasi.callblocker.view.dialog.ContributePhonebookNumber;
import com.reputasi.callblocker.view.dialog.ContributeRecentNumberDialog;
import com.reputasi.callblocker.view.utilities.AppConstant;

/**
 * Created by Vikraa on 2/11/2015.
 */
public class ContributeContentsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String[] contributeOptions = getResources().getStringArray(R.array.contribute_option);
        View v = inflater.inflate(R.layout.fragment_contribute, null);
        View vwOptionSubmitNumber = v.findViewById(R.id.row_submit_number);
        View vwOptionSubmitPhoneBook = v.findViewById(R.id.row_submit_phonebook);
        View vwOptionSubmitRecentCall = v.findViewById(R.id.row_submit_recent_call);
        vwOptionSubmitNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog.SubmitManualDialog dialog = new CommonDialog.SubmitManualDialog();
                dialog.show(getFragmentManager(), "");
            }
        });
        vwOptionSubmitPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributePhonebookNumber dialog = new ContributePhonebookNumber();
                dialog.show(getFragmentManager(), "");

                /*CommonDialog.SubmitPhonebook dialog = new CommonDialog.SubmitPhonebook();
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB, true);
                bundle.putString(AppConstant.COMMON_DIALOG_TITLE, getResources().getString(R.string.title_common_dialog_submit_number));
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(),"");*/
            }
        });
        vwOptionSubmitRecentCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContributeRecentNumberDialog dialog = new ContributeRecentNumberDialog();
                dialog.show(getFragmentManager(), "");
                /*CommonDialog.SubmitRecentCalls dialog = new CommonDialog.SubmitRecentCalls();
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppConstant.COMMON_DIALOG_ENABLE_THUMB, true);
                bundle.putString(AppConstant.COMMON_DIALOG_TITLE, getResources().getString(R.string.title_common_dialog_submit_number));
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(),"");*/
            }
        });
        ReputasiTextView tvSubmitNumber = (ReputasiTextView)vwOptionSubmitNumber.findViewById(R.id.tv_row_title);
        ReputasiTextView tvSubmitPhoneBook = (ReputasiTextView)vwOptionSubmitPhoneBook.findViewById(R.id.tv_row_title);
        ReputasiTextView tvSubmitRecentCall = (ReputasiTextView)vwOptionSubmitRecentCall.findViewById(R.id.tv_row_title);
        ImageView ivSubmitNumber = (ImageView)vwOptionSubmitNumber.findViewById(R.id.iv_row_icon);
        ImageView ivSubmitPhoneBook = (ImageView)vwOptionSubmitPhoneBook.findViewById(R.id.iv_row_icon);
        ImageView ivSubmitRecentCall = (ImageView)vwOptionSubmitRecentCall.findViewById(R.id.iv_row_icon);

        ivSubmitNumber.setImageResource(R.drawable.icon_pencil);
        ivSubmitPhoneBook.setImageResource(R.drawable.icon_book);
        ivSubmitRecentCall.setImageResource(R.drawable.icon_phone);
        tvSubmitNumber.setText(contributeOptions[0]);
        tvSubmitRecentCall.setText(contributeOptions[1]);
        tvSubmitPhoneBook.setText(contributeOptions[2]);
        return v;
    }
}
