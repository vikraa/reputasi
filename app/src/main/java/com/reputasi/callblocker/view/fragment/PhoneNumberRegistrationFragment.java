package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
import com.reputasi.callblocker.view.custom.ReputasiEditText;
import com.reputasi.callblocker.view.utilities.AppConstant;

/**
 * Created by vikraa on 5/30/2015.
 */
public class PhoneNumberRegistrationFragment extends Fragment {
    FragmentPagerListener mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration_number, null);
        ImageButton btnInputNumberNext = (ImageButton)v.findViewById(R.id.btn_number_next);
        final ReputasiEditText edtInputNumber = (ReputasiEditText)v.findViewById(R.id.edt_input_number);
        btnInputNumberNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtInputNumber.getText())) {
                    if (mListener != null) {
                        mListener.onPagerEvent(edtInputNumber.getText(), AppConstant.REGISTRATION_PHONE_NUMBER_SUBMITTED);
                    }
                } else {
                    Toast.makeText(GlobalApplication.getContext(), "Invalid phone number" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public PhoneNumberRegistrationFragment setPagerEventListener(FragmentPagerListener listener) {
        mListener = listener;
        return this;
    }
}
