package com.appspace.evyalert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.R;
import com.appspace.evyalert.fragment.EventCommentActivityFragment;
import com.appspace.evyalert.manager.ApiManager;
import com.appspace.evyalert.model.Comment;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.util.Helper;
import com.google.firebase.crash.FirebaseCrash;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCommentActivity extends AppCompatActivity {
    private final String TAG = "EventCommentActivity";

    public Event mEvent;

    MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra();
        setContentView(R.layout.activity_event_comment);

        initInstances();

        loadComment();
    }

    private void getExtra() {
        Intent i = getIntent();
        Event event = i.getExtras().getParcelable(Helper.KEY_EVENT_ITEM);
        if (event != null) {
            LoggerUtils.log2D(TAG, event.eventUid);
            mEvent = event;
        }
    }

    private void initInstances() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progressing)
                .autoDismiss(false)
                .progress(true, 0)
                .build();
    }

    public void loadComment() {
        showProgressDialog();
        Call<Comment[]> call = ApiManager.getInstance().getAPIService()
                .loadComment(mEvent.eventUid);
        call.enqueue(new Callback<Comment[]>() {
            @Override
            public void onResponse(Call<Comment[]> call, Response<Comment[]> response) {
                hideProgressDialog();
                Comment[] comments = response.body();
                LoggerUtils.log2D(TAG, "comments:"+comments.length);
                loadDataToView(comments);
            }

            @Override
            public void onFailure(Call<Comment[]> call, Throwable t) {
                hideProgressDialog();
                FirebaseCrash.report(t);
                LoggerUtils.log2D(TAG, t.getMessage());
            }
        });
    }

    private void loadDataToView(Comment[] comments) {
        EventCommentActivityFragment f = (EventCommentActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        f.loadDataToRecyclerView(comments);
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.dismiss();
        EventCommentActivityFragment f = (EventCommentActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        f.stopLayoutRefresh();
    }


}
