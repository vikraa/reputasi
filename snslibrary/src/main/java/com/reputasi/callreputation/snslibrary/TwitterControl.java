package com.reputasi.callreputation.snslibrary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.reputasi.callreputation.snslibrary.model.TwitterModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.extractors.BaseStringExtractorImpl;
import org.scribe.extractors.HeaderExtractorImpl;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.RequestTuner;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.services.HMACSha1SignatureService;
import org.scribe.services.TimestampServiceImpl;
import org.scribe.utils.OAuthEncoder;

/**
 * Created by Vikraa on 2/23/2015.
 */
public class TwitterControl {

    private static final String APIKEY = "tXfx7F3YM6DZwVXMLIjEfrmGP";
    private static final String APISECRET = "3dEzimbHkTTySjNJWJB5IBpDTXhLG3r5zRa6YdJW12LKAUtcJi";
    private static final String CALLBACK = "oauth://twitter";

    private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
    private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");
    private static final String ACCESS_ENDPOINT = "https://api.twitter.com/oauth/access_token";
    private static final String CREDENTIALS_URI = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String UPDATE_STATUS_URI = "https://api.twitter.com/1.1/statuses/update.json";

    private static OAuthService mOAuthService;
    private Token mRequestToken;
    private Token mAccessToken;

    //private SocialMediaCallback mCallback;
    private Dialog mDialog;
    private WebView mWebView;
    private Activity mActivity;
    private ProgressDialog dialog;
    private SnsController.SnsListener mSnsListener;
    private int counter;

    public class TwitterResponseToken {
        private String mTwitterRaw;

        private String mTwitterSecret;

        private String mTwitterToken;

        public String getTwitterRaw() {
            return mTwitterRaw;
        }

        public void setTwitterRaw(String mTwitterRaw) {
            this.mTwitterRaw = mTwitterRaw;
        }

        public String getTwitterToken() {
            return mTwitterToken;
        }

        public void setTwitterToken(String mTwitterToken) {
            this.mTwitterToken = mTwitterToken;
        }

        public String getTwitterSecret() {
            return mTwitterSecret;
        }

        public void setTwitterSecret(String mTwitterSecret) {
            this.mTwitterSecret = mTwitterSecret;
        }
    }
    public static enum SocialMediaResponse{
        LOGIN_SUCCESS("login_success"), LOGIN_FAILURE("login_failure");
        private String name;
        SocialMediaResponse(String name){
            this.name = name;
        }
        public String getName(){return name;}
    }
    public static interface SocialMediaCallback{
        void onReturn(SocialMediaResponse socialMediaResponse, Object... response);
    }

    private TwitterResponseToken mToken;

    public TwitterControl (SocialMediaCallback listener, Activity activity) {
        //mCallback = listener;
        mActivity = activity;

        mOAuthService = new ServiceBuilder()
                .provider(TwitterApi.SSL.class)
                .apiKey(APIKEY)
                .apiSecret(APISECRET)
                .callback(CALLBACK)
                .build();
    }

    public void setListener(SnsController.SnsListener listener){
        this.mSnsListener = listener;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void clearTwitterToken(){
        if (mSnsListener != null){
            mSnsListener.OnSnsResult(null, SnsController.SNS_TWITTER_LOGGED_OUT);
        }
    }

    public void startAuth() {
        counter = 0;
        (new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                //mSnsListener.onShowDialog("Twitter", "Mohon ditunggu...");
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Void... params) {
                try {
                    mRequestToken = mOAuthService.getRequestToken();
                    return mOAuthService.getAuthorizationUrl(mRequestToken);
                } catch (Exception ex){
                    Log.e(TwitterControl.class.getName(), ex.getMessage());
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                showWebViewPopup(result);
            }
        }).execute();
    }

    private void showWebViewPopup(String url) {
        mDialog = new Dialog(mActivity);
        mWebView = new WebView(mActivity);

        mWebView.clearCache(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setFocusable(true);
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus())
                        {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.loadUrl(url);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mWebView);
    }

