package com.reputasi.library.manager;

import android.app.Activity;
import android.content.Intent;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.reputasi.callreputation.snslibrary.SnsController;
import com.reputasi.callreputation.snslibrary.TwitterControl;
import com.reputasi.callreputation.snslibrary.model.FacebookModel;
import com.reputasi.callreputation.snslibrary.model.GoogleplusModel;
import com.reputasi.callreputation.snslibrary.model.TwitterModel;
import com.reputasi.library.ReputasiConstants;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.preference.PreferenceManager;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.BaseResponse;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;
import com.reputasi.library.rest.request.Profile;
import com.reputasi.library.rest.response.ProfileSignIn;
import com.reputasi.library.rest.response.ProfileSignUp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 6/28/2015.
 */
public class UserManager extends BaseManager implements SnsController.SnsListener {

    public static final int LOGIN_TYPE_EMAIL = 0;
    public static final int LOGIN_TYPE_GOOGLE_PLUS = 1;
    public static final int LOGIN_TYPE_FACEBOOK = 2;
    public static final int LOGIN_TYPE_TWITTER = 3;
    public static final int LOGIN_TYPE_FORGOT_PASSWORD = 4;
    private static UserManager mInstance;
    private static SnsController mSnsControl;
    private static SnsController.SnsListener mSnsListener;

