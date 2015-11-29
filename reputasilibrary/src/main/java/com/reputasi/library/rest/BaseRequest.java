package com.reputasi.library.rest;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by vikraa on 6/23/2015.
 */
public class BaseRequest {
    @SerializedName("ACL")
    private HashMap<String, HashMap<String, Boolean>> mACL;

    public HashMap<String, HashMap<String, Boolean>> getACL() {
        return mACL;
    }

    public void setACL(HashMap<String, HashMap<String, Boolean>> mACLPermissions) {
        this.mACL = mACLPermissions;
    }

}
