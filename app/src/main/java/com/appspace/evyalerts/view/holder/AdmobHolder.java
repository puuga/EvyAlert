package com.appspace.evyalerts.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appspace.evyalerts.R;
import com.google.android.gms.ads.AdView;

/**
 * Created by siwaweswongcharoen on 8/14/2016 AD.
 */
public class AdmobHolder extends RecyclerView.ViewHolder {

    public AdView mAdView;

    public AdmobHolder(View itemView) {
        super(itemView);

        mAdView = (AdView) itemView.findViewById(R.id.adView);
    }
}
