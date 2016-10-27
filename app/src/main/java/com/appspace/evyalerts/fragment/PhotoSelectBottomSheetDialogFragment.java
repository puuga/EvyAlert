package com.appspace.evyalerts.fragment;


import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.appspace.evyalerts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoSelectBottomSheetDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            };

    Button btnImageFromCamera;
    Button btnImageFromGallery;

    OnBottomSheetItemClickListener listener;

    public interface OnBottomSheetItemClickListener {
        void onCameraClick();

        void onGalleryClick();
    }

    public void setOnBottomSheetItemClickListener(OnBottomSheetItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.fragment_photo_select_bottom_sheet_dialog, null);
        dialog.setContentView(view);

        initInstances(view);
    }

    private void initInstances(View view) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        btnImageFromCamera = (Button) view.findViewById(R.id.btnImageFromCamera);
        btnImageFromGallery = (Button) view.findViewById(R.id.btnImageFromGallery);

        btnImageFromCamera.setOnClickListener(this);
        btnImageFromGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnImageFromCamera.getId()) {
            listener.onCameraClick();
        } else if (view.getId() == btnImageFromGallery.getId()) {
            listener.onGalleryClick();
        }
    }
}
