package com.reputasi.callreputation.snslibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.util.ArrayList;
import java.util.List;

public class FacebookControl implements Request.Callback {

    private static final String TAG = FacebookControl.class.getSimpleName();


    private Session.StatusCallback mFacebookCallback;
    private Activity mContext;
    private SnsController.SnsListener mListener;
    FragmentActivity mFActivity;
    private boolean mRequestPublish = false;

    public FacebookControl(Activity context) {
        //super(null, context);
        mContext = context;
        mFacebookCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };
    }

    public void setListener(SnsController.SnsListener listener) {
        this.mListener = listener;
    }

    public void setContext(Activity activity) {
        mContext = activity;
    }

    public void loginFacebook(Activity activity) {
        List<String> permission = new ArrayList<String>();
        permission.add("user_photos");
        permission.add("user_birthday");
        permission.add("email");
        permission.add("read_friendlists");
        openActiveSession(activity, true, mFacebookCallback, permission);
    }


    private Session openActiveSession(Activity activity, boolean allowLoginUI, Session.StatusCallback callback, List<String> permissions) {
        OpenRequest openRequest = new OpenRequest(activity).setPermissions(permissions).setCallback(callback);
        openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
        Session session;
        if (mRequestPublish) {
            session = Session.openActiveSessionFromCache(activity);
        } else {
            session = new Session.Builder(activity).build();
        }
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
            Session.setActiveSession(session);
            if (!session.isOpened()) {
                session.openForRead(openRequest);
            }
            return session;
        }

        return null;
    }

    public Session.StatusCallback getCallback() {
        return mFacebookCallback;
    }

    public boolean getRequestPublish() {
        return mRequestPublish;
    }

    public void setRequestPublish(boolean requestPublish) {
        this.mRequestPublish = requestPublish;
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        Log.e(TAG, session.getState().toString());

        if(exception instanceof FacebookAuthorizationException){
            Log.e(TAG, exception.getMessage());
        } else if (exception instanceof FacebookOperationCanceledException) {
            if (mListener != null) {
                mListener.OnSnsResult(null, SnsController.SNS_FACEBOOK_CANCELED);
            }
        }
        if (state.isOpened()) {
            String token = session.getAccessToken();

            Log.d("FacebookToken", token);
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (mListener != null) {
                        if (response.getError() == null) {
                            List<Object> facebookResponseList = new ArrayList<Object>();
                            facebookResponseList.add(session);
                            facebookResponseList.add(user);
                            /*FacebookModel.Data model = new FacebookModel.Data();
                            model.setAccessToken(session.getAccessToken());
                            model.setFacebookId(user.getId());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String strDate = sdf.format(session.getExpirationDate());
                            model.setTokenExpiredDate(strDate);*/
                            mListener.OnSnsResult(facebookResponseList, SnsController.SNS_FACEBOOK_LOGGED_IN);
                        } else {
                            mListener.OnSnsResult(null, SnsController.SNS_FACEBOOK_EXCEPTION);
                        }
                    }
                }
            }).executeAsync();
            Log.v(TAG, "Your token is " + token);
        }
    }

    public void disconnectFacebook() {
        mRequestPublish = false;
        Session session = Session.openActiveSessionFromCache(mFActivity == null ? mContext : mFActivity);
        if (session != null) {
            Log.e(TAG, session.getPermissions().toArray().toString());
            Session.getActiveSession().closeAndClearTokenInformation();
        }
        if (mListener != null) {
            mListener.OnSnsResult(null, SnsController.SNS_FACEBOOK_LOGGED_OUT);
        }
    }

    public void postToWall(String message, String caption, String urlLink, String urlImgPromo, final SnsController.SnsListener listener) {
        Session session = Session.openActiveSessionFromCache(mContext);
        Bundle param = new Bundle();
        param.putString("caption", caption);
        param.putString("message", message);
        param.putString("link",urlLink);
        param.putString("picture",urlImgPromo);
        Request request = new Request(session,"me/feed", param, HttpMethod.POST);
        request.setCallback(new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                /*if (listener == null) {
                    if (mListener != null) {
                        mListener.OnSNSResult(response, SnsController.FACEBOOK_POST_RESULT);
                    }
                } else {
                    listener.OnSNSResult(response, SnsController.FACEBOOK_POST_RESULT);
                }*/
            }
        });
        request.executeAsync();

        /*Request.newStatusUpdateRequest(session*//*Session.getActiveSession()*//*, message, new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if (listener == null) {
                    if (mListener != null) {
                        mListener.OnSNSResult(response, SnsController.FACEBOOK_POST_RESULT);
                    }
                } else {
                    listener.OnSNSResult(response, SnsController.FACEBOOK_POST_RESULT);
                }
            }
        }).executeAsync();*/

    }

    public Session getSession() {
        return Session.openActiveSessionFromCache(mFActivity == null ? mContext : mFActivity);
    }

    @Override
    public void onCompleted(Response response) {
        // TODO Auto-generated method stub
        Log.e(TAG, response.toString());
    }
}
