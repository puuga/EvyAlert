package com.appspace.evyalert.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.evyalert.R;
import com.appspace.evyalert.util.FirebaseUserUtil;
import com.appspace.evyalert.util.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    final String TAG = "IntroActivity";

    CoordinatorLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initInstances();
        checkPermission();
        checkLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.LOGIN_RESUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(container, R.string.login_ok, Snackbar.LENGTH_SHORT)
                        .show();
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

    private void initInstances() {
        container = (CoordinatorLayout) findViewById(R.id.container);
    }

    private void checkPermission() {

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
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
