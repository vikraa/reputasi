package com.reputasi.callblocker.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.adapter.CategoryNumberAdapter;
import com.reputasi.callblocker.view.custom.ReputasiEditText;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.callblocker.view.custom.ReputasiViewPager;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.database.record.RecentNumberItem;
import com.reputasi.library.manager.CallManager;
import com.reputasi.library.manager.CategoryManager;
import com.reputasi.library.manager.ContributeManager;
import com.reputasi.library.manager.ManagerListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vikraa on 8/29/2015.
 */
public class ContributeRecentNumberDialog extends BaseDialog implements ManagerListener {

    private ReputasiViewPager mPager;
    private Map<String, String> mContributeMap;
    public static int EVENT_CONTRIBUTE_THUMBS_UP = 1;
    public static int EVENT_CONTRIBUTE_THUMBS_DOWN = 0;
    public static int EVENT_CONTRIBUTE_CATEGORY_SELECTED = 10;
    public static int EVENT_CONTRIBUTE_SUBMITTED = 20;
    public static int EVENT_CONTRIBUTE_SKIPPED = 30;

    private String KEY_CONTRIBUTE_PHONE_NUMBER = "contribute_phone_number";
    private String KEY_CONTRIBUTE_OWNER_NAME = "contribute_owner_name";
    private String KEY_CONTRIBUTE_CATEGORY_ID = "contribute_category_id";
    private String KEY_CONTRIBUTE_DESCRIPTION = "contribute_description";
    private String KEY_CONTRIBUTE_REPUTATION = "contribute_reputation";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContributeMap = new HashMap<>();
        View v = inflater.inflate(R.layout.dialog_common_submit, null);
        ImageView ivClose = (ImageView)v.findViewById(R.id.tv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mPager = (ReputasiViewPager)v.findViewById(R.id.pager_dialog);
        FragmentPagerRecentCalls listRecentCallsFragment = new FragmentPagerRecentCalls();
        listRecentCallsFragment.setListener(this);
        FragmentPagerCategory listCategoryFragment = new FragmentPagerCategory();
        listCategoryFragment.setListener(this);
        Fragment[] pages = new Fragment[] { listRecentCallsFragment, listCategoryFragment};
        mPager.setup(this, pages, new String[] { "","" });
        mPager.setWrapContent(true);
        mPager.setSwipeEnabled(false);
        setCancelable(true);
        return v;
    }

