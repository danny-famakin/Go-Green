package com.codepath.gogreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anyazhang on 7/31/17.
 */

public class SearchFragment extends Fragment {
    String searchQuery;
    ArrayList<ParseUser> users;
    RecyclerView rvResult;
    UserAdapter userAdapter;
    LayoutInflater inflater;
    View v;
    Context context;
    TextView tvError;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();
        rvResult = (RecyclerView) v.findViewById(R.id.rvResults);
        users = new ArrayList<>();
        rvResult.setLayoutManager(new LinearLayoutManager(context));
        userAdapter = new UserAdapter(users, false);
        rvResult.setAdapter(userAdapter);
        tvError = (TextView) v.findViewById(R.id.tvError);
        try {
            search();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return v;
    }

    public static SearchFragment newInstance(String query) {

        Bundle args = new Bundle();
        args.putString("search_query", query);

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public void addItems(List<ParseUser> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ParseUser user= userList.get(i);
            users.add(0, user);
            userAdapter.notifyItemInserted(0);

        }
    }

    public void search() throws ParseException {
        searchQuery = getArguments().getString("search_query");
        /* This method uses exact search. However, fuzzy matching will be implemented later*/
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereMatches("name", searchQuery, "i");
        //query.whereMatches("name", search, "r");
        query.orderByAscending("name");
        query.setLimit(200);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null && userList.size() > 0) {
                    tvError.setVisibility(View.GONE);
                    users.clear();
                    addItems(userList);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
