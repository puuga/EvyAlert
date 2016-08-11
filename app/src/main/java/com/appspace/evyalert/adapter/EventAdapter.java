package com.appspace.evyalert.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.evyalert.R;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.view.holder.EventHolder;
import com.appspace.evyalert.view.holder.EventWithImageHolder;

import java.util.List;

/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Event> eventList;
    private OnEventItemClickCallback callback;

    public interface OnEventItemClickCallback {
        void onEventItemClickCallback(Event event, int position);
        void onEventItemPhotoClickCallback(Event event, int position);
    }

    public EventAdapter(Context context, List<Event> eventList, OnEventItemClickCallback callback) {
        this.context = context;
        this.eventList = eventList;
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        Event event = eventList.get(position);
        if (!event.eventPhotoUrl.equals(""))
            return 0; // no photo
        else
            return 1; // has photo
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_event, parent, false);
                return new EventHolder(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_event_with_image, parent, false);
                return new EventWithImageHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
