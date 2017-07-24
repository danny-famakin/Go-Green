package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Action;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by melissaperez on 7/14/17.
 */

public class ReuseFragment extends ModalFragment {

    EditText bags;
    LayoutInflater inflater;
    View v;
    int total;
    int totalBagPoints;
    int newPoints;
    SharedPreferences reuse;



    public static ReuseFragment newInstance() {

        Bundle args = new Bundle();

        ReuseFragment fragment = new ReuseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_log_reuse, null);
        bags = (EditText) v.findViewById(R.id.etBags);
        reuse = this.getActivity().getSharedPreferences("reuse", 0);
        total = reuse.getInt("bagCount", 0);
        totalBagPoints = reuse.getInt("pointCount", 0);

        openModal(v);
    }

    public void onSave() {
        Intent i = new Intent();

        if(isValid(bags, "Number of bags reused")) {
            int newBags = Integer.valueOf(bags.getText().toString());
            total += newBags;
            newPoints = newBags * 10;
            totalBagPoints += newPoints;
            updatePoints();

            // save action in database
            final Action action = new Action();
            action.setUid(USER_ID);
            action.setActionType("reuse");
            action.setMagnitude(newBags);
            action.setPoints(newPoints);

            action.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(getActivity(), "Action logged", Toast.LENGTH_SHORT).show();
                    listener.updateFeed(action);
                }
            });




//            i.putExtra("vehicle", vehicleType);
//            i.putExtra("distance", Integer.parseInt(etDistance.getText().toString()));
//            setResult(RESULT_OK, i);
//            finish();
            modal.dismiss();
        }
        else {
        }
    }

    private void updatePoints() {
        final ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("fbId", currentUser.get("fbId"));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null && userList.size() > 0) {
                    double points = userList.get(0).getInt("totalPoints");
                    points += newPoints;
                    currentUser.put("totalPoints", points);
                    currentUser.saveInBackground();
                } else if (e != null) {
                    Log.e("points", "Error: " + e.getMessage());
                }
            }
        });

        SharedPreferences reuse = this.getActivity().getSharedPreferences("reuse", 0);
        SharedPreferences.Editor editor = reuse.edit();
        editor.putInt("bagCount",total);
        editor.putInt("pointCount", totalBagPoints);

        //Commit the edits
        editor.commit();


    }
}
