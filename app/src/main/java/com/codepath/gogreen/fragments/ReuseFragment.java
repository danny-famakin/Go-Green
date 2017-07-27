package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Action;
import com.parse.ParseException;
import com.parse.SaveCallback;

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
    int INT_MIN = 1;
    int INT_MAX = 10;
    Double[] bagConstants = new Double[] {0.0794, 0.0124, 1.2, 0.};



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

        openModal(v);
    }

    public void onSave() {
        Intent i = new Intent();

        if(isValid(bags, INT_MIN, INT_MAX)) {
            int newBags = Integer.valueOf(bags.getText().toString());
            total += newBags;
            newPoints = newBags * 10;
            totalBagPoints += newPoints;
            updateResources(newPoints, newBags, bagConstants);

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


}
