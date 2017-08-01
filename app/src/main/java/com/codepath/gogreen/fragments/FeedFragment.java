package com.codepath.gogreen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.gogreen.ActionAdapter;
import com.codepath.gogreen.DividerItemDecoration;
import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Action;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anyazhang on 7/13/17.
 */

public class FeedFragment extends FloatingMenuFragment {
    ArrayList<Action> actions;
    RecyclerView rvActions;
    ActionAdapter actionAdapter;
    ParseUser currentUser;
    private SwipeRefreshLayout swipeContainer;
    ArrayList<String> friendIdList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        rvActions = (RecyclerView) v.findViewById(R.id.rvFeed);
        actions = new ArrayList<>();
        actionAdapter = new ActionAdapter(actions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvActions.setLayoutManager(linearLayoutManager);
        rvActions.addItemDecoration(new DividerItemDecoration(getContext()));
        // set the adapter
        rvActions.setAdapter(actionAdapter);
        update();

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swiper);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionAdapter.clear();
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
        friendIdList = loadFriends();
        ParseQuery<Action> query = ParseQuery.getQuery("Action");
        query.whereContainedIn("uid", friendIdList);
        query.findInBackground(new FindCallback<Action>() {
            public void done(List<Action> actionList, ParseException e) {
                if (e == null) {
                    addItems(actionList);
                } else {
                    Log.d("action", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void addItems(List<Action> actionList) {
        // iterate through JSON array
        // for each entry, deserialize the JSON object
        for (int i = 0; i < actionList.size(); i++) {
            Action action = actionList.get(i);
            actions.add(0, action);
            actionAdapter.notifyItemInserted(0);
        }
    }

    public void addAction(Action action) {
        actions.add(0, action);
        actionAdapter.notifyItemInserted(0);
        rvActions.scrollToPosition(0);
    }
}