package com.appspace.evyalert.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.R;
import com.appspace.evyalert.activity.MainActivity;
import com.appspace.evyalert.adapter.EventAdapter;
import com.appspace.evyalert.model.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EventListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "EventListFragment";

    private SwipeRefreshLayout swipeContainer;

    MainActivity mainActivity;

    RecyclerView recyclerView;
    List<Event> eventList;

    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance() {
        return new EventListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        initInstances(view);

        return view;
    }

    private void initInstances(View view) {
        mainActivity = (MainActivity) getActivity();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        swipeContainer.setOnRefreshListener(this);

        eventList = new ArrayList<>();
        EventAdapter adapter = new EventAdapter(mainActivity, eventList, mainActivity);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);
    }

    public void loadDataToRecyclerView(Event[] events) {
        if (eventList == null) {
//            getActivity().recreate();
            return;
        }
        eventList.clear();
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + eventList.size());
        eventList.addAll(Arrays.asList(events));
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + eventList.size());
        recyclerView.getAdapter().notifyDataSetChanged();
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + recyclerView.getAdapter().getItemCount());
    }

    public void reloadRecyclerView() {
        recyclerView.getAdapter().notifyDataSetChanged();
        LoggerUtils.log2D("api", "reloadRecyclerView:OK - ");
    }

    @Override
    public void onRefresh() {
        mainActivity.loadEventWithRecentOption();
    }

    public void stopLayoutRefresh() {
        if (swipeContainer == null)
            return;
        swipeContainer.setRefreshing(false);
    }
}
