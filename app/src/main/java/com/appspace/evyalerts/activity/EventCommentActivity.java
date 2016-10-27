package com.appspace.evyalerts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalerts.R;
import com.appspace.evyalerts.fragment.EventCommentActivityFragment;
import com.appspace.evyalerts.manager.ApiManager;
import com.appspace.evyalerts.model.Comment;
import com.appspace.evyalerts.model.Event;
import com.appspace.evyalerts.util.ArrayUtil;
import com.appspace.evyalerts.util.Helper;
import com.appspace.evyalerts.util.TimeUtil;
import com.appspace.evyalerts.view.holder.CommentInCommentHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCommentActivity extends AppCompatActivity implements
        CommentInCommentHolder.OnCommentItemClickCallback {
    private final String TAG = "EventCommentActivity";

    public Event mEvent;

    MaterialDialog mProgressDialog;

    boolean mDidChangeComment = false;

    Comment[] mComment;

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
                mDidChangeComment = true;
                Intent returnIntent = new Intent();
                setResult(Helper.RESULT_DID_COMMENT, returnIntent);
                Comment[] comments = response.body();
                LoggerUtils.log2D(TAG, "comments:" + comments.length);
                mComment = comments;
                loadDataToView(comments);
            }

            @Override
            public void onFailure(Call<Comment[]> call, Throwable t) {
                hideProgressDialog();
                FirebaseCrash.report(t);
            }
        });
    }

    public void postComment(String commentString) {
        showProgressDialog();
        final EventCommentActivityFragment f = (EventCommentActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        Call<Comment> call = ApiManager.getInstance().getAPIService()
                .postComment(
                        mEvent.eventUid,
                        commentString,
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(),
                        String.valueOf(TimeUtil.getCurrentTime())
                );
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                hideProgressDialog();
                Comment comment = response.body();
                mComment = ArrayUtil.append(mComment, comment);
                loadDataToView(mComment);
                f.clearCommentText();
                f.scrollToLastPosition();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                f.clearCommentText();
                hideProgressDialog();
                FirebaseCrash.report(t);
            }
        });
    }

    public void deleteComment(Comment comment) {
        showProgressDialog();
        Call<Response<Void>> call = ApiManager.getInstance().getAPIService()
                .deleteComment(String.valueOf(comment.id));
        call.enqueue(new Callback<Response<Void>>() {
            @Override
            public void onResponse(Call<Response<Void>> call, Response<Response<Void>> response) {
                hideProgressDialog();
                if (response.code() == 204) {
                    loadComment();
                }
            }

            @Override
            public void onFailure(Call<Response<Void>> call, Throwable t) {
                hideProgressDialog();
                FirebaseCrash.report(t);
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

    @Override
    public void onCommentItemClickCallback(Comment comment) {

    }

    @Override
    public void onCommentItemLongClickCallback(final Comment comment) {
        LoggerUtils.log2D(TAG, "onCommentItemLongClickCallback: " + comment.id);
        new MaterialDialog.Builder(this)
                .title(R.string.delete)
                .content(R.string.delete_confirm)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .neutralText(R.string.later)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteComment(comment);
                    }
                })
                .show();
    }

    @Override
    public void onUserProfileCommentItemClickCallback(Comment comment) {

    }
}
