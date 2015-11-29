package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
import com.reputasi.callblocker.view.custom.ReputasiEditText;
import com.reputasi.callblocker.view.dialog.CommonDialog;
import com.reputasi.callblocker.view.utilities.AppConstant;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.rest.response.CheckVersion;

import java.util.HashMap;
import java.util.Map;
//import com.reputasi.callblocker.utilities.AppConstant;
//import com.reputasi.callblocker.view.custom.ReputasiViewPager;

/**
 * Created by vikraa on 5/17/2015.
 */
public class DialogSubmitManualFragment extends Fragment {

    private FragmentPagerListener mListener;
    public static int EVENT_CONTRIBUTE_THUMBS_UP = 1;
    public static int EVENT_CONTRIBUTE_THUMBS_DOWN = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_pager_input_submitmanual, null);
        final ImageView ivThumbUp = (ImageView)v.findViewById(R.id.iv_thumbs_up);
        final ImageView ivThumbDown = (ImageView)v.findViewById(R.id.iv_thumbs_down);
        final ReputasiEditText edtPhoneNumber = (ReputasiEditText)v.findViewById(R.id.edt_number);
        final ReputasiEditText edtOwnerName = (ReputasiEditText)v.findViewById(R.id.edt_name);
        final Map<String, String> mapValues = new HashMap<String, String>();

        ivThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapValues.put(CommonDialog.SubmitManualDialog.KEY_CONTRIBUTE_REPUTATION, String.valueOf(EVENT_CONTRIBUTE_THUMBS_UP));
            }
        });
        ivThumbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapValues.put(CommonDialog.SubmitManualDialog.KEY_CONTRIBUTE_REPUTATION, String.valueOf(EVENT_CONTRIBUTE_THUMBS_DOWN));
            }
        });
        Button btnNext = (Button)v.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mapValues.put(CommonDialog.SubmitManualDialog.KEY_CONTRIBUTE_PHONE_NUMBER, ReputasiUtils.validateNumber(edtPhoneNumber.getText().toString()));
                    mapValues.put(CommonDialog.SubmitManualDialog.KEY_CONTRIBUTE_OWNER_NAME, edtOwnerName.getText().toString());
                    mListener.onPagerEvent(mapValues, AppConstant.COMMON_DIALOG_NEXT_PAGE);
                }
            }
        });
        return v;
    }

    public DialogSubmitManualFragment setListener(FragmentPagerListener listener) {
        this.mListener = listener;
        return this;
    }
}
