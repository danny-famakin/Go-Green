package com.codepath.gogreen.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.gogreen.FeedActivity;

/**
 * Created by famakindaniel7 on 7/13/17.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] {"Feed", "Leaderboard"};
    private Context context;
    FeedFragment feedFragment;
    LeaderboardFragment leaderboardFragment;
    public TabPagerAdapter(FragmentManager fm, FeedActivity feedActivity){
        super(fm);
        feedFragment = new FeedFragment();
        leaderboardFragment = new LeaderboardFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return feedFragment;
        }else if (position == 1){
            return leaderboardFragment;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle (int position) {
        return tabTitles[position];
    }
}
