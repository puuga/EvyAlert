package com.appspace.evyalert.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspace.evyalert.R;
import com.appspace.evyalert.model.Comment;
import com.appspace.evyalert.model.Event;


/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class CommentInCommentHolder extends RecyclerView.ViewHolder {

    public static final String TAG = "EventHolder";

    public ImageView ivProfile;
    public TextView tvUsername;
    public TextView tvTimeStamp;
    public TextView tvCommentTitle;

    public int listPosition;
    public Comment comment;

    public CommentInCommentHolder(View itemView) {
        super(itemView);

        ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
        tvCommentTitle = (TextView) itemView.findViewById(R.id.tvCommentTitle);
    }

    public interface OnEventItemClickCallback {
        void onCommentItemClickCallback(Event event, int position);

        void onUserProfileCommentItemClickCallback(Event event, int position);
    }
}