    @Override
    public void onPreBackgroundTask(int id) {

    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {

    }

    @Override
    public void onEvent(Object ob, int id) {
        if (id == EVENT_CONTRIBUTE_THUMBS_DOWN || id == EVENT_CONTRIBUTE_THUMBS_UP) {
            mContributeMap.put(KEY_CONTRIBUTE_PHONE_NUMBER, (String)ob);
            mContributeMap.put(KEY_CONTRIBUTE_REPUTATION, String.valueOf(id));
            mPager.nextPage();
        } else if (id == EVENT_CONTRIBUTE_CATEGORY_SELECTED) {
            CategoryNumberItem categoryNumberItem = (CategoryNumberItem)ob;
            mContributeMap.put(KEY_CONTRIBUTE_CATEGORY_ID, categoryNumberItem.getCategoryId());
            mContributeMap.put(KEY_CONTRIBUTE_OWNER_NAME, categoryNumberItem.getCategoryName());
        } else if (id == EVENT_CONTRIBUTE_SKIPPED) {
            mContributeMap.clear();
            dismiss();
        } else if (id == EVENT_CONTRIBUTE_SUBMITTED) {
            ContributeManager.getInstance().setListener(this).addContributeNumber(
                    mContributeMap.get(KEY_CONTRIBUTE_PHONE_NUMBER),
                    mContributeMap.get(KEY_CONTRIBUTE_OWNER_NAME),
                    mContributeMap.get(KEY_CONTRIBUTE_CATEGORY_ID),
                    (String)ob,
                    Integer.parseInt(mContributeMap.get(KEY_CONTRIBUTE_REPUTATION)));
            Toast.makeText(getActivity(), "Thank you for your contribution", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    @Override
    public void onSuccess(Object ob, int id) {

    }

    @Override
    public void onFailed(Object ob, int id) {

    }

    public static class AdapterRecentCalls extends ArrayAdapter<RecentNumberItem> {
        private Context mContext;
        private List<RecentNumberItem> mValues;
        private ManagerListener mListener;

        public AdapterRecentCalls(Context context, List<RecentNumberItem> values) {
            super(context, R.layout.adapter_internal_item);
            mContext = context;
            mValues = values;
        }

        public void setListener(ManagerListener listener) {
            mListener = listener;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.adapter_internal_item, null);
                holder = new ViewHolder();
                holder.mNumber = (ReputasiTextView)v.findViewById(R.id.tv_phone_number);
                holder.mInfo = (ReputasiTextView)v.findViewById(R.id.tv_phone_info);
                holder.mThumbDown = (ImageView)v.findViewById(R.id.iv_thumbs_down);
                holder.mThumbUp = (ImageView)v.findViewById(R.id.iv_thumbs_up);
                v.setTag(holder);
            } else {
                holder = (ViewHolder)v.getTag();
            }

            holder.mNumber.setText(mValues.get(position).getPhoneNumber());
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            Date date = new Date(mValues.get(position).getStartTimestamp());
            String callDate = sdf.format(date);
            holder.mInfo.setText(callDate);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("contribute", "item selected");
                }
            });

            holder.mThumbUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onEvent(mValues.get(position).getPhoneNumber(), EVENT_CONTRIBUTE_THUMBS_UP);
                    }
                }
            });

            holder.mThumbDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onEvent(mValues.get(position).getPhoneNumber(), EVENT_CONTRIBUTE_THUMBS_DOWN);
                    }
                }
            });

            return v;
        }

        @Override
        public int getCount() {
            return mValues.size();
        }

        @Override
        public RecentNumberItem getItem(int position) {
            return mValues.get(position);
        }

        private static class ViewHolder {
            ReputasiTextView mNumber, mInfo;
            ImageView mThumbUp, mThumbDown;
        }
    }

    public static class FragmentPagerRecentCalls extends Fragment implements ManagerListener {

        private ManagerListener mListener;
        private ListView mListRecentCalls;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog_pager_input_internal, null);
            mListRecentCalls = (ListView)v.findViewById(R.id.lv_internal);
            CallManager.getInstance().setListener(this).startPopulateRecentCall();
            return v;
        }

        public void setListener(ManagerListener listener) {
            mListener = listener;
        }

        @Override
        public void onPreBackgroundTask(int id) {

        }

        @Override
        public void onPostBackgroundTask(Object ob, int id) {
            switch (id) {
                case CallManager.POPULATE_RECENTCALL_ITEMS :
                    AdapterRecentCalls adapter = new AdapterRecentCalls(GlobalApplication.getContext(), (List<RecentNumberItem>)ob);
                    adapter.setListener(mListener);
                    mListRecentCalls.setAdapter(adapter);
                    break;
                default:
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

    public static class FragmentPagerCategory extends Fragment {

        private ManagerListener mListener;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog_pager_category, null);
            final CategoryNumberAdapter adapter = new CategoryNumberAdapter(GlobalApplication.getContext(), CategoryManager.getInstance().getCategoryCache(CategoryManager.CATEGORY_TYPE_ALL));
            final Spinner spinner = (Spinner)v.findViewById(R.id.spinner_category);
            spinner.setAdapter(adapter);
            final ReputasiEditText editNote = (ReputasiEditText)v.findViewById(R.id.edt_note);
            Button btnSkip = (Button)v.findViewById(R.id.btn_skip);
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onEvent(null, EVENT_CONTRIBUTE_SKIPPED);
                    }
                }
            });
            Button btnSubmit = (Button)v.findViewById(R.id.btn_submit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onEvent(adapter.getItem(spinner.getSelectedItemPosition()), EVENT_CONTRIBUTE_CATEGORY_SELECTED);
                        mListener.onEvent(editNote.getText().toString(), EVENT_CONTRIBUTE_SUBMITTED);
                    }
                }
            });
            return v;
        }

        public void setListener(ManagerListener listener) {
            mListener = listener;
        }
    }
}
