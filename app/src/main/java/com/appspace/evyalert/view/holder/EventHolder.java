package com.appspace.evyalert.view.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspace.evyalert.R;

/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class EventHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public ImageView ivProfile;
    public TextView tvUsername;
    public TextView tvTimeStamp;
    public TextView tvEventTitle;
    public ImageView ivEventType;
    public TextView tvEventType;

    public EventHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.cardView);
        ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
        tvEventTitle = (TextView) itemView.findViewById(R.id.tvEventTitle);
        ivEventType = (ImageView) itemView.findViewById(R.id.ivEventType);
        tvEventType = (TextView) itemView.findViewById(R.id.tvEventType);
    }
}
