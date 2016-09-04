package com.appspace.evyalert.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.evyalert.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventCommentActivityFragment extends Fragment {

    public EventCommentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_comment, container, false);
    }
}
