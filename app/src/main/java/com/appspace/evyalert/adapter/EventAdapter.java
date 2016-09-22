package com.appspace.evyalert.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.evyalert.R;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.util.EventIconUtil;
import com.appspace.evyalert.util.Helper;
import com.appspace.evyalert.view.holder.AdmobHolder;
import com.appspace.evyalert.view.holder.EventHolder;
import com.appspace.evyalert.view.holder.EventWithImageHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by siwaweswongcharoen on 8/11/2016 AD.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "EventAdapter";

    private Context context;
    private List<Event> eventList;
    private EventHolder.OnEventItemClickCallback callback;

    public EventAdapter(Context context, List<Event> eventList, EventHolder.OnEventItemClickCallback callback) {
        this.context = context;
        this.eventList = eventList;
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) {
            return Helper.HOLDER_TYPE_ADMOB; // admob
        } else if (position != 0 && (position % 10 == 0)) {
            return Helper.HOLDER_TYPE_ADMOB; // admob
        }

        int listPosition;
        if (position == 0)
            listPosition = position;
        else if (position <= 1)
            listPosition = position;
        else
            listPosition = position - 1 - (position / 10);

        Event event = eventList.get(listPosition);

        if (event.eventPhotoUrl.equals(""))
            return Helper.HOLDER_TYPE_NO_IMAGE; // no photo
        else
            return Helper.HOLDER_TYPE_IMAGE; // has photo
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case Helper.HOLDER_TYPE_NO_IMAGE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_event, parent, false);
                return new EventHolder(itemView);
            case Helper.HOLDER_TYPE_IMAGE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_event_with_image, parent, false);
                return new EventWithImageHolder(itemView);
            case Helper.HOLDER_TYPE_ADMOB:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_admob, parent, false);
                AdmobHolder admobHolder = new AdmobHolder(itemView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("3EC1EF88FD766483AA48DEDC3AAC8A18")
                        .build();
                admobHolder.mAdView.loadAd(adRequest);
                return admobHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0 && (position == 2 || position % 10 == 0))
            return;

        int listPosition;
        if (position == 0)
            listPosition = position;
        else if (position <= 1)
            listPosition = position;
        else
            listPosition = position - 1 - (position / 10);

        final Event event = eventList.get(listPosition);

        switch (holder.getItemViewType()) {
            case Helper.HOLDER_TYPE_NO_IMAGE:
                EventHolder holder0 = (EventHolder) holder;
                setDataToHolder(holder0, event, listPosition);

//                setOnEventItemClickCallback(holder0, event);
//                setOnEventItemLongClickCallback(holder0, event);
                break;
            case Helper.HOLDER_TYPE_IMAGE:
                EventWithImageHolder holder1 = (EventWithImageHolder) holder;
                setDataToHolder(holder1, event, listPosition);
                setEventImageToHolder(holder1, event);

//                setOnEventItemClickCallback(holder1, event);
//                setOnEventItemLongClickCallback(holder1, event);
                break;
        }
    }

    private void setDataToHolder(EventHolder holder, Event event, int listPosition) {
        holder.listPosition = listPosition;
        holder.callback = callback;
        holder.event = event;

        Glide.with(context)
                .load(event.userPhotoUrl)
                .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                .crossFade()
                .into(holder.ivProfile);

        holder.tvUsername.setText(event.userName);

//        holder.tvTimeStamp.setText(TimeUtil.timpStampFormater(event.createdAtLong));
        String timeStamp = String.valueOf(DateUtils.getRelativeTimeSpanString(event.createdAtLong));
        holder.tvTimeStamp.setText(timeStamp);

        String eventText = context.getResources()
                .getStringArray(R.array.event_type)[Integer.parseInt(event.eventTypeIndex)];
        holder.tvEventType.setText(eventText);

        int eventImageResource = EventIconUtil.eventColorIcons[Integer.parseInt(event.eventTypeIndex)];
        holder.ivEventType.setImageResource(eventImageResource);

        holder.tvEventTitle.setText(event.title);

        String province = context.getResources()
                .getStringArray(R.array.province)[Integer.parseInt(event.provinceIndex)];
        holder.tvProvince.setText(province);

        if (event.numberOfComments == 0)
            holder.btnComment.setText(context.getString(R.string.comment));
        else
            holder.btnComment.setText(context.getString(R.string.comment_with_number, event.numberOfComments));
    }

    private void setEventImageToHolder(EventWithImageHolder holder, Event event) {
        Glide.with(context)
                .load(event.eventPhotoUrl)
                .centerCrop()
                .crossFade()
                .into(holder.ivEventPhoto);
    }

    @Override
    public int getItemCount() {
        if (eventList == null)
            return 0;
        else if (eventList.size() <= 2)
            return eventList.size();
        else
            return eventList.size() + 1 + (eventList.size() / 10);
    }
}
