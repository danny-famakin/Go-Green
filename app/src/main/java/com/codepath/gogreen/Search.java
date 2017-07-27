package com.codepath.gogreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by famakindaniel7 on 7/24/17.
 */



public class Search extends AppCompatActivity {
    String search;
    ArrayList<ParseUser> mUsers;
    RecyclerView rvResult;
    UserAdapter userAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = getIntent().getExtras().getString("search_users");
        setContentView(R.layout.search_activity);
        rvResult = (RecyclerView) findViewById(R.id.rvResults);
        mUsers = new ArrayList<>();
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(mUsers);
        rvResult.setAdapter(userAdapter);
        try {
            search();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addItems(List<ParseUser> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ParseUser users= userList.get(i);
            mUsers.add(0, users);
            userAdapter.notifyItemInserted(0);
        }
    }

    public void search() throws ParseException {
        /* This method uses exact search. However, fuzzy matching will be implemented later*/
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereMatches("name", search, "i");
        //query.whereMatches("name", search, "r");
        query.orderByAscending("totalPoints");
        query.setLimit(200);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Found ", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < users.size(); i++) {
                        ParseUser user= users.get(i);
                        mUsers.add(user);
                        userAdapter.notifyItemInserted(0);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                    builder.setMessage(e.getMessage())
                            .setTitle("User not found")
                            .setPositiveButton("Done", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
