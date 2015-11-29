package com.reputasi.callblocker.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.reputasi.callblocker.R;
import com.reputasi.library.preference.UserPreferenceManager;

/**
 * Created by Vikraa on 3/4/2015.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!UserPreferenceManager.getDisclaimerAgreement()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    View v = LayoutInflater.from(SplashActivity.this).inflate(R.layout.dialog_disclaimer, null);
                    builder.setView(v)
                            .setTitle(getString(R.string.disclaimer_title))
                            .setPositiveButton(getString(R.string.disclaimer_button_agree), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserPreferenceManager.putDisclaimerAgreement(true);
                                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(getString(R.string.disclaimer_button_exit), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserPreferenceManager.putDisclaimerAgreement(false);
                                    System.exit(0);
                                }
                            })
                            .setCancelable(false);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
                /*if (!UserManager.getInstance().isUserLoggedIn()) {
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                } else if (!UserManager.getInstance().isPhoneNumberRegistered()) {
                    Intent registerIntent = new Intent(SplashActivity.this, RegistrationActivity.class);
                    startActivity(registerIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }*/
            }
        }, 1000L);
    }


}
