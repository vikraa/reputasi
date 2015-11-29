package com.reputasi.library.rest;

/**
 * Created by vikraa on 6/24/2015.
 */
public class RestConstant {
    public static final String PARSE_APPLICATION_ID = "k6iID9uMnzhs0qpmzB5gKq2kHdKG7VNFXmv3dpN4";
    public static final String PARSE_CLIENT_KEY = "TWFz2UtJMwMH4WdqAwJViz28F7m0LxwosQ9YvorF";
    public static final String PARSE_REST_API_KEY = "rWxLrTXwZT9SH8rd2vrktbs9tEo0k5U6Vo3KDSnq";

    public static final String PARSE_BASE_API = "/1/classes/";
    public static final String PARSE_API_CONTACTBOOK = "/1/functions/sendUserContact";
    public static final String PARSE_API_GET_SPECIALNUMBER = "/1/functions/getUserSpecialNumber";
    public static final String PARSE_API_POST_SPECIALNUMBER = "/1/functions/addUserSpecialNumber";
    public static final String PARSE_API_DELETE_SPECIAL_NUMBER = "/1/functions/deleteUserSpecialNumber";
    public static final String PARSE_API_NUMBER_CATEGORY = "/1/functions/getAllCategories";
    public static final String PARSE_API_CONTRIBUTE = "/1/functions/contributeNumber";
    public static final String PARSE_API_USERS = "/1/users";
    public static final String PARSE_API_UPDATE_USERS = "/1/{objectid}";
    public static final String PARSE_API_LOGIN = "/1/login";
    public static final String PARSE_API_SEARCHNUMBER = "/1/functions/searchNumber";
    public static final String PARSE_API_CHECKFORCEUPDATE = "/1/functions/checkForceUpdate";
    public static final String PARSE_API_CHECKCONTACTHASH = "/1/functions/checkContactHash";

    public static final long DEFAULT_TIMEOUT = 10000;  // 10 seconds
    public static final long DEFAULT_TIMEOUT_INCOMING_CALL = 4000; // 5 seconds
    public static final String REST_API_SERVER = "https://api.parse.com";
    public static final String REST_CONTENT_TYPE_JSON = "Content-Type: application/json";

}
