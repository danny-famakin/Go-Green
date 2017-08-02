package com.codepath.gogreen;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    FloatingActionMenu actionMenu;
    SubActionButton button1;
    SubActionButton button2;
    SubActionButton button3;
    SubActionButton button4;


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
            loadFeed();
            Log.d("loadfeed", "load");
        }

        else {
            ParseLoginBuilder builder = new ParseLoginBuilder(FeedActivity.this);
            startActivityForResult(builder.build(), 0);

        }



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// repeat many times:

        button1 = createSubActionButton(R.drawable.ic_transit2);
        button2 = createSubActionButton(R.drawable.ic_water2);
        button3 = createSubActionButton(R.drawable.ic_bag3);
        button4 = createSubActionButton(R.drawable.ic_can2);



        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(fab)
                .setRadius(400)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitFragment transitFragment = TransitFragment.newInstance();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, transitFragment,"TAG_FRAGMENT");
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
                ft.replace(R.id.flContainer, waterFragment, "TAG_FRAGMENT");
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
                ft.replace(R.id.flContainer, reuseFragment, "TAG_FRAGMENT");
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
                ft.replace(R.id.flContainer, recycleFragment, "TAG_FRAGMENT");
                // commit
                ft.commit();
            }
        });

        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                Log.d("menu", "opened");
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fab.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 135);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                Log.d("menu", "closed");

                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fab.setRotation(135);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
            }

        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (actionMenu.isOpen()) {
//set close when touch outside it
            Rect rect1 = new Rect();
            button1.getGlobalVisibleRect(rect1);
            Rect rect2 = new Rect();
            button2.getGlobalVisibleRect(rect2);
            Rect rect3 = new Rect();
            button3.getGlobalVisibleRect(rect3);
            Rect rect4 = new Rect();
            button4.getGlobalVisibleRect(rect4);
            if (!(rect3.contains((int) ev.getX(), (int) ev.getY())
                    || rect2.contains((int) ev.getX(), (int) ev.getY())
                    || rect1.contains((int) ev.getX(), (int) ev.getY())
                    || rect4.contains((int) ev.getX(), (int) ev.getY()))) {
                actionMenu.close(true);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Intent i = new Intent(context, SearchActivity.class);
                i.putExtra("search_users", query);
                context.startActivity(i);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

        Log.d("FeedActivity", String.valueOf(action.getMagnitude()) + " points awarded for " + action.getActionType());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == android.app.Activity.RESULT_OK) {
            loadFeed();
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
        Log.d("loadfeed", "loaded");

    }
}

