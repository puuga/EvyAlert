package com.appspace.evyalerts.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.util.InternetUtil;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalerts.BuildConfig;
import com.appspace.evyalerts.R;
import com.appspace.evyalerts.util.FirebaseUserUtil;
import com.appspace.evyalerts.util.Helper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class IntroActivity extends AppCompatActivity {

    final String TAG = "IntroActivity";

    CoordinatorLayout container;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initInstances();

        initAdMob();

        initFirebase();

        checkInternet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.LOGIN_RESUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                Snackbar.make(container, R.string.login_ok, Snackbar.LENGTH_SHORT)
//                        .show();
                gotoMainActivity();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(container, R.string.login_cancel, Snackbar.LENGTH_SHORT)
                        .show();

                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.need_login)
                        .content(R.string.need_login_description)
                        .positiveText(R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                gotoLoginActivity();
                            }
                        })
                        .show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkInternet() {
        if (!InternetUtil.isInternetAvailable(this)) {
            LoggerUtils.log2D("internet", "no internet");

            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title(R.string.no_internet)
                    .content(R.string.no_internet_description)
                    .positiveText(R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .autoDismiss(false)
                    .show();
        } else {
            checkPermission();
        }

    }

    private void initInstances() {
        container = (CoordinatorLayout) findViewById(R.id.container);
    }

    private void initAdMob() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6174292774814788~4101189953");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("3EC1EF88FD766483AA48DEDC3AAC8A18")
                .build();
        mAdView.loadAd(adRequest);
    }

    private void initFirebase() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Helper.REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    checkPermission();
                }
                return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermission() {
// check android.permission.ACCESS_FINE_LOCATION permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            checkLogin();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Helper.REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void checkLogin() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            gotoLoginActivity();
        else {
            FirebaseUserUtil.updateProfilePhotoUri(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    }
                    gotoMainActivity();
                }
            });
        }
    }

    protected void gotoLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, Helper.LOGIN_RESUEST_CODE);
    }

    protected void gotoMainActivity() {
        LoggerUtils.log2D(TAG, BuildConfig.BUILD_TYPE);
        Handler handler = new Handler();
        long delay = BuildConfig.DEBUG ? 500 : mFirebaseRemoteConfig.getLong(Helper.INTRO_DELAY);
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, delay);
    }

}
