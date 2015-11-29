package com.reputasi.callblocker.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.utilities.DialogUtils;
import com.reputasi.library.manager.BlacklistManager;
import com.reputasi.library.manager.CategoryManager;
import com.reputasi.library.manager.ContactManager;
import com.reputasi.library.manager.ManagerListener;
import com.reputasi.library.manager.UserManager;
import com.reputasi.callreputation.snslibrary.SnsController;

/**
 * Created by vikraa on 2/9/2015.
 */
public class LoginActivity extends Activity implements ManagerListener {

    private EditText mEdtUserEmail, mEdtUserPassword;
    private UiLifecycleHelper mUiLifecycleHelper;
    private ProgressDialog mProgressDialog;
    private boolean mIsCategoryFinished, mBtnLoginPressed;
    private int mLoginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Session.StatusCallback fbCallback = (Session.StatusCallback) UserManager.getInstance().createSnsController(LoginActivity.this).getSnsCallback(SnsController.SNS_FACEBOOK);
        mUiLifecycleHelper = new UiLifecycleHelper(this, fbCallback);
        mUiLifecycleHelper.onCreate(savedInstanceState);
        mEdtUserEmail = (EditText)findViewById(R.id.edt_email_address);
        mEdtUserPassword = (EditText)findViewById(R.id.edt_password);
        CategoryManager.getInstance().setListener(this).fetchCategoryNumber();

        ImageButton ibFacebook = (ImageButton)findViewById(R.id.btn_sns_facebook);
        ibFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = DialogUtils.showProgressDialog(LoginActivity.this,false, getResources().getString(R.string.signing_up_progress_title), getResources().getString(R.string.signing_up_progress));
                mBtnLoginPressed = true;
                mLoginType = UserManager.LOGIN_TYPE_FACEBOOK;
                if (mIsCategoryFinished) {
                    UserManager.getInstance().setListener(LoginActivity.this).login(UserManager.LOGIN_TYPE_FACEBOOK, null, null);
                }
            }
        });

        ImageButton ibGplus = (ImageButton)findViewById(R.id.btn_sns_gplus);
        ibGplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = DialogUtils.showProgressDialog(LoginActivity.this,false, getResources().getString(R.string.signing_up_progress_title), getResources().getString(R.string.signing_up_progress));
                mBtnLoginPressed = true;
                mLoginType = UserManager.LOGIN_TYPE_GOOGLE_PLUS;
                if (mIsCategoryFinished) {
                    UserManager.getInstance().setListener(LoginActivity.this).login(UserManager.LOGIN_TYPE_GOOGLE_PLUS, null, null);
                }
            }
        });

        Button btnSignup = (Button)findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mEdtUserEmail.getText()) || TextUtils.isEmpty(mEdtUserPassword.getText())) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_signing_email_or_password),Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog = DialogUtils.showProgressDialog(LoginActivity.this,false, getResources().getString(R.string.signing_up_progress_title), getResources().getString(R.string.signing_up_progress));
                    mBtnLoginPressed = true;
                    mLoginType = UserManager.LOGIN_TYPE_EMAIL;
                    if (mIsCategoryFinished) {
                        UserManager.getInstance().setListener(LoginActivity.this).login(UserManager.LOGIN_TYPE_EMAIL, mEdtUserEmail.getText().toString(), mEdtUserPassword.getText().toString());
                    }
                }
            }
        });

    }


    @Override
    public void onPreBackgroundTask(int id) {

    }

    @Override
    public void onPostBackgroundTask(Object ob, int id) {
        switch (id) {
            case ContactManager.SYNCHRONIZE_CONTACT_LOCAL_DATABASE:
                mProgressDialog.dismiss();
                ContactManager.getInstance().startSynchronizeServer();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                break;
            case CategoryManager.POPULATE_CATEGORY_NUMBER:
                mIsCategoryFinished = true;
                break;
        }
    }

    @Override
    public void onEvent(Object ob, int id) {

    }

    @Override
    public void onSuccess(Object ob, int id) {
        switch (id) {
            case UserManager.LOGIN_TYPE_FACEBOOK:
            case UserManager.LOGIN_TYPE_GOOGLE_PLUS:
            case UserManager.LOGIN_TYPE_EMAIL:
            case UserManager.LOGIN_TYPE_TWITTER:
                ContactManager.getInstance().setListener(this).startSynchronizeLocal();
            case BlacklistManager.POPULATE_BLACKLIST_FROM_SERVER:
                /*mProgressDialog.dismiss();
                ContactManager.getInstance().startSynchronizeServer();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();*/
                break;
            case CategoryManager.POPULATE_CATEGORY_NUMBER:
                mIsCategoryFinished = true;
                if (mBtnLoginPressed) {
                    if (mLoginType == UserManager.LOGIN_TYPE_EMAIL) {
                        UserManager.getInstance().setListener(LoginActivity.this).login(UserManager.LOGIN_TYPE_EMAIL, mEdtUserEmail.getText().toString(), mEdtUserPassword.getText().toString());
                    } else if (mLoginType == UserManager.LOGIN_TYPE_GOOGLE_PLUS) {
                        UserManager.getInstance().setListener(LoginActivity.this).login(UserManager.LOGIN_TYPE_GOOGLE_PLUS, null, null);
                    } else  if (mLoginType == UserManager.LOGIN_TYPE_FACEBOOK) {
                        UserManager.getInstance().setListener(LoginActivity.this).login(UserManager.LOGIN_TYPE_FACEBOOK, null, null);
                    }
                }
                break;
        }
    }

    @Override
    public void onFailed(Object ob, int id) {
        switch (id) {
            case UserManager.LOGIN_TYPE_FACEBOOK:
            case UserManager.LOGIN_TYPE_GOOGLE_PLUS:
            case UserManager.LOGIN_TYPE_EMAIL:
            case UserManager.LOGIN_TYPE_TWITTER:
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_signing_general),Toast.LENGTH_SHORT).show();
                break;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUiLifecycleHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUiLifecycleHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUiLifecycleHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiLifecycleHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
        UserManager.getInstance().OnSnsGplusActivityResult(requestCode, resultCode, data);
    }
}
