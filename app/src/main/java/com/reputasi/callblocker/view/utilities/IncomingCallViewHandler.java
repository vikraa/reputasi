package com.reputasi.callblocker.view.utilities;

import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.reputasi.callblocker.GlobalApplication;
import com.reputasi.callblocker.R;
import com.reputasi.callblocker.view.custom.ReputasiTextView;
import com.reputasi.library.ReputasiApplication;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.manager.ContactManager;

/**
 * Created by vikraa on 7/25/2015.
 */
public class IncomingCallViewHandler {
    private static IncomingCallViewHandler  mInstance;
    private View mView;
    private ReputasiTextView mCallerName, mCallerTitle, mCallerThumbsInfo, mCallerNumber;/*, mCallerThumbsDown, mCallerCategory*/;
    private LinearLayout mBaseLayoutCard, /*mSearchingLayout,*/ mIncomingLayout;
    private RelativeLayout mLayoutBand;
    private WindowManager.LayoutParams mParams;
    private ImageView mCallerLogo, mCallerThumb;
    public static IncomingCallViewHandler getInstance() {
        if (mInstance == null) {
            mInstance = new IncomingCallViewHandler();
        }
        return mInstance;
    }

    public void showCallCard(String incomingNumber) {
        if (mBaseLayoutCard == null) {
            init();
            mCallerName.setText(ContactManager.getInstance().getContactBookName(ReputasiUtils.validateNumber(incomingNumber)));
            mCallerTitle.setText("Searching...");
            mCallerThumbsInfo.setText("");
            try {
                mCallerNumber.setText(PhoneNumberUtil.getInstance().getNumberType(PhoneNumberUtil.getInstance().parse(ReputasiUtils.validateNumber(incomingNumber),"")).toString().toLowerCase() + " | " + ReputasiUtils.validateNumber(incomingNumber));
            } catch (NumberParseException e) {
                mCallerNumber.setText("Unknown" + " | " + ReputasiUtils.validateNumber(incomingNumber));
            }
            WindowManager wm = (WindowManager) GlobalApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
                /*WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY*/,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                    PixelFormat.TRANSPARENT);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mParams.height = size.y / 4; //call card height
            mParams.width = size.x;
            mParams.x = 265;
            KeyguardManager keyLock = (KeyguardManager) ReputasiApplication.getContext().getSystemService(Context.KEYGUARD_SERVICE);
            if( keyLock.inKeyguardRestrictedInputMode()) {
                //it is locked
                mParams.y = 0;
            } else {
                //it is not locked
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mParams.y = size.y / 4;
                } else {
                    mParams.y = 0;
                }
            }
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.gravity = Gravity.TOP;

            mBaseLayoutCard = new LinearLayout(GlobalApplication.getContext());

            /*if (mBaseLayoutCard == null) {
                mBaseLayoutCard = new LinearLayout(GlobalApplication.getContext());
            }*/
        /*if (mBaseLayoutCard.getWindowToken() != null) {
            wm.removeView(mBaseLayoutCard);
        }*/

            mBaseLayoutCard.setBackgroundColor(Color.TRANSPARENT);
            mBaseLayoutCard.setOrientation(LinearLayout.VERTICAL);
            mBaseLayoutCard.addView(mView);
            wm.addView(mBaseLayoutCard, mParams);
        }
    }

    private void init() {
        mView = LayoutInflater.from(GlobalApplication.getContext()).inflate(R.layout.card_incoming_call, null);
        mCallerName = (ReputasiTextView)mView.findViewById(R.id.tv_caller_name);
        mCallerTitle = (ReputasiTextView)mView.findViewById(R.id.tv_caller_category);
        mCallerLogo = (ImageView)mView.findViewById(R.id.iv_caller_logo);
        mCallerThumb = (ImageView)mView.findViewById(R.id.iv_caller_thumbs);
        mCallerNumber = (ReputasiTextView)mView.findViewById(R.id.tv_caller_number);
        mLayoutBand = (RelativeLayout)mView.findViewById(R.id.rl_incoming_call_band_layout);
        mCallerThumbsInfo = (ReputasiTextView)mView.findViewById(R.id.tv_caller_thumbs_score);
        mIncomingLayout = (LinearLayout)mView.findViewById(R.id.ll_information);
    }

    public void dismissCallCard() {
        try {
            if (mBaseLayoutCard != null) {
                WindowManager wm = (WindowManager) GlobalApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
                wm.removeViewImmediate(mBaseLayoutCard);
                mBaseLayoutCard = null;
                mParams = null;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    public void updateCallCardInformation(String phoneNumber, String name, String title, String score, String thumbUpScore, String thumbDownScore) {
        if (mBaseLayoutCard != null) {
            WindowManager wm = (WindowManager) GlobalApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
            mCallerName.setText(name.isEmpty() ? phoneNumber : name);
            mCallerTitle.setText(title);
            //mCallerScore.setText(score);
            int callScore = Integer.parseInt(score);
            if (callScore > 0 && callScore < 60) { // red
                mCallerLogo.setImageResource(R.drawable.incoming_call_red);
                mLayoutBand.setBackground(GlobalApplication.getContext().getResources().getDrawable(R.drawable.rounded_band_incoming_spam));
                mCallerThumb.setVisibility(View.VISIBLE);
                mCallerThumb.setImageResource(R.drawable.thumbsdown_incoming_call);
                mCallerThumbsInfo.setText(thumbDownScore + " Thumbs Down");
            } else if (callScore > 60 ) {
                mCallerLogo.setImageResource(R.drawable.incoming_call_green);
                mLayoutBand.setBackground(GlobalApplication.getContext().getResources().getDrawable(R.drawable.rounded_band_incoming_trusted));
                mCallerThumb.setVisibility(View.VISIBLE);
                mCallerThumb.setImageResource(R.drawable.thumbsup_incoming_call);
                mCallerThumbsInfo.setText(thumbUpScore + " Thumb Up");
            } else {
                mCallerLogo.setImageResource(R.drawable.incoming_call_netral);
                mLayoutBand.setBackground(GlobalApplication.getContext().getResources().getDrawable(R.drawable.rounded_band_incoming_netral));
                mCallerThumb.setVisibility(View.GONE);
                mCallerThumbsInfo.setText(score + " Netral");
            }
            try {
                mCallerNumber.setText(PhoneNumberUtil.getInstance().getNumberType(PhoneNumberUtil.getInstance().parse(ReputasiUtils.validateNumber(phoneNumber),"")).toString().toLowerCase() + " | " + ReputasiUtils.validateNumber(phoneNumber));
            } catch (NumberParseException e) {
                mCallerNumber.setText("Unknown" + " | " + ReputasiUtils.validateNumber(phoneNumber));
            }
            wm.updateViewLayout(mBaseLayoutCard, mParams);
        }
    }


}
