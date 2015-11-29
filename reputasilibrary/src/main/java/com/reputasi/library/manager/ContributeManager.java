package com.reputasi.library.manager;

import android.util.Log;

import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.BaseResponse;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;
import com.reputasi.library.rest.request.Contribute;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 8/29/2015.
 */
public class ContributeManager extends BaseManager<Void, Void> {
    private static ContributeManager mInstance;
    private ManagerListener mListener;

    public static ContributeManager getInstance() {
        if (mInstance == null) {
            mInstance = new ContributeManager();
        }
        return mInstance;
    }

    public ContributeManager setListener(ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    public void addContributeNumber(String phoneNumber, String ownerName, String categoryid, String description, int thumb) {
        Contribute request = new Contribute();
        request.setPhoneNumber(ReputasiUtils.validateNumber(phoneNumber));
        request.setNumberOwner(ownerName);
        request.setCategoryId(categoryid);
        request.setNotes(description);
        request.setThumbUpDown(String.valueOf(thumb));
        RestClient restClient = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
        restClient.requestAPI().rptsPostContributeNumber(request, new Callback<BaseResponse>() {
            @Override
            public void success(BaseResponse baseResponse, Response response) {
                Log.d("contribute", "success");
                Log.d("contribute", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("contribute", "failed");
                Log.d("contribute", "failed");
            }
        });
    }

}
