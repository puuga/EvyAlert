package com.appspace.evyalert.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evyalert.R;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.util.EventIconUtil;
import com.appspace.evyalert.view.holder.EventHolder;
import com.appspace.evyalert.view.holder.EventWithImageHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "EventAdapter";

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
        if (event.eventPhotoUrl.equals(""))
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
        final Event event = eventList.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                EventHolder holder0 = (EventHolder) holder;
                setDataToHolder(holder0, event);

                setOnEventItemClickCallback(holder0, event, holder.getAdapterPosition());
                break;
            case 1:
                EventWithImageHolder holder1 = (EventWithImageHolder) holder;
                setDataToHolder(holder1, event);
                setEventImageToHolder(holder1, event);

                setOnEventItemClickCallback(holder1, event, holder.getAdapterPosition());
                break;
        }
    }

    private void setDataToHolder(EventHolder holder, Event event) {
        Glide.with(context)
                .load(event.userPhotoUrl)
                .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                .crossFade()
                .into(holder.ivProfile);

        holder.tvUsername.setText(event.userName);

        holder.tvTimeStamp.setText(event.createdAt);

        String eventText = context.getResources()
                .getStringArray(R.array.event_type)[Integer.parseInt(event.eventTypeIndex)];
        holder.tvEventType.setText(eventText);

        int eventImageResource = EventIconUtil.eventIcons[Integer.parseInt(event.eventTypeIndex)];
        holder.ivEventType.setImageResource(eventImageResource);

        holder.tvEventTitle.setText(event.title);
    }

    private void setEventImageToHolder(EventWithImageHolder holder, Event event) {
        Glide.with(context)
                .load(event.eventPhotoUrl)
                .centerCrop()
                .crossFade()
                .into(holder.ivEventPhoto);
    }

    private void setOnEventItemClickCallback(EventHolder holder, final Event event, final int adapterPosition) {
        LoggerUtils.log2D(TAG, "onEventItemClickCallback: " + adapterPosition);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onEventItemClickCallback(event, adapterPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList == null ? 0 : eventList.size();
    }
}
