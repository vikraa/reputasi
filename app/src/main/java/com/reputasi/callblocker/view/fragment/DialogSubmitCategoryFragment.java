package com.reputasi.callblocker.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
//import com.reputasi.callblocker.managers.SpecialNumbersManager;
//import com.reputasi.callblocker.model.RecordCategoryNumberModel;
//import com.reputasi.callblocker.utilities.AppConstant;
import com.reputasi.callblocker.view.adapter.CategoryNumberAdapter;
import com.reputasi.callblocker.view.custom.ReputasiEditText;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.callblocker.view.dialog.CommonDialog;
import com.reputasi.callblocker.view.utilities.AppConstant;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.manager.CategoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vikraa on 5/17/2015.
 */
public class DialogSubmitCategoryFragment extends Fragment {

    private FragmentPagerListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_pager_category, null);
        final Spinner spinnerCategory = (Spinner)v.findViewById(R.id.spinner_category);
        final ReputasiEditText edtNote = (ReputasiEditText)v.findViewById(R.id.edt_note);
        final List<CategoryNumberItem> values = CategoryManager.getInstance().getCategoryCache(CategoryManager.CATEGORY_TYPE_ALL);
        final CategoryNumberAdapter categoryAdapter = new CategoryNumberAdapter(GlobalApplication.getContext(), values);

        spinnerCategory.setAdapter(categoryAdapter);
        Button btnSubmit = (Button)v.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Map<String, String> contributeMap = new HashMap<>();
                    contributeMap.put(CommonDialog.SubmitManualDialog.KEY_CONTRIBUTE_CATEGORY_ID, categoryAdapter.getItem(spinnerCategory.getSelectedItemPosition()).getCategoryId());
                    contributeMap.put(CommonDialog.SubmitManualDialog.KEY_CONTRIBUTE_DESCRIPTION, edtNote.getText().toString());
                    mListener.onPagerEvent(contributeMap, AppConstant.COMMON_DIALOG_SUBMIT);
                }
            }
        });
        Button btnSkip = (Button)v.findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPagerEvent(v, AppConstant.COMMON_DIALOG_SKIP);
                }
            }
        });

        return v;
    }

    public DialogSubmitCategoryFragment setListener(FragmentPagerListener listener) {
        this.mListener = listener;
        return this;
    }

}
