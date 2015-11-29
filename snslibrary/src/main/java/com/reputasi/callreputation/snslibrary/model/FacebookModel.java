package com.reputasi.callreputation.snslibrary.model;

/**
 * Created by vikraa on 1/31/2015.
 */
import com.google.gson.annotations.SerializedName;

public class FacebookModel {

    @SerializedName("facebook")
    private Data mFbModel;

    public void setData(Data fbModel) {
        this.mFbModel = fbModel;
    }

    public static class Data {
        @SerializedName("id")
        private String mFacebookId;

        @SerializedName("access_token")
        private String mAccessToken;

        @SerializedName("expiration_date")
        private String mTokenExpiredDate;

        public String getFacebookId() {
            return mFacebookId;
        }

        public void setFacebookId(String mFacebookId) {
            this.mFacebookId = mFacebookId;
        }

        public String getAccessToken() {
            return mAccessToken;
        }

        public void setAccessToken(String mAccessToken) {
            this.mAccessToken = mAccessToken;
        }

        public String getTokenExpiredDate() {
            return mTokenExpiredDate;
        }

        public void setTokenExpiredDate(String mTokenExpiredDate) {
            this.mTokenExpiredDate = mTokenExpiredDate;
        }
    }

}
