package com.codepath.gogreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.codepath.gogreen.R;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by anyazhang on 7/21/17.
 */

public class TabsFragment extends Fragment {
    LayoutInflater inflater;
    View v;
    Context context;
    public ArrayList<String> friendsList;
    ParseUser currentUser;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_tabs, null);


    }

    public ArrayList<String> loadFriends() {
        Log.d("loadFriends", "loading");
        currentUser = ParseUser.getCurrentUser();
        final String userId = currentUser.getString("fbId");
        ArrayList<String> friendsList = new ArrayList<String>();
        JSONArray friends = currentUser.getJSONArray("friends");

        for (int i = 0; i < friends.length(); i++) {
            try {
                if (friends.getString(i) != null) {
                    friendsList.add(friends.getString(i));
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        friendsList.add(userId);
        return friendsList;


    }

}
