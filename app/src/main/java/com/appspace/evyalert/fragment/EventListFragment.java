package com.appspace.evyalert.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.appspace.evyalert.util.TimeUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EventListFragment extends Fragment implements EventAdapter.OnEventItemClickCallback {

    MainActivity mainActivity;

    Event[] events;
    RecyclerView recyclerView;
    List<Event> eventList;

    private DatabaseReference mEventsRef;

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
        initFirebase();



        loadEvent(2);
        return view;
    }

    private void initFirebase() {
        mEventsRef = FirebaseDatabase.getInstance().getReference().child("events");
    }

    private void initInstances(View view) {
        mainActivity = (MainActivity) getActivity();

        eventList = new ArrayList<>();
        EventAdapter adapter = new EventAdapter(mainActivity, eventList, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEventItemClickCallback(Event event, int position) {

    }

    @Override
    public void onEventItemPhotoClickCallback(Event event, int position) {

    }

    public void loadEvent(int option) {
        switch (option) {
            case 0:
                loadEventNearBy(20);
                break;
            case 1:
                loadEventNearBy(50);
                break;
            case 2:
                loadEventLast2Days();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            default:
        }
    }

    private void loadEventNearBy(int range) {
    }

    private void loadEventLast2Days() {
        Long now = TimeUtil.getCurrentTime();
        Long last2Day = TimeUtil.getLast2DaysTime();
        LoggerUtils.log2D("loadEventLast2Days", "now: " + now);
        LoggerUtils.log2D("loadEventLast2Days", "last2Day: " + last2Day);
//        Query eventsLast2Days = mEventsRef
//                .startAt(now)
//                .endAt(last2Day);
        Query eventsLast2Days = mEventsRef.orderByChild("createdAtLong").limitToFirst(10);
        eventsLast2Days.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LoggerUtils.log2D("loadEventLast2Days", "count: " + dataSnapshot.getChildrenCount());
                eventList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Event event = data.getValue(Event.class);
                    eventList.add(event);
                    LoggerUtils.log2D("loadEventLast2Days", "event: " + data.getKey());
                }
                reloadRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadDataToRecyclerView(Event[] events) {
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
}
