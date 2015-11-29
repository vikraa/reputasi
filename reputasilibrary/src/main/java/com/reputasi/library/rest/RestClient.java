package com.reputasi.library.rest;

import com.reputasi.library.preference.UserPreferenceManager;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;

/**
 * Created by vikraa on 6/24/2015.
 */
public class RestClient {

    private OkHttpClient mOkHttpClient;
    private RequestInterceptor mRequestInterceptor;
    private RestAdapter mRestAdapter;
    private RestApi mRestApi;

    //private static RestClient mInstancePublic, mInstanceSession;

    /*public static RestClient getInstance() {
        if (mInstancePublic == null) {
            mInstancePublic = new RestClient(RestConstant.DEFAULT_TIMEOUT);
        }
        return mInstancePublic;
    }

    public static RestClient getInstance(String sessionToken) {
        if (mInstanceSession == null ){
            mInstanceSession = new RestClient(RestConstant.DEFAULT_TIMEOUT, sessionToken);
        }
        return mInstanceSession;
    }*/

    public RestClient(long requestTimeout) {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(requestTimeout, TimeUnit.MILLISECONDS);
        mOkHttpClient.setReadTimeout(requestTimeout, TimeUnit.MILLISECONDS);

        mRequestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Parse-Application-Id", RestConstant.PARSE_APPLICATION_ID);
                request.addHeader("X-Parse-REST-API-Key", RestConstant.PARSE_REST_API_KEY);
            }
        };

        mRestAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(mOkHttpClient))
                .setEndpoint(RestConstant.REST_API_SERVER)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(mRequestInterceptor)
                /*.setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError cause) {
                        return cause;
                    }
                })*/
                .build();
        mRestApi = mRestAdapter.create(RestApi.class);
    }


    public RestClient(long requestTimeout, final String sessionToken) {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(requestTimeout, TimeUnit.MILLISECONDS);
        mOkHttpClient.setReadTimeout(requestTimeout, TimeUnit.MILLISECONDS);

        mRequestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                String session = UserPreferenceManager.getSession();
                request.addHeader("X-Parse-Application-Id", RestConstant.PARSE_APPLICATION_ID);
                request.addHeader("X-Parse-REST-API-Key", RestConstant.PARSE_REST_API_KEY);
                request.addHeader("X-Parse-Session-Token", session/*sessionToken*/);
            }
        };

        mRestAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(mOkHttpClient))
                .setEndpoint(RestConstant.REST_API_SERVER)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(mRequestInterceptor)
                /*.setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError cause) {
                        return cause;
                    }
                })*/
                .build();
        mRestApi = mRestAdapter.create(RestApi.class);
    }

    public RestApi requestAPI() {
        return mRestApi;
    }

}