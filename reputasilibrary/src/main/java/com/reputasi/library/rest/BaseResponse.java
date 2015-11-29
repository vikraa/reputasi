package com.reputasi.library.rest;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by vikraa on 6/23/2015.
 */
public class BaseResponse {
    @SerializedName("objectId")
    private String mObjectId;
    @SerializedName("updatedAt")
    private String mUpdatedAt;
    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("ACL")
    private HashMap<String, HashMap<String, Boolean>> mACL;

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        this.mObjectId = objectId;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.mUpdatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.mCreatedAt = createdAt;
    }

    public HashMap<String, HashMap<String, Boolean>> getACL() {
        return mACL;
    }

    public void setACL(HashMap<String, HashMap<String, Boolean>> acl) {
        this.mACL = acl;
    }
}
