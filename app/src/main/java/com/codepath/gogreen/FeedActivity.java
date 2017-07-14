package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import Fragments.ModalFragment;
import Fragments.RecycleFragment;
import Fragments.TabPagerAdapter;
import Fragments.TransitFragment;


public class FeedActivity extends AppCompatActivity implements ModalFragment.OnItemSelectedListener {

    private TabPagerAdapter PagerAdapter;
    private ViewPager ViewPager;
    Context context;

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

        SubActionButton button1 = createSubActionButton(R.drawable.ic_add_green3);
        SubActionButton button2 = createSubActionButton(R.drawable.ic_add);
        SubActionButton button3 = createSubActionButton(R.drawable.ic_add);
        SubActionButton button4 = createSubActionButton(R.mipmap.ic_rec);


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(fab)
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
                ModalFragment modalFragment = ModalFragment.newInstance("water");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, modalFragment);
                // commit
                ft.commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalFragment modalFragment = ModalFragment.newInstance("reuse");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, modalFragment);
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

    public SubActionButton createSubActionButton(int iconId) {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        int subActionButtonSize = 160;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);
        ImageView icon = new ImageView(this);
        Glide.with(context)
                .load(iconId)
                .into(icon);
        //icon.setImageResource(R.drawable.ic_add_circle_black_24dp);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.myProfile) {
            Intent profile = new Intent(FeedActivity.this, MyProfile.class);
            startActivity(profile);
            return true;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFeed(String actionType, double magnitude) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


        public void updateFeed(String actionType, double magnitude) {
            Log.d("FeedActivity", String.valueOf(magnitude) + " points awarded for " + actionType);

        }
    }
}
