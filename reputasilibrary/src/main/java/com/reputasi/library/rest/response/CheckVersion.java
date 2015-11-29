package com.reputasi.library.rest.response;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.BaseResponse;

/**
 * Created by vikraa on 7/26/2015.
 */
public class CheckVersion extends BaseResponse {
    @SerializedName("result")
    private Data mResult;

    public Data getResult() {
        return mResult;
    }

    public static class Data {
        @SerializedName("forceUpdate")
        private boolean mForceUpdate;

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public void setForceUpdate(boolean forceUpdate) {
            this.mForceUpdate = forceUpdate;
        }
    }
}
