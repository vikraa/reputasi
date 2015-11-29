package com.reputasi.callreputation.snslibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by vikraa on 3/2/2015.
 */
public class GoogleplusControl implements ConnectionCallbacks,
        OnConnectionFailedListener {

    //private final String OAUTH2 = "oauth2:server:client_id:";
    private final String OAUTH2 = "oauth2:client_id:";
    private final String API_SCOPE =":api_scope:" + Scopes.PLUS_LOGIN;
    private final String SCHEMAS = "http://schemas.google.com/AddActivity http://schemas.google.com/BuyActivity";
    private final String CLIENT_ID = "128332953241-00ka7tgdalqak76bn5fjkrk84misih5h.apps.googleusercontent.com";
    private static final int REQUEST_CODE_SIGN_IN = 0;
    private static final int REQUEST_CODE_TOKEN_AUTH = 1;

    public static final int LOGIN = 200;
    public static final int LOGOUT = 201;

    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress = false;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    private HashMap<String, String> mReturnData = new HashMap<>();
    private SnsController.SnsListener mListener;

    public GoogleplusControl(Activity activity) {
        this.mActivity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_TOKEN_AUTH) {
            if (responseCode == Activity.RESULT_OK) {
                intent.getExtras();
                getAndUseAuthTokenInAsyncTask();
            } else {
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                    /*if(listener!=null){
                        mReturnData.put(AppConstants.GOOGLE_PLUS_EXCEPTION, "Cancel");
                        listener.onLoginResult(LOGIN, mReturnData);
                    }*/
                }
            }
        } else if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (responseCode != Activity.RESULT_OK) {
                mSignInClicked = false;
                /*if(listener!=null){
                    mReturnData.put(AppConstants.GOOGLE_PLUS_EXCEPTION, "Cancel");
                    listener.onLoginResult(LOGIN, mReturnData);
                }*/
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    public void setListener(SnsController.SnsListener listener) {
        this.mListener = listener;
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(mActivity, REQUEST_CODE_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    void getAndUseAuthTokenInAsyncTask() {
        AsyncTask<Void, Void, HashMap<String, String>> task = new AsyncTask<Void, Void, HashMap<String, String>>() {
            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                String token = null;
                String gid = null;
                TimeZone tz = TimeZone.getDefault();
                String timezone = ""+ tz.getDisplayName(false, TimeZone.SHORT);


                try {
                    Bundle appActivities = new Bundle();
                    appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES, SCHEMAS);

                    token = GoogleAuthUtil.getToken( mActivity, Plus.AccountApi.getAccountName(mGoogleApiClient),
                            "oauth2:https://www.googleapis.com/auth/plus.login"/*OAUTH2+CLIENT_ID+API_SCOPE*/);
                    GoogleAuthUtil.invalidateToken(mActivity, token);

                    Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    String name = person.getDisplayName();
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                    gid = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getId();

                    //SLogger.logdeb("GID", gid + " > " +token);
                    Log.d("GooglePlusToken", token);

                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_ID, gid);
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_AUTH_TOKEN, token);
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_TIMEZONE, timezone);
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_DISPLAYNAME, name);
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_EMAIL, email);
                    mReturnData.remove(SnsController.SNS_GOOGLE_PLUS_EXCEPTION);
                } catch (IOException transientEx) {
                    // Network or server error, try later
                    //SLogger.logerr(this, transientEx.toString());
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_EXCEPTION, transientEx.toString());
                    Log.d("IOException", transientEx.getMessage());
                } catch (UserRecoverableAuthException e) {
                    // Recover (with e.getIntent())
                    //SLogger.logerr(this, e.toString());
                    Intent recover = e.getIntent();
                    mActivity.startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
                    Log.d("UserRecoverableAuthExc", e.getMessage());
                    return null;
                } catch (IllegalStateException exception) {
                    //SLogger.logerr(this, exception.toString());
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_EXCEPTION, exception.toString());
                    Log.d("IllegalStateException", exception.getMessage());
                } catch (GoogleAuthException authEx) {
                    // The call is not ever expected to succeed
                    // assuming you have already verified that
                    // Google Play services is installed.
                    //SLogger.logerr(this, authEx.toString());
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_EXCEPTION, authEx.toString());
                    Log.d("GoogleAuthException", authEx.getMessage());
                } catch (Exception exception){
                    //SLogger.logerr(this, exception.toString());
                    mReturnData.put(SnsController.SNS_GOOGLE_PLUS_EXCEPTION, exception.toString());
                    Log.d("Exception", exception.getMessage());
                }

                return mReturnData;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> userData) {
                //SLogger.logdeb(TAG, "Access token retrieved:" + userData);

                if(userData != null){
                    if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                    }

                    if(mListener!=null){
                        mListener.OnSnsResult(mReturnData, SnsController.SNS_GOOGLE_PLUS_LOGGED_IN);
                    }
                }
            }

        };
        task.execute();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if(mSignInClicked){
            mSignInClicked = false;
            getAndUseAuthTokenInAsyncTask();
        } else {
            signOutFromGplus();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    public void signInWithGplus() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }


    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            //listener.onLoginResult(LOGOUT, null);
            if(mListener!=null){
                mListener.OnSnsResult(mReturnData, SnsController.SNS_GOOGLE_PLUS_LOGGED_OUT);
            }
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), mActivity, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

}
