package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.gogreen.fragments.ModalFragment;
import com.codepath.gogreen.fragments.TabPagerAdapter;
import com.codepath.gogreen.models.Action;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


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
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        PagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        ViewPager = (ViewPager) findViewById(R.id.container);
        ViewPager.setAdapter(PagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(ViewPager);

        // check if necessary to display login screen
        currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null)) {
            Log.d("loggedin", "true");
            loadData();
        } else {
            Log.d("loggedin", "false");
            ParseLoginBuilder builder = new ParseLoginBuilder(FeedActivity.this);
            startActivityForResult(builder.build(), 0);

        }
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
        Log.d("FeedActivity", String.valueOf(action.getMagnitude()) + " points awarded for " + action.getActionType());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == android.app.Activity.RESULT_OK) {
            loadData();
        } else {

        }
    }

    public void loadData() {
        currentUser = ParseUser.getCurrentUser();
        Log.d("User", "@" + currentUser.getString("profileImgUrl"));
        Log.d("User", "@" + currentUser.getString("name"));
    }
}
