package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.preference.UserPreferenceManager;

/**
 * Created by vikraa on 2/10/2015.
 */
public class AccountReputasiFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_reputasi, null);
        ReputasiTextView textName = (ReputasiTextView)v.findViewById(R.id.tv_account_number);
        textName.setText(UserPreferenceManager.getUserName());
        return v;
    }
}
