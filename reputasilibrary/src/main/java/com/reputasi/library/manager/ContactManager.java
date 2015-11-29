package com.reputasi.library.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.reputasi.library.ReputasiApplication;
import com.reputasi.library.ReputasiConstants;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.record.ContactBookItem;
import com.reputasi.library.database.table.TableContactBook;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.BaseResponse;
import com.reputasi.library.rest.BaseResponseFunction;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;
import com.reputasi.library.rest.request.ContactBook;
import com.reputasi.library.rest.response.ContactHash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 6/27/2015.
 */
public class ContactManager extends BaseManager<Void, Void> {

    //private Context mContext;
    private static String mContactsMd5;
    private static ContactManager mInstance;
    private static List<String> mHistoryList;
    private int mProgressRunning;
    private ManagerListener mListener;
    private Stack<ContactBookItem> mStack;

    public static final int SYNCHRONIZE_NONE = 0;
    public static final int SYNCHRONIZE_CONTACT_LOCAL_DATABASE = 1;
    public static final int SYNCHRONIZE_CONTACT_SERVER_DATABASE = 2;

    public static ContactManager getInstance() {
        if (mInstance == null) {
            mInstance = new ContactManager();
            mHistoryList = new ArrayList<>();
            //mInstance.mContext = ReputasiApplication.getContext();
        }
        return mInstance;
    }

