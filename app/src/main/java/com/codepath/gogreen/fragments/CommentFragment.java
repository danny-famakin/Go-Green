package com.codepath.gogreen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.gogreen.CommentAdapter;
import com.codepath.gogreen.DividerItemDecoration;
import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Comment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melissaperez on 7/26/17.
 */

public class CommentFragment extends Fragment{
    ArrayList<Comment> comments;
    RecyclerView rvComments;
    CommentAdapter commentAdapter;
    ParseUser currentUser;
    private SwipeRefreshLayout swipeContainer;
    View view;
    String actionID;

    public static CommentFragment newInstance() {
        Bundle args = new Bundle();
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionID = getArguments().getString("aid");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comment_list, container, false);
        rvComments = (RecyclerView) v.findViewById(R.id.rvComments);
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.addItemDecoration(new DividerItemDecoration(getContext()));
        // set the adapter
        rvComments.setAdapter(commentAdapter);
      //  update();

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swiper);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentAdapter.clear();
                update();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorAccent,
                R.color.colorAccentDark,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        return v;
    }

    public void update() {
        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo("aid", actionID);
        query.findInBackground(new FindCallback<Comment>() {
            public void done(List<Comment> commentList, ParseException e) {
                if (e == null) {
                    addItems(commentList);
                } else {
                    Log.d("action", "Error: " + e.getMessage());
                }
            }
        });
    }


    public void addItems(List<Comment> commentList) {
        // iterate through JSON array
        // for each entry, deserialize the JSON object
        for (int i = 0; i < commentList.size(); i++) {
            Comment comment = commentList.get(i);
            comments.add(0, comment);
            commentAdapter.notifyItemInserted(0);
        }
    }

    public void addComment(Comment comment) {
        comments.add(0, comment);
        commentAdapter.notifyItemInserted(0);
        rvComments.scrollToPosition(0);
    }
}
