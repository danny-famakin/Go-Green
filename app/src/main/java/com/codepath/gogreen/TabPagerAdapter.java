package com.codepath.gogreen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.gogreen.FeedActivity;
import com.codepath.gogreen.fragments.FeedFragment;
import com.codepath.gogreen.fragments.LeaderboardFragment;

/**
 * Created by famakindaniel7 on 7/13/17.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] {"Feed", "Leaderboard"};
    private Context context;
    public FeedFragment feedFragment;
    public LeaderboardFragment leaderboardFragment;

    public TabPagerAdapter(FragmentManager fm, FeedActivity feedActivity){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            feedFragment = getFeedInstance();
            return feedFragment;
        }else if (position == 1){
            leaderboardFragment = getBoardInstance();
            return leaderboardFragment;
        } else {
            return null;
        }
    }

    public FeedFragment getFeedInstance() {
        if (feedFragment == null) {
            feedFragment = new FeedFragment();
        }
        return feedFragment;
    }

    public LeaderboardFragment getBoardInstance() {
        if (leaderboardFragment == null) {
            leaderboardFragment = new LeaderboardFragment();
        }
        return leaderboardFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle (int position) {
        return tabTitles[position];
    }


}