    public ContactManager setListener(ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    @Override
    protected void onPreBackgroundTask() {
        switch(mInstance.mProgressRunning) {
            case SYNCHRONIZE_CONTACT_LOCAL_DATABASE:
                if (mListener != null) {
                    mListener.onPreBackgroundTask(SYNCHRONIZE_CONTACT_LOCAL_DATABASE);
                }
                break;
            case SYNCHRONIZE_CONTACT_SERVER_DATABASE:
                if (mListener != null) {
                    mListener.onPreBackgroundTask(SYNCHRONIZE_CONTACT_SERVER_DATABASE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected Void onBackgroundTask(Integer... params) {
        int type = (Integer)params[0];
        switch(type) {
            case SYNCHRONIZE_CONTACT_LOCAL_DATABASE:
                populateContact();
                break;
            case SYNCHRONIZE_CONTACT_SERVER_DATABASE:
                synchronizeContact();
                break;
            default:
                break;
        }
        return super.onBackgroundTask(params);
    }

    @Override
    protected void onPostBackgroundTask(Void aVoid) {
        super.onPostBackgroundTask(aVoid);
        List<BaseItem> baseItems = DataContentProviderHelper.getInstance().queries(TableContactBook.TABLE_CODE, null, null, null);
        List<ContactBookItem> contactBookItems = new ArrayList<>();
        for (BaseItem item : baseItems) {
            ContactBookItem contactBookItem = (ContactBookItem)item;
            contactBookItems.add(contactBookItem);
        }
        if (mListener != null) {
            mListener.onPostBackgroundTask(contactBookItems, mInstance.mProgressRunning);
        }
        mInstance.mProgressRunning = SYNCHRONIZE_NONE;
    }

    public void startSynchronizeServer() {
        HashMap<String, String> request = new HashMap<>();
        request.put("hashString", mContactsMd5);
        RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
        client.requestAPI().rptsPostCheckContactHash(request, new Callback<BaseResponseFunction<ContactHash>>() {
            @Override
            public void success(BaseResponseFunction<ContactHash> contactHashResponse, Response response) {
                    if (contactHashResponse.getResult().isNeedUpdate()) {
                    if (startBackgroundTask(SYNCHRONIZE_CONTACT_SERVER_DATABASE)) {
                         mInstance.mProgressRunning = SYNCHRONIZE_CONTACT_SERVER_DATABASE;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                onPostBackgroundTask(null);
            }
        });
    }

    public void startSynchronizeLocal() {
        if (startBackgroundTask(SYNCHRONIZE_CONTACT_LOCAL_DATABASE)) {
            mInstance.mProgressRunning = SYNCHRONIZE_CONTACT_LOCAL_DATABASE;
        }
    }

    public List<ContactBookItem> getContacts() {
        List<BaseItem> result = DataContentProviderHelper.getInstance().queries(TableContactBook.TABLE_CODE, null, null, TableContactBook.CONTACT_NAME + " ASC ");
        List<ContactBookItem> items = new ArrayList<>();
        if (result != null) {
            for (BaseItem cb : result) {
                ContactBookItem contact = (ContactBookItem)cb;
                items.add(contact);
            }
        }
        return items;
    }


    private void populateContact() {
        DataContentProviderHelper.getInstance().clear(TableContactBook.TABLE_CODE);
        Cursor contactCursor = ReputasiApplication.getResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " asc");
        if (contactCursor.getCount() > 0) {
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                while (contactCursor.moveToNext()) {
                    if (Integer.parseInt(contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        ContactBookItem model = new ContactBookItem();
                        String contactName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor phoneNumberCursor = ReputasiApplication.getResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);
                        while (phoneNumberCursor.moveToNext()) {
                            model.setContactNumber(phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", ""));
                            model.setContactName(contactName);
                            model.setContactId(UUID.randomUUID().toString());
                            model.setSynchronizeStatus(ReputasiConstants.SYNCHRONIZE_STATUS_FAILED);

                            JSONObject objectItem = new JSONObject();
                            objectItem.put("contactName", model.getContactName());
                            objectItem.put("contactNumber", model.getContactNumber());
                            jsonArray.put(objectItem);

                            DataContentProviderHelper.getInstance().insert(model);
                        }
                        phoneNumberCursor.close();
                    }
                }
                jsonObject.put("contacts", jsonArray);
                mContactsMd5 = ReputasiUtils.generateMd5(jsonObject.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        contactCursor.close();
    }




    private void synchronizeContact() {
        if (mInstance.mStack == null) {
            mInstance.mStack = new Stack<>();
        }

        mInstance.mStack.clear();

        List<BaseItem> contactItems = DataContentProviderHelper.getInstance().queries(TableContactBook.TABLE_CODE,
                TableContactBook.CONTACT_SYNCHRONIZE_STATUS + " = ? ",
                new String[]{String.valueOf(ReputasiConstants.SYNCHRONIZE_STATUS_FAILED)}, null);
        if (contactItems != null) {
            for (BaseItem bi : contactItems) {
                ContactBookItem item = (ContactBookItem)bi;
                mInstance.mStack.add(item);
            }
            synchronizeContact(mInstance.mStack);
        }
    }

    private void synchronizeContact(Stack<ContactBookItem> items) {
        if (!mInstance.mStack.isEmpty()) {
            final ContactBookItem item = mInstance.mStack.pop();
            ContactBook request = new ContactBook();
            request.setContactName(item.getContactName());
            //request.setDeviceId(ReputasiUtils.getDeviceSerialNumber());
            //request.setSimCardId(ReputasiUtils.getSimCardId());
            request.setPhoneNumber(ReputasiUtils.validateNumber(item.getContactNumber().replace(" ","").replace("-","").replace("(","").replace(")","")));
            HashMap<String, HashMap<String, Boolean>> mapACL = new HashMap<>();
            HashMap<String, Boolean> aclPublicAccess = new HashMap<>();
            HashMap<String, Boolean> aclPrivateAccess = new HashMap<>();
            aclPrivateAccess.put("read", true );
            mapACL.put("*", aclPublicAccess);
            mapACL.put(UserPreferenceManager.getObjectId(), aclPrivateAccess);
            //request.setACL(mapACL);
            RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT, UserPreferenceManager.getSession());
            client.requestAPI().rptsPostUserContactsBook(request, new Callback<BaseResponse>() {
                @Override
                public void success(BaseResponse baseResponse, Response response) {
                    item.setSynchronizeStatus(ReputasiConstants.SYNCHRONIZE_STATUS_SUCCESS);
                    DataContentProviderHelper.getInstance().update(item);
                    if (!mInstance.mStack.isEmpty()) {
                        synchronizeContact(mInstance.mStack);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (!mInstance.mStack.isEmpty()) {
                        synchronizeContact(mInstance.mStack);
                    }
                }
            });


        }
    }

    public String getContactBookName(String phoneNumber) {
        String result = phoneNumber;
        if (!TextUtils.isEmpty(phoneNumber)) {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(phoneNumber));
            String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
            Cursor cur = ReputasiApplication.getResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    result = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
        }
        return result;
    }

    /*public String getContactBookType(String phoneNumber) {
        String result = "Unknown";
        if (!TextUtils.isEmpty(phoneNumber)) {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.TYPE };
            Cursor cur = ReputasiApplication.getResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    int type = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    switch (type) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            result = "Home";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                            result = "Work";
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            result = "Mobile";
                            break;
                    }
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
        }
        return result;
    }*/

}
