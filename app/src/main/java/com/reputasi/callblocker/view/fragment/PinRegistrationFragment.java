package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;

/**
 * Created by vikraa on 5/30/2015.
 */
public class PinRegistrationFragment extends Fragment {

    private FragmentPagerListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration_pin, null);
        return v;
    }

    public PinRegistrationFragment setPagerEventListener(FragmentPagerListener listener) {
        mListener = listener;
        return this;
    }
}
