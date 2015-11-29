package com.reputasi.callreputation.snslibrary.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vikraa on 3/3/2015.
 */
public class GoogleplusModel {
    @SerializedName("anonymous")
    private Data mData;

    public void setData(Data data) {
        this.mData = data;
    }

    public static class Data {

        @SerializedName("id")
        private String mId;

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            this.mId = id;
        }
    }
}
