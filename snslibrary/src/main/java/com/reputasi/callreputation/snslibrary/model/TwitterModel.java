package com.reputasi.callreputation.snslibrary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vikraa on 2/23/2015.
 */
public class TwitterModel {
    @SerializedName("twitter")
    private Data mData;

    public void setData(Data data) {
        this.mData = data;
    }

    public Data getData() {
        return mData;
    }

    public static class Data {
        @SerializedName("id")
        private String mTwitterId;
        @SerializedName("screen_name")
        private String mScreenName;
        @SerializedName("consumer_key")
        private String mConsumerKey;
        @SerializedName("consumer_secret")
        private String mConsumerSecret;
        @SerializedName("auth_token")
        private String mAuthToken;
        @SerializedName("auth_token_secret")
        private String mAuthTokenSecret;

        public String getTwitterId() {
            return mTwitterId;
        }

        public void setTwitterId(String mTwitterId) {
            this.mTwitterId = mTwitterId;
        }

        public String getScreenName() {
            return mScreenName;
        }

        public void setScreenName(String mScreenName) {
            this.mScreenName = mScreenName;
        }

        public String getConsumerKey() {
            return mConsumerKey;
        }

        public void setConsumerKey(String mConsumerKey) {
            this.mConsumerKey = mConsumerKey;
        }

        public String getConsumerSecret() {
            return mConsumerSecret;
        }

        public void setConsumerSecret(String mConsumerSecret) {
            this.mConsumerSecret = mConsumerSecret;
        }

        public String getAuthToken() {
            return mAuthToken;
        }

        public void setAuthToken(String mAuthToken) {
            this.mAuthToken = mAuthToken;
        }

        public String getAuthTokenSecret() {
            return mAuthTokenSecret;
        }

        public void setAuthTokenSecret(String mAuthTokenSecret) {
            this.mAuthTokenSecret = mAuthTokenSecret;
        }
    }
}