    private ManagerListener mListener;
    public static UserManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserManager();
        }
        return mInstance;
    }

    /*public static UserManager getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new UserManager();
        }
        return mInstance;
    }*/

    public UserManager createSnsController(Activity activity) {
        if (mInstance.mSnsControl == null) {
            mInstance.mSnsControl = SnsController.getInstance(activity);
            mInstance.mSnsControl.setListener(mInstance);
            mInstance.mSnsListener = mInstance;
        }
        return mInstance;
    }

    public UserManager setListener(ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    public void login(final int loginType, String userName, String userPassword) {
        final Profile request = new Profile();
        switch(loginType) {
            case LOGIN_TYPE_EMAIL :
                //request.setEmail(userMail);
                request.setName(userName);
                request.setPassword(userPassword);
                request.setSimCardId(ReputasiUtils.getSimCardId());
                request.setDeviceInfo(ReputasiUtils.getDeviceInfo());
                HashMap<String, HashMap<String, Boolean>> mapACL = new HashMap<>();
                mapACL.put("*", new HashMap<String, Boolean>());
                request.setACL(mapACL);
                request.setGender("");
                request.setAuthData(null);
                request.setDisplayName(userName);
                RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT);
                client.requestAPI().rptsUserSignup(request, new Callback<ProfileSignUp>() {
                    @Override
                    public void success(ProfileSignUp profileSignUp, Response response) {
                        UserPreferenceManager.putObjectId(profileSignUp.getObjectId());
                        UserPreferenceManager.putSession(profileSignUp.getSessionToken());
                        //UserPreferenceManager.putEmail(request.getEmail());
                        UserPreferenceManager.putUserName(request.getName());
                        UserPreferenceManager.putGender(request.getGender());

                        if (mListener != null) {
                            mListener.onSuccess(profileSignUp, loginType);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error != null ) {
                            if (error.getResponse() != null) {
                                int status = error.getResponse().getStatus();
                                if (status > 201) {
                                    if (mListener != null) {
                                        mListener.onEvent("Relogin user", loginType);
                                    }
                                    UserPreferenceManager.putUserName(request.getName());
                                    UserPreferenceManager.putGender(request.getGender());
                                    relogin(request.getName(), request.getPassword(), loginType);
                                } else {
                                    if (mListener != null) {
                                        mListener.onFailed(error, loginType);
                                    }
                                }
                            }
                        }
                    }
                });
                break;
            case LOGIN_TYPE_GOOGLE_PLUS:
                    mInstance.mSnsControl.snsLogin(SnsController.SNS_GOOGLE_PLUS);
                break;
            case LOGIN_TYPE_FACEBOOK:
                    mInstance.mSnsControl.snsLogin(SnsController.SNS_FACEBOOK);
                break;
            case LOGIN_TYPE_TWITTER:
                    mInstance.mSnsControl.snsLogin(SnsController.SNS_TWITTER);
                break;
            case LOGIN_TYPE_FORGOT_PASSWORD:
                break;
        }
    }

    private void relogin(final String userEmail, String password, final int loginType) {
        HashMap<String, String> loginParam = new HashMap<>();
        loginParam.put("username", userEmail);
        loginParam.put("password", password);
        RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT);
        client.requestAPI().rptsUserSignin(loginParam, new Callback<ProfileSignIn>() {
            @Override
            public void success(ProfileSignIn profileSignIn, Response response) {
                UserPreferenceManager.putObjectId(profileSignIn.getObjectId());
                UserPreferenceManager.putSession(profileSignIn.getSessionToken());
                UserPreferenceManager.putEmail(userEmail);
                if (mListener != null) {
                    mListener.onSuccess(profileSignIn, loginType);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (mListener != null) {
                    mListener.onFailed(error, loginType);
                }
            }
        });
    }

    public void loginForgotPassword(String userMail) {

    }

    public void registerPhoneNumber(String phoneNumber) {
        UserPreferenceManager.putPhoneNumber(phoneNumber);
        /*Profile model = new Profile();
        RestClient.getInstance().requestAPI().rptsUserUpdate(PreferenceManager.getString(ReputasiConstants.USER_OBJECT_ID), model ,new Callback<BaseResponse>() {
            @Override
            public void success(BaseResponse baseResponse, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/
    }

    public void registerPin(String pin) {

    }



    public boolean isPinValid(String pin) {
        return true;
    }

    public boolean isPhoneNumberRegistered() {
        return UserPreferenceManager.getPhoneNumber() != null;
    }

    public boolean isUserLoggedIn() {
        return UserPreferenceManager.getSession() != "";
    }

    public Object getSnsCallback(int snsType) {
        return mSnsControl.snsGetCallback(snsType);
    }

    public void OnSnsGplusActivityResult(int requestCode, int responseCode, Intent intent) {
        mSnsControl.onGPlusActivityResult(requestCode, responseCode, intent);
    }


    @Override
    public void OnSnsResult(Object object, String tag) {
        if (tag.equalsIgnoreCase(SnsController.SNS_FACEBOOK_LOGGED_IN)) {
            if (object != null && object instanceof List<?>) {
                List<Object> dataList = (List<Object>)object;
                Session session = (Session)dataList.get(0);
                GraphUser fbUser = (GraphUser)dataList.get(1);
                final Profile userRequest = new Profile();
                //userRequest.setEmail((String) fbUser.asMap().get("email"));
                userRequest.setName((String) fbUser.asMap().get("email"));
                userRequest.setDisplayName(fbUser.getName());
                //userRequest.setPhoneNumber("");
                userRequest.setSimCardId(ReputasiUtils.getSimCardId());
                userRequest.setDeviceInfo(ReputasiUtils.getDeviceInfo());
                FacebookModel fbModel = new FacebookModel();
                FacebookModel.Data fbData = new FacebookModel.Data();
                fbData.setAccessToken(session.getAccessToken());
                fbData.setFacebookId(fbUser.getId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String strDate = sdf.format(session.getExpirationDate());
                fbData.setTokenExpiredDate(strDate);
                fbModel.setData(fbData);
                userRequest.setAuthData(fbModel);
                RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT);
                client.requestAPI().rptsUserSignup(userRequest, new Callback<ProfileSignUp>() {
                    @Override
                    public void success(ProfileSignUp profileSignUp, Response response) {
                        UserPreferenceManager.putObjectId(profileSignUp.getObjectId());
                        UserPreferenceManager.putSession(profileSignUp.getSessionToken());
                        //UserPreferenceManager.putEmail(userRequest.getEmail());
                        UserPreferenceManager.putUserName(userRequest.getName());
                        UserPreferenceManager.putGender(userRequest.getGender());

                        if (mListener != null) {
                            mListener.onSuccess(profileSignUp, LOGIN_TYPE_FACEBOOK);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mListener != null) {
                            mListener.onFailed(error, LOGIN_TYPE_FACEBOOK);
                        }
                    }
                });
            }
        } else if (tag.equalsIgnoreCase(SnsController.SNS_FACEBOOK_EXCEPTION)) {

        } else if (tag.equalsIgnoreCase(SnsController.SNS_FACEBOOK_LOGGED_OUT)) {
            PreferenceManager.clearPreference();
        } else if (tag.equalsIgnoreCase(SnsController.SNS_FACEBOOK_CANCELED)) {
            if (mListener != null) {
                mListener.onFailed(object, LOGIN_TYPE_FACEBOOK);
            }
        } else if (tag.equalsIgnoreCase(SnsController.SNS_TWITTER_LOGGED_IN)) {
            if (object != null && object instanceof List<?>) {
                List<Object> dataList = (List<Object>)object;
                TwitterControl.TwitterResponseToken token = (TwitterControl.TwitterResponseToken)dataList.get(0);
                TwitterModel twModel = (TwitterModel)dataList.get(1);
                final Profile userRequest = new Profile();
                //userRequest.setEmail(twModel.getData().getTwitterId() + "@twitter.com");
                userRequest.setName(twModel.getData().getTwitterId());
                userRequest.setDisplayName(twModel.getData().getScreenName());
                userRequest.setPhoneNumber("");
                userRequest.setSimCardId(ReputasiUtils.getSimCardId());
                userRequest.setDeviceInfo(ReputasiUtils.getDeviceInfo());
                userRequest.setAuthData(twModel);

                RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT);
                client.requestAPI().rptsUserSignup(userRequest, new Callback<ProfileSignUp>() {
                    @Override
                    public void success(ProfileSignUp profileSignUp, Response response) {
                        UserPreferenceManager.putObjectId(profileSignUp.getObjectId());
                        UserPreferenceManager.putSession(profileSignUp.getSessionToken());
//                        UserPreferenceManager.putEmail(userRequest.getEmail());
                        UserPreferenceManager.putUserName(userRequest.getName());
                        UserPreferenceManager.putGender(userRequest.getGender());
                        if (mListener != null) {
                            mListener.onSuccess(profileSignUp, LOGIN_TYPE_TWITTER);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mListener != null) {
                            mListener.onFailed(error, LOGIN_TYPE_TWITTER);
                        }
                    }
                });
                /*RestClient restClient = new RestClient(RestConstant.DEFAULT_TIMEOUT);
                restClient.requestAPI().rptsUserSignup(userRequest, new Callback<UsersModel>() {
                    @Override
                    public void success(UsersModel usersModel, Response response) {
                        if (mListener != null){
                            userRequest.setCreatedAt(usersModel.getCreatedAt());
                            userRequest.setObjectId(usersModel.getObjectId());
                            userRequest.setSessionToken(usersModel.getSessionToken());
                            mListener.onCompleted(usersModel, RestConstant.API_USERS);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (mListener != null){
                            mListener.onError(error, RestConstant.API_USERS);
                        }
                    }
                });*/
            }
        } else if (tag.equalsIgnoreCase(SnsController.SNS_TWITTER_LOGGED_OUT)) {
            PreferenceManager.clearPreference();
        } else if (tag.equalsIgnoreCase(SnsController.SNS_GOOGLE_PLUS_LOGGED_IN)) {
            if (object != null && object instanceof HashMap<?,?>) {
                HashMap<String, String> gplusMap = (HashMap<String, String>)object;
                final Profile userRequest = new Profile();
                GoogleplusModel gplusModel = new GoogleplusModel();
                GoogleplusModel.Data gplusData = new GoogleplusModel.Data();
                gplusData.setId(UUID.randomUUID().toString());
                gplusModel.setData(gplusData);
                //userRequest.setEmail(gplusMap.get(SnsController.SNS_GOOGLE_PLUS_EMAIL));
                userRequest.setName(gplusMap.get(SnsController.SNS_GOOGLE_PLUS_EMAIL));
                userRequest.setPassword(gplusMap.get(SnsController.SNS_GOOGLE_PLUS_ID));
                userRequest.setDisplayName(gplusMap.get(SnsController.SNS_GOOGLE_PLUS_DISPLAYNAME));
                userRequest.setPhoneNumber("");
                userRequest.setSimCardId(ReputasiUtils.getSimCardId());
                userRequest.setDeviceInfo(ReputasiUtils.getDeviceInfo());
                userRequest.setAuthData(gplusModel);
                RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT);
                client.requestAPI().rptsUserSignup(userRequest, new Callback<ProfileSignUp>() {
                    @Override
                    public void success(ProfileSignUp profileSignUp, Response response) {
                        UserPreferenceManager.putObjectId(profileSignUp.getObjectId());
                        UserPreferenceManager.putSession(profileSignUp.getSessionToken());
//                        UserPreferenceManager.putEmail(userRequest.getEmail());
                        UserPreferenceManager.putUserName(userRequest.getName());
                        UserPreferenceManager.putGender(userRequest.getGender());
                        if (mListener != null) {
                            mListener.onSuccess(profileSignUp, LOGIN_TYPE_GOOGLE_PLUS);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error != null) {
                            if (error.getResponse() != null) {
                                int status = error.getResponse().getStatus();
                                if (status > 201) {
                                    UserPreferenceManager.putUserName(userRequest.getName());
                                    UserPreferenceManager.putGender(userRequest.getGender());
                                    relogin(userRequest.getName(), userRequest.getPassword(), LOGIN_TYPE_GOOGLE_PLUS);
                                }
                            }
                        } else {
                            if (mListener != null) {
                                mListener.onFailed(error, LOGIN_TYPE_GOOGLE_PLUS);
                            }
                        }
                    }
                });
            }
        } else if (tag.equalsIgnoreCase(SnsController.SNS_GOOGLE_PLUS_LOGGED_OUT)) {
            PreferenceManager.clearPreference();
        }

    }
}