    private void getCredentials(){
        final OAuthRequest request = new OAuthRequest(Verb.GET, CREDENTIALS_URI);
        mOAuthService.signRequest(mAccessToken, request);
        (new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //TODO
                //ADD DIALOG HERE
            }

            @Override
            protected String doInBackground(Void... params) {
                Response response = request.send();
                String result = "";
                try {
                    JSONObject json = new JSONObject(response.getBody());
                    result = json.toString(3);
                } catch (JSONException e) {
                    Log.e(TwitterControl.class.getName(), e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                List<Object> twitterResponseList = new ArrayList<Object>();
                TwitterModel twModel = new TwitterModel();
                TwitterModel.Data twData = new TwitterModel.Data();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    twData.setScreenName(jsonObject.getString("screen_name"));
                    twData.setTwitterId(jsonObject.getString("id"));
                    twData.setAuthToken(mToken.getTwitterToken());
                    twData.setAuthTokenSecret(mToken.getTwitterSecret());
                    twData.setConsumerKey(APIKEY);
                    twData.setConsumerSecret(APISECRET);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                twModel.setData(twData);
                twitterResponseList.add(mToken);
                twitterResponseList.add(twModel);
                mSnsListener.OnSnsResult(twitterResponseList, SnsController.SNS_TWITTER_LOGGED_IN);

                /*TwitterModel model = new TwitterModel();
                model.setScreenName(result);
                model.setTwitterResponseToken(mToken);

                if (mSnsListener != null){
                    mSnsListener.OnSNSResult(model, SnsController.TWITTER);
                }*/
            }
        }).execute();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        boolean verified = false;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if ((url != null) && (url.startsWith(CALLBACK)) && !verified) { // Override webview when user came back to CALLBACK_URL
                verified = true;
                mWebView.stopLoading();
                mWebView.setVisibility(View.INVISIBLE); // Hide webview if necessary
                Uri uri = Uri.parse(url);
                if (uri.getQuery().contains("oauth_verifier")) {
                    final Verifier verifier = new Verifier(uri.getQueryParameter("oauth_verifier"));
                    (new AsyncTask<Void, Void, Token>() {
                        @Override
                        protected Token doInBackground(Void... params) {
                            return customGetAccessToken(mRequestToken, verifier);
                        }

                        @Override
                        protected void onPostExecute(Token accessToken) {
                            mDialog.dismiss();
                            mAccessToken = accessToken;
                            mToken = new TwitterResponseToken();
                            mToken.setTwitterRaw(mAccessToken.getRawResponse());
                            mToken.setTwitterToken(mAccessToken.getToken());
                            mToken.setTwitterSecret(mAccessToken.getSecret());
                            // GET USER CREDENTIALS
                            getCredentials();
                            verified = false;
                        }
                    }).execute();
                } else {
                    mDialog.dismiss();
                }
            } else {
                super.onPageStarted(view, url, favicon);
            }
        }

        public void onPageFinished(WebView view, String url) {
            if (!mActivity.isFinishing()) {
                //mSnsListener.onDismissDialog();
                if ((url != null) && (!url.startsWith(CALLBACK)) && counter==0) {
                    mDialog.show();
                    counter = 1;
                }
            }
        };
    };

    private Token customGetAccessToken (Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(Verb.POST, ACCESS_ENDPOINT);
        request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());

        addOAuthParams(request, requestToken);
        appendSignature(request);

        RequestTuner tuner = new RequestTuner() {
            @Override
            public void tune(Request request) {
                request.setReadTimeout(2, TimeUnit.SECONDS);
            }
        };

        Response response = request.send(tuner);
        String body = response.getBody();
        return extract(body);
    }

    private void addOAuthParams(OAuthRequest request, Token token) {
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, new TimestampServiceImpl().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstants.NONCE, new TimestampServiceImpl().getNonce());
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, APIKEY);
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, "HMAC-SHA1");
        request.addOAuthParameter(OAuthConstants.VERSION, "1.0");
        request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
    }

    private String getSignature(OAuthRequest request, Token token){
        String baseString = new BaseStringExtractorImpl().extract(request);
        String signature = new HMACSha1SignatureService().getSignature(baseString, APISECRET, token.getSecret());

        return signature;
    }

    private void appendSignature(OAuthRequest request){
        String oauthHeader = new HeaderExtractorImpl().extract(request);
        request.addHeader(OAuthConstants.HEADER, oauthHeader);
    }

    private String extract(String response, Pattern p) {
        Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1)		{
            return OAuthEncoder.decode(matcher.group(1));
        }
        else {
            return "";
        }
    }

    private Token extract(String response){
        String token = extract(response, TOKEN_REGEX);
        String secret = extract(response, SECRET_REGEX);
        return new Token(token, secret, response);
    }
}
