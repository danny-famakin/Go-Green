package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.gogreen.fragments.ModalFragment;
import com.codepath.gogreen.fragments.RecycleFragment;
import com.codepath.gogreen.fragments.ReuseFragment;
import com.codepath.gogreen.fragments.TabPagerAdapter;
import com.codepath.gogreen.fragments.TransitFragment;
import com.codepath.gogreen.fragments.WaterFragment;
import com.codepath.gogreen.models.Action;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FeedActivity extends AppCompatActivity implements ModalFragment.OnItemSelectedListener {

    TabPagerAdapter PagerAdapter;
    ViewPager ViewPager;
    public Context context;
    ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // check if necessary to display login screen
        currentUser = ParseUser.getCurrentUser();
        if((currentUser != null)){
            getFriends();
        }

        else {
            ParseLoginBuilder builder = new ParseLoginBuilder(FeedActivity.this);
            startActivityForResult(builder.build(), 0);

        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// repeat many times:

        SubActionButton button1 = createSubActionButton(R.drawable.ic_transit2);
        SubActionButton button2 = createSubActionButton(R.drawable.ic_water);
        SubActionButton button3 = createSubActionButton(R.drawable.ic_bag3);
        SubActionButton button4 = createSubActionButton(R.drawable.ic_can2);



        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(fab)
                .setRadius(500)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitFragment transitFragment = TransitFragment.newInstance();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, transitFragment);
                // commit
                ft.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaterFragment waterFragment = WaterFragment.newInstance();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, waterFragment);
                // commit
                ft.commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReuseFragment reuseFragment = ReuseFragment.newInstance();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, reuseFragment);
                // commit
                ft.commit();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecycleFragment recycleFragment = RecycleFragment.newInstance();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, recycleFragment);
                // commit
                ft.commit();
            }
        });

    }

    public void getFriends() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        ParseUser user = ParseUser.getCurrentUser();
                        try {
                            ArrayList<String> friends = new ArrayList<String>();
                            JSONArray jsonArrayFriends = response.getJSONObject().getJSONArray("data");
                            for (int i = 0; i < jsonArrayFriends.length(); i++) {
                                JSONObject friendlistObject = jsonArrayFriends.getJSONObject(i);
                                String friendListID = friendlistObject.getString("id");
                                friends.add(friendListID);
                            }
                            user.put("friends", friends);
                            user.saveInBackground();
                            loadFeed();
                            } catch(JSONException e){
                                e.printStackTrace();
                            }



                    }
                }
        ).executeAsync();
    }

    public SubActionButton createSubActionButton(int iconId) {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        int subActionButtonSize = 280;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);
        ImageView icon = new ImageView(this);
        Glide.with(context)
                .load(iconId)
                .into(icon);
        return itemBuilder.setContentView(icon).setLayoutParams(params).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.myProfile) {
            Intent profile = new Intent(FeedActivity.this, ProfileActivity.class);
            startActivity(profile);
            return true;

        }


        return super.onOptionsItemSelected(item);
    }

    public void updateFeed(Action action) {
        PagerAdapter.feedFragment.addAction(action);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == android.app.Activity.RESULT_OK) {
            getFriends();
        } else {

        }
    }


    public void loadFeed() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        PagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        ViewPager = (ViewPager) findViewById(R.id.container);
        ViewPager.setAdapter(PagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(ViewPager);
    }
}

