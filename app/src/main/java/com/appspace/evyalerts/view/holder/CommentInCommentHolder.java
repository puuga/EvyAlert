package com.appspace.evyalerts.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalerts.R;
import com.appspace.evyalerts.model.Comment;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class CommentInCommentHolder extends RecyclerView.ViewHolder implements
        View.OnLongClickListener {

    public static final String TAG = "EventHolder";

    public ImageView ivProfile;
    public TextView tvUsername;
    public TextView tvTimeStamp;
    public TextView tvCommentTitle;

    public Comment comment;
    public OnCommentItemClickCallback callback;

    public CommentInCommentHolder(View itemView) {
        super(itemView);

        ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
        tvCommentTitle = (TextView) itemView.findViewById(R.id.tvCommentTitle);

        itemView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        LoggerUtils.log2D(TAG, "onCommentItemLongClickCallback: " + comment.id);
        if (comment.userUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            callback.onCommentItemLongClickCallback(comment);
            return true;
        }
        return false;
    }

    public interface OnCommentItemClickCallback {
        void onCommentItemClickCallback(Comment comment);

        void onCommentItemLongClickCallback(Comment comment);

        void onUserProfileCommentItemClickCallback(Comment comment);
    }
}
