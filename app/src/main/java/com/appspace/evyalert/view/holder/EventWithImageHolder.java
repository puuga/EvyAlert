package com.appspace.evyalert.view.holder;

import android.view.View;
import android.widget.ImageView;

import com.appspace.evyalert.R;

/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class EventWithImageHolder extends EventHolder {

    public ImageView ivEventPhoto;

    public EventWithImageHolder(View itemView) {
        super(itemView);

        ivEventPhoto = (ImageView) itemView.findViewById(R.id.ivEventPhoto);
    }
}
