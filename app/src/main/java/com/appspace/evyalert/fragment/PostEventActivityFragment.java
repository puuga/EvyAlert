package com.appspace.evyalert.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.R;
import com.appspace.evyalert.activity.PostEventActivity;
import com.bumptech.glide.Glide;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostEventActivityFragment extends Fragment implements
        View.OnClickListener {

    public RadioButton radioAccident;
    public RadioButton radioNaturalDisaster;
    public RadioButton radioOther;
    public TextInputEditText edtEventTitle;
    public ImageView ivEventImage;

    public PostEventActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_event, container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        radioAccident = (RadioButton) view.findViewById(R.id.radioAccident);
        radioNaturalDisaster = (RadioButton) view.findViewById(R.id.radioNaturalDisaster);
        radioOther = (RadioButton) view.findViewById(R.id.radioOther);
        edtEventTitle = (TextInputEditText) view.findViewById(R.id.edtEventTitle);
        ivEventImage = (ImageView) view.findViewById(R.id.ivEventImage);

        ivEventImage.setOnClickListener(this);
    }

    public void checkEditMode() {
        PostEventActivity activity = (PostEventActivity) getActivity();
        if (activity.isEditEvent) {
            LoggerUtils.log2D("PostEventActivityFragment", "EDIT_MODE newsId: " + activity.event);

            edtEventTitle.setText(activity.event.title);

            ivEventImage.setOnClickListener(null);
            Glide.with(getActivity())
                    .load(activity.event.eventPhotoUrl)
                    .centerCrop()
                    .crossFade()
                    .into(ivEventImage);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == ivEventImage) {
            ((PostEventActivity) getActivity()).bottomSheetDialogFragment
                    .show(getActivity().getSupportFragmentManager(), "bottomSheetDialogFragment");
        }
    }
}
