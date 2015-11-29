package com.reputasi.library.rest;

import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.rest.request.Blacklist;
import com.reputasi.library.rest.request.ContactBook;
import com.reputasi.library.rest.request.Contribute;
import com.reputasi.library.rest.request.Profile;
import com.reputasi.library.rest.response.BlacklistResponse;
import com.reputasi.library.rest.response.CategoryNumber;
import com.reputasi.library.rest.response.CheckVersion;
import com.reputasi.library.rest.response.ContactHash;
import com.reputasi.library.rest.response.ProfileSignIn;
import com.reputasi.library.rest.response.ProfileSignUp;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by vikraa on 6/24/2015.
 */
public interface RestApi {

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_CONTACTBOOK)
    void rptsPostUserContactsBook(@Body ContactBook request, Callback<BaseResponse> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_USERS)
    void rptsUserSignup(@Body Profile request, Callback<ProfileSignUp> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @GET(RestConstant.PARSE_API_LOGIN)
    void rptsUserSignin(@retrofit.http.QueryMap Map<String, String> map, Callback<ProfileSignIn> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @PUT(RestConstant.PARSE_API_UPDATE_USERS)
    void rptsUserUpdate(@Path("objectid") String objectId, @Body Profile updateProfile, Callback<BaseResponse> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_SEARCHNUMBER)
    void rptsPostSearchNumber(@Body Map<String, String> incomingNumber, Callback<HashMap<String, HashMap<String, Object>>> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_NUMBER_CATEGORY)
    BaseResponseQueries<CategoryNumber> rptsGetBlacklistCategory(@Body ReputasiUtils.Dummy dummy);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_GET_SPECIALNUMBER)
    void rptsGetBlacklistNumber(@Body ReputasiUtils.Dummy dummy, Callback<BaseResponseQueries<BlacklistResponse>> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_POST_SPECIALNUMBER)
    void rptsPostBlacklistNumber(@Body Blacklist request, Callback<BaseResponse> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_CONTRIBUTE)
    void rptsPostContributeNumber(@Body Contribute request, Callback<BaseResponse> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_DELETE_SPECIAL_NUMBER)
    void rptsPostDeleteBlacklistNumber(@Body Blacklist request, Callback<BaseResponse> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_CHECKFORCEUPDATE)
    void rptsGetCheckVersion(@Body Map<String, Integer> versionCode, Callback<CheckVersion> callback);

    @Headers(RestConstant.REST_CONTENT_TYPE_JSON)
    @POST(RestConstant.PARSE_API_CHECKCONTACTHASH)
    void rptsPostCheckContactHash(@Body Map<String, String> request, Callback<BaseResponseFunction<ContactHash>> callback);
}

