package com.appspace.evyalerts.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalerts.R;
import com.appspace.evyalerts.activity.PostEventActivity;
import com.appspace.evyalerts.model.ProvinceCentroid;
import com.appspace.evyalerts.util.DistanceUtil;
import com.appspace.evyalerts.util.ViewBlinkingUtil;
import com.bumptech.glide.Glide;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostEventActivityFragment extends Fragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {

    public ToggleButton toggleAccident;
    public ToggleButton toggleNaturalDisaster;
    public ToggleButton toggleOther;
    public ToggleButton toggleTrafficJam;
    public TextInputEditText edtEventTitle;
    public ImageView ivEventImage;
    public Spinner spnProvince;
    public TextView tvDistanceError;

    public int eventTypeIndex = -1;
    public int provinceIndex = 0;
    public boolean didSelectedProvince = false;

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
        toggleTrafficJam = (ToggleButton) view.findViewById(R.id.toggleTrafficJam);
        edtEventTitle = (TextInputEditText) view.findViewById(R.id.edtEventTitle);
        ivEventImage = (ImageView) view.findViewById(R.id.ivEventImage);
        spnProvince = (Spinner) view.findViewById(R.id.spnProvince);
        tvDistanceError = (TextView) view.findViewById(R.id.tvDistanceError);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.province, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProvince.setAdapter(adapter);

        ivEventImage.setOnClickListener(this);
        toggleAccident.setOnCheckedChangeListener(this);
        toggleNaturalDisaster.setOnCheckedChangeListener(this);
        toggleOther.setOnCheckedChangeListener(this);
        toggleTrafficJam.setOnCheckedChangeListener(this);
        spnProvince.setOnItemSelectedListener(this);
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

    public void focusOnSpnProvince() {
        ViewBlinkingUtil.blinking(tvDistanceError);
        spnProvince.performClick();
    }

    private void setEventButtonToNornalState() {
        toggleAccident.setChecked(false);
        toggleNaturalDisaster.setChecked(false);
        toggleOther.setChecked(false);
        toggleTrafficJam.setChecked(false);
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
        if (compoundButton == toggleAccident) {
            eventTypeIndex = b ? 0 : -1;

            toggleNaturalDisaster.setChecked(false);
            toggleOther.setChecked(false);
            toggleTrafficJam.setChecked(false);
        } else if (compoundButton == toggleNaturalDisaster) {
            eventTypeIndex = b ? 1 : -1;

            toggleAccident.setChecked(false);
            toggleOther.setChecked(false);
            toggleTrafficJam.setChecked(false);
        } else if (compoundButton == toggleOther) {
            eventTypeIndex = b ? 2 : -1;

            toggleAccident.setChecked(false);
            toggleNaturalDisaster.setChecked(false);
            toggleTrafficJam.setChecked(false);
        } else if (compoundButton == toggleTrafficJam) {
            eventTypeIndex = b ? 3 : -1;

            toggleAccident.setChecked(false);
            toggleNaturalDisaster.setChecked(false);
            toggleOther.setChecked(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        provinceIndex = i;
        didSelectedProvince = true;
        LoggerUtils.log2D("onItemSelected", String.valueOf(i));
        LoggerUtils.log2D("onItemSelected", adapterView.getItemAtPosition(i).toString());

        PostEventActivity activity = (PostEventActivity) getActivity();
        double latitude = activity.latitude;
        double longitude = activity.longitude;
        ProvinceCentroid centroid = DistanceUtil.getInstance().provinceCentroids[provinceIndex];

        float distance = DistanceUtil.getInstance().distanceBetween(latitude, longitude, centroid);
        boolean isTooFar = DistanceUtil.getInstance().isTooFar(distance);
        LoggerUtils.log2D("distance", String.valueOf(isTooFar));
        if (isTooFar) {
            String text = getString(R.string.too_far, adapterView.getItemAtPosition(i).toString());
            ViewBlinkingUtil.blinking(tvDistanceError);
            tvDistanceError.setText(text);
            activity.canPostEvent = false;
        } else {
            tvDistanceError.setText("");
            activity.canPostEvent = true;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
