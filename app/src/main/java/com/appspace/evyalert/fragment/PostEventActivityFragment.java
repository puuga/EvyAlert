package com.appspace.evyalert.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.R;
import com.appspace.evyalert.activity.PostEventActivity;
import com.bumptech.glide.Glide;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostEventActivityFragment extends Fragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    public ToggleButton toggleAccident;
    public ToggleButton toggleNaturalDisaster;
    public ToggleButton toggleOther;
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
        toggleAccident = (ToggleButton) view.findViewById(R.id.toggleAccident);
        toggleNaturalDisaster = (ToggleButton) view.findViewById(R.id.toggleNaturalDisaster);
        toggleOther = (ToggleButton) view.findViewById(R.id.toggleOther);
        edtEventTitle = (TextInputEditText) view.findViewById(R.id.edtEventTitle);
        ivEventImage = (ImageView) view.findViewById(R.id.ivEventImage);

        ivEventImage.setOnClickListener(this);
        toggleAccident.setOnCheckedChangeListener(this);
        toggleNaturalDisaster.setOnCheckedChangeListener(this);
        toggleOther.setOnCheckedChangeListener(this);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == toggleAccident && b) {
            toggleNaturalDisaster.setChecked(false);
            toggleOther.setChecked(false);
        } else if (compoundButton == toggleNaturalDisaster && b) {
            toggleAccident.setChecked(false);
            toggleOther.setChecked(false);
        } else if (compoundButton == toggleOther && b) {
            toggleAccident.setChecked(false);
            toggleNaturalDisaster.setChecked(false);
        }
    }
}
