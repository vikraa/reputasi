package com.reputasi.library.rest.response;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseResponse;

/**
 * Created by vikraa on 6/28/2015.
 */
public class ProfileSignUp extends BaseResponse {
    @SerializedName("sessionToken")
    private String mSessionToken;

    public String getSessionToken() {
        return mSessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.mSessionToken = sessionToken;
    }
}
