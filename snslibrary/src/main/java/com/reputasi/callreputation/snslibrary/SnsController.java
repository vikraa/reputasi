package com.reputasi.callreputation.snslibrary;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * Created by vikraa on 1/31/2015.
 */
public class SnsController {

    public static final int SNS_FACEBOOK = 0;
    public static final String SNS_FACEBOOK_LOGGED_IN = "sns_facebook_logged_in";
    public static final String SNS_FACEBOOK_EXCEPTION = "sns_facebook_exception";
    public static final String SNS_FACEBOOK_LOGGED_OUT = "sns_facebook_logged_out";
    public static final String SNS_FACEBOOK_CANCELED = "sns_facebook_canceled";

    public static final int SNS_TWITTER = 1;
    public static final String SNS_TWITTER_LOGGED_OUT = "sns_twitter_logged_out";
    public static final String SNS_TWITTER_LOGGED_IN = "sns_twitter_logged_in";

    public static final int SNS_GOOGLE_PLUS = 2;
    public static final String SNS_GOOGLE_PLUS_ID = "sns_google_plus_id";
    public static final String SNS_GOOGLE_PLUS_AUTH_TOKEN = "sns_google_plus_auth_token";
    public static final String SNS_GOOGLE_PLUS_EXCEPTION = "sns_google_plus_exception";
    public static final String SNS_GOOGLE_PLUS_TIMEZONE = "sns_google_plus_timezone";
    public static final String SNS_GOOGLE_PLUS_LOGGED_IN = "sns_google_plus_logged_in";
    public static final String SNS_GOOGLE_PLUS_LOGGED_OUT = "sns_google_plus_logged_out";
    public static final String SNS_GOOGLE_PLUS_DISPLAYNAME = "sns_google_plus_display_name";
    public static final String SNS_GOOGLE_PLUS_EMAIL = "sns_google_plus_email";

    public static FacebookControl mFacebookControl;
    public static TwitterControl mTwitterControl;
    public static GoogleplusControl mGplusControl;

    private static SnsController mInstance;
    private static Activity mActivity;
    private static SnsListener mListener;

    public static interface SnsListener {
        void OnSnsResult(Object object, String tag);
    }

    public static SnsController getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new SnsController();
            mFacebookControl = new FacebookControl(activity);
            mTwitterControl = new TwitterControl(new TwitterControl.SocialMediaCallback() {
                @Override
                public void onReturn(TwitterControl.SocialMediaResponse socialMediaResponse, Object... response) {
                    Log.d("twitter", "do something");
                }
            },activity);
            mGplusControl = new GoogleplusControl(activity);

        }
        mActivity = activity;
        if (mActivity != null) {
            mFacebookControl.setContext(mActivity);
            mTwitterControl.setActivity(mActivity);
            mGplusControl.setActivity(mActivity);
        }

        return mInstance;
    }

    public SnsController setListener(SnsListener listener) {
        mInstance.mListener = listener;
        mFacebookControl.setListener(mInstance.mListener);
        mTwitterControl.setListener(mInstance.mListener);
        mGplusControl.setListener(mInstance.mListener);
        return mInstance;
    }

    public void snsLogin(int snsType) {
        switch (snsType) {
            case SNS_FACEBOOK:
                mFacebookControl.loginFacebook(mActivity);
                break;
            case SNS_TWITTER:
                mTwitterControl.startAuth();
                break;
            case SNS_GOOGLE_PLUS:
                mGplusControl.signInWithGplus();
                break;
            default:
                break;
        }
    }

    public void snsLogout(int snsType) {
        switch (snsType) {
            case SNS_FACEBOOK:
                mFacebookControl.disconnectFacebook();
                break;
            case SNS_TWITTER:
                mTwitterControl.clearTwitterToken();
                break;
            case SNS_GOOGLE_PLUS:
                mGplusControl.signOutFromGplus();
                break;
            default:
                break;
        }
    }

    public void onGPlusActivityResult(int requestCode, int responseCode, Intent intent) {
        mGplusControl.onActivityResult(requestCode, responseCode, intent);
    }

    public Object snsGetCallback(int snsType){
        switch (snsType) {
            case SNS_FACEBOOK:
                return mFacebookControl.getCallback();
            case SNS_TWITTER:
                return null;
            case SNS_GOOGLE_PLUS:
                return null;
            default:
                return null;
        }
    }
}
