package com.codepath.gogreen.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.gogreen.R;
import com.codepath.gogreen.UserAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anyazhang on 7/13/17.
 */

public class LeaderboardFragment extends FloatingMenuFragment {
    ArrayList<ParseUser> users;
    RecyclerView rvUsers;
    UserAdapter userAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        rvUsers = (RecyclerView) v.findViewById(R.id.rvLeaderboard);
        users = new ArrayList<>();
        userAdapter = new UserAdapter(users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUsers.setLayoutManager(linearLayoutManager);
        // set the adapter
        rvUsers.setAdapter(userAdapter);

        return v;
    }

    @Override
    public void onFriendsLoaded(ArrayList<String> friendIdList) {
        Log.d("leaderboard", "loaded");
        Log.d("leaderboard", String.valueOf(friendIdList.size()));
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereContainedIn("fbId", friendIdList);
        query.orderByAscending("totalPoints");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> friendUserList, ParseException e) {
                if (e == null) {
                    addItems(friendUserList);
                    Log.d("leaderboard", String.valueOf(friendUserList.size()));
                } else {
                    Log.d("user", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void addItems(List<ParseUser> userList) {
        // iterate through JSON array
        // for each entry, deserialize the JSON object
        Log.d("addItems", String.valueOf(userList.size()));
        for (int i = 0; i < userList.size(); i++) {
            ParseUser user= userList.get(i);
            users.add(0, user);
            userAdapter.notifyItemInserted(0);
            Log.d("addItems", String.valueOf(users.size()) + ": " + user.getString("name"));

        }
    }

}
