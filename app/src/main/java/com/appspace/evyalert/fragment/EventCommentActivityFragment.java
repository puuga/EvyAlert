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
import com.appspace.evyalert.activity.EventCommentActivity;
import com.appspace.evyalert.adapter.CommentAdapter;
import com.appspace.evyalert.model.Comment;
import com.appspace.evyalert.model.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCommentActivityFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeContainer;

    EventCommentActivity eventCommentActivity;

    Event event;
    RecyclerView recyclerView;
    List<Comment> commentList;

    public EventCommentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_comment, container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        eventCommentActivity = (EventCommentActivity) getActivity();
        event = eventCommentActivity.mEvent;

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        swipeContainer.setOnRefreshListener(this);

        commentList = new ArrayList<>();
        CommentAdapter adapter = new CommentAdapter(eventCommentActivity, event, commentList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(eventCommentActivity));
        recyclerView.setAdapter(adapter);
    }

    public void loadDataToRecyclerView(Comment[] comments) {
        if (commentList == null) {
//            getActivity().recreate();
            return;
        }
        commentList.clear();
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + commentList.size());
        commentList.addAll(Arrays.asList(comments));
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + commentList.size());
        recyclerView.getAdapter().notifyDataSetChanged();
        LoggerUtils.log2D("api", "loadDataToRecyclerView:OK - " + recyclerView.getAdapter().getItemCount());
    }

    @Override
    public void onRefresh() {
        eventCommentActivity.loadComment();
    }

    public void stopLayoutRefresh() {
        swipeContainer.setRefreshing(false);
    }
}
