package com.appspace.evyalerts.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.evyalerts.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class IntroActivityFragment extends Fragment {

    public IntroActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
    }
}
