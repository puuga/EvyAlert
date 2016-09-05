package com.appspace.evyalert.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.evyalert.R;
import com.appspace.evyalert.model.Comment;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.util.EventIconUtil;
import com.appspace.evyalert.util.Helper;
import com.appspace.evyalert.view.holder.CommentInCommentHolder;
import com.appspace.evyalert.view.holder.EventInCommentHolder;
import com.appspace.evyalert.view.holder.EventWithImageInCommentHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by siwaweswongcharoen on 9/5/2016 AD.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CommentAdapter";

    private Event event;
    private Context context;
    private List<Comment> commentList;
    private CommentInCommentHolder.OnCommentItemClickCallback callback;

    public CommentAdapter(
            Context context,
            Event event,
            List<Comment> commentList,
            CommentInCommentHolder.OnCommentItemClickCallback callback) {
        this.event = event;
        this.context = context;
        this.commentList = commentList;
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (event.eventPhotoUrl.equals(""))
                return Helper.HOLDER_TYPE_NO_IMAGE; // event no photo
            else
                return Helper.HOLDER_TYPE_IMAGE; // event has photo
        }

        return Helper.HOLDER_TYPE_COMMENT; // comment
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case Helper.HOLDER_TYPE_NO_IMAGE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_event_in_comment, parent, false);
                EventInCommentHolder holder = new EventInCommentHolder(itemView);
                setDataToEventHolder(holder, event);
                return holder;
            case Helper.HOLDER_TYPE_IMAGE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_event_with_image_in_comment, parent, false);
                EventWithImageInCommentHolder holder1 = new EventWithImageInCommentHolder(itemView);
                setDataToEventHolder(holder1, event);
                setEventImageToEventHolder(holder1, event);
            case Helper.HOLDER_TYPE_COMMENT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.holder_comment_in_comment, parent, false);
                return new CommentInCommentHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Helper.HOLDER_TYPE_COMMENT:
                CommentInCommentHolder holder1 = (CommentInCommentHolder) holder;
                Comment comment = commentList.get(position - 1);
                setDataToCommentHolder(holder1, comment);
                holder1.callback = callback;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size() + 1;
    }

    private void setDataToEventHolder(EventInCommentHolder holder, Event event) {
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
    }

    private void setEventImageToEventHolder(EventWithImageInCommentHolder holder, Event event) {
        Glide.with(context)
                .load(event.eventPhotoUrl)
                .centerCrop()
                .crossFade()
                .into(holder.ivEventPhoto);
    }

    private void setDataToCommentHolder(CommentInCommentHolder holder, Comment comment) {
        holder.comment = comment;

        Glide.with(context)
                .load(comment.userPhotoUrl)
                .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                .crossFade()
                .into(holder.ivProfile);

        holder.tvUsername.setText(comment.userName);

//        holder.tvTimeStamp.setText(TimeUtil.timpStampFormater(event.createdAtLong));
        String timeStamp = String.valueOf(DateUtils.getRelativeTimeSpanString(comment.createdAtLong));
        holder.tvTimeStamp.setText(timeStamp);

        holder.tvCommentTitle.setText(comment.comment);
        ;

    }
}
