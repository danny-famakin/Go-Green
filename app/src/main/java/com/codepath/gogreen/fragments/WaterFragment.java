package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.codepath.gogreen.R;

/**
 * Created by anyazhang on 7/14/17.
 */

public class WaterFragment extends ModalFragment {
    EditText etTime;
    LayoutInflater inflater;
    View v;
    public static final int SHOWER_BASELINE = 10;
    public static final int SECS_PER_MIN = 60;
    double newTime;


    public static WaterFragment newInstance() {

        Bundle args = new Bundle();

        WaterFragment fragment = new WaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_log_water, null);

        etTime = (EditText) v.findViewById(R.id.etTime);


        openModal(v);
    }

    public void onSave() {
        if (isValid(etTime, "Time")) {
            updateData();

            modal.dismiss();
        }
        else {
        }
    }

    private void updateData() {
        // Get stored data
        SharedPreferences waterData = this.getActivity().getSharedPreferences("water", 0);
        double totalShowerTime = getDouble(waterData, "showerTime", 0);
        double sampleSize = getDouble(waterData, "sampleSize", 0);
        double points = getDouble(waterData, "points", 0);

        // update local copies of data
        double newPoints;
        newTime = Double.parseDouble(etTime.getText().toString());
        totalShowerTime += newTime;

        if (newTime < SHOWER_BASELINE) {
            newPoints = 2 * (SHOWER_BASELINE - newTime);}
        else {
            newPoints = 0;
        }

        points += newPoints;
        sampleSize += 1;
        Log.d("total points", String.valueOf(points));

        // push local changes to storage
        SharedPreferences.Editor editor = waterData.edit();
        putDouble(editor, "showerTime", totalShowerTime);
        putDouble(editor, "points", points);
        putDouble(editor, "sampleSize", sampleSize);
        editor.commit();

        // update feed
        listener.updateFeed("shower", newPoints);
    }


    public String stringifyTime(double timeDouble) {
        String timeStr = "";
        long minutes = (long) timeDouble;
        Log.d("minutes", String.valueOf(minutes));
        long seconds = (long) ((timeDouble - (double) minutes) * SECS_PER_MIN);
        timeStr = String.valueOf(minutes) + ":" + String.format("%02d", seconds);
        return timeStr;
    }

}
