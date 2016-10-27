package com.appspace.evyalerts.view.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalerts.R;
import com.appspace.evyalerts.model.Event;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class EventHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnLongClickListener {

    public static final String TAG = "EventHolder";

    public CardView cardView;
    public ImageView ivProfile;
    public TextView tvUsername;
    public TextView tvTimeStamp;
    public TextView tvEventTitle;
    public ImageView ivEventType;
    public TextView tvEventType;
    public TextView tvProvince;
    public Button btnComment;

    public int listPosition;
    public Event event;
    public OnEventItemClickCallback callback;

    public EventHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.cardView);
        ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
        tvEventTitle = (TextView) itemView.findViewById(R.id.tvEventTitle);
        ivEventType = (ImageView) itemView.findViewById(R.id.ivEventType);
        tvEventType = (TextView) itemView.findViewById(R.id.tvEventType);
        tvProvince = (TextView) itemView.findViewById(R.id.tvProvince);
        btnComment = (Button) itemView.findViewById(R.id.btnComment);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        btnComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnComment) {
            LoggerUtils.log2D(TAG, "onEventItemCommentClickCallback: " + listPosition);
            callback.onEventItemCommentClickCallback(event, listPosition);
        } else {
            LoggerUtils.log2D(TAG, "onEventItemClickCallback: " + listPosition);
            callback.onEventItemClickCallback(event, listPosition);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        LoggerUtils.log2D(TAG, "onEventItemLongClickCallback: " + listPosition);
        if (event.userUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            callback.onEventItemLongClickCallback(event, listPosition);
            return true;
        }
        return false;
    }

    public interface OnEventItemClickCallback {
        void onEventItemClickCallback(Event event, int position);

        void onEventItemLongClickCallback(Event event, int position);

        void onEventItemCommentClickCallback(Event event, int position);

        void onEventItemPhotoClickCallback(Event event, int position);
    }
}
