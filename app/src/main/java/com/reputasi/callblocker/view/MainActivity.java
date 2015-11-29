package com.reputasi.callblocker.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.listener.FragmentPagerListener;
import com.reputasi.callblocker.view.fragment.AccountFragment;
import com.reputasi.callblocker.view.fragment.BlockFragment;
import com.reputasi.callblocker.view.fragment.ContributeFragment;
import com.reputasi.callblocker.view.fragment.DiscoverFragment;
import com.reputasi.callblocker.view.fragment.SearchFragment;
import com.reputasi.library.ReputasiConstants;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.table.TableContactBook;
import com.reputasi.library.manager.CategoryManager;
import com.reputasi.library.manager.ContactManager;
import com.reputasi.library.manager.ManagerListener;
import com.reputasi.library.manager.UserManager;
import com.reputasi.library.preference.PreferenceManager;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.response.CheckVersion;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 2/9/2015.
 */
public class MainActivity extends FragmentActivity implements
        FragmentPagerListener, ManagerListener {

    private static final int[] TAB_BAR_BUTTONS = { R.id.tab_1, R.id.tab_2,
            R.id.tab_3, /*R.id.tab_4, */R.id.tab_5 }; /* discover tab currently disabled */
    private Button[] mTabs = new Button[TAB_BAR_BUTTONS.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        for (int i = 0; i < mTabs.length; i++) {
            final int position = i;
            mTabs[i] = (Button)findViewById(TAB_BAR_BUTTONS[i]);
            mTabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedTab(position);
                }
            });
        }
        setSelectedTab(2);
        if (TextUtils.isEmpty(UserPreferenceManager.getSession())) {
            CategoryManager.getInstance().setListener(this).fetchCategoryNumber();
            ContactManager.getInstance().setListener(this).startSynchronizeLocal();
        }
//        ContactManager.getInstance().setListener(this).synchronizeBlacklist();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void setSelectedTab(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (Button b : mTabs) {
            b.setSelected(false);
        }
        Fragment container = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        mTabs[position].setSelected(true);
        switch (position) {
            case 0:
                if (!(container instanceof BlockFragment)) {
                    Fragment f = new BlockFragment().prepared(this);
                    ft.replace(R.id.fl_container, f);
                    ft.commit();
                }
                break;
            case 1:
                if (!(container instanceof ContributeFragment)) {
                    Fragment f = new ContributeFragment().prepared(this);
                    ft.replace(R.id.fl_container, f);
                    ft.commit();
                }
                break;
            case 2:
                if (!(container instanceof SearchFragment)){
                    Fragment f = new SearchFragment().prepared(this);
                    ft.replace(R.id.fl_container, f);
                    ft.commit();
                }
                break;
            /* discover tab currently disabled */
            /*case 3:
                if (!(container instanceof DiscoverFragment)) {
                    Fragment f = new DiscoverFragment().prepared(this);
                    ft.replace(R.id.fl_container, f);
                    ft.commit();
                }
                break;*/
            case 3:
                if (!(container instanceof AccountFragment)) {
                    Fragment f = new AccountFragment().prepared(this);
                    ft.replace(R.id.fl_container, f);
                    ft.commit();
                }
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!UserPreferenceManager.getDisclaimerAgreement()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_disclaimer, null);
            builder.setView(v)
                    .setTitle(getString(R.string.disclaimer_title))
                    .setPositiveButton(getString(R.string.disclaimer_button_agree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserPreferenceManager.putDisclaimerAgreement(true);
                            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.disclaimer_button_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserPreferenceManager.putDisclaimerAgreement(false);
                            System.exit(0);
                        }
                    })
                    .setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            try {
                ReputasiUtils.checkVersion(GlobalApplication.getContext().getPackageManager().getPackageInfo(getPackageName(), 0), new Callback<CheckVersion>() {
                    @Override
                    public void success(CheckVersion checkVersion, Response response) {
                        if (!isFinishing()) {
                            if (checkVersion.getResult().isForceUpdate()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Update");
                                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.card_force_update, null);
                                builder.setCancelable(false);
                                builder.setView(view);
                                final AlertDialog alert = builder.create();
                                ImageView iv = (ImageView) view.findViewById(R.id.btnPlayStore);
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert.dismiss();
                                        final String appPackageName = GlobalApplication.getContext().getPackageName();
                                        try {
                                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("market://details?id=" + appPackageName));
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            GlobalApplication.getContext().startActivity(intent);
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id="
                                                            + appPackageName));
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            GlobalApplication.getContext().startActivity(intent);
                                        }
                                    }
                                });
                                alert.show();
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (!isFinishing()) {
                            Log.d("failed", "unknown");
                        }
                    }
                });
                if (TextUtils.isEmpty(UserPreferenceManager.getSession())) {
                    String[] loginInfo = ReputasiUtils.getLoginInfo();
                    UserManager.getInstance().setListener(this).login(UserManager.LOGIN_TYPE_EMAIL, loginInfo[0], loginInfo[1]);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onPreBackgroundTask(int id) {

    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {
        if (!isFinishing()) {
            switch(id) {
                case ContactManager.SYNCHRONIZE_CONTACT_LOCAL_DATABASE:
                    ContactManager.getInstance().startSynchronizeServer();
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    break;
                case ContactManager.SYNCHRONIZE_CONTACT_SERVER_DATABASE:
                    break;
                case CategoryManager.POPULATE_CATEGORY_NUMBER:
                    break;
                default:
                    break;
            }
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

    @Override
    public void onPagerEvent(Object obj, int tag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalApplication.startBackgroundService();
    }
}
