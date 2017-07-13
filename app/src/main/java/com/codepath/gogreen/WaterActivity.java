package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class WaterActivity extends AppCompatActivity implements View.OnClickListener  {
    Context context;
    static final int REQUEST_FAB = 20;
    TextView tvShowerTime;
    double totalShowerTime;
    double sampleSize;
    double points;
    static final double SHOWER_BASELINE = 10;
    static final int SECS_PER_MIN = 60;
    TextView tvPoints;
    SharedPreferences waterData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_water);
        ImageView ivShower = (ImageView) findViewById(R.id.ivShower);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);

        tvShowerTime = (TextView) findViewById(R.id.tvShowerTime);
        tvPoints = (TextView) findViewById(R.id.tvPoints);
        TextView tvWaterInfo = (TextView) findViewById(R.id.tvWaterInfo);

        // Stat sources:

        tvWaterInfo.setText(
                "Water statistics here"

        );

        waterData = getSharedPreferences("water", 0);
        double storedShowerTime = getDouble(waterData, "showerTime", 0);
        totalShowerTime = storedShowerTime;
        double storedSampleSize = getDouble(waterData, "sampleSize", 0);
        sampleSize = storedSampleSize;
        double storedPoints = getDouble(waterData, "points", 0);
        points = storedPoints;

        tvPoints.setText(String.valueOf(points));
        tvShowerTime.setText(stringifyTime(totalShowerTime / sampleSize));

    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.fab:
                i = new Intent(context, LogWaterActivity.class);
                startActivityForResult(i, REQUEST_FAB);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_FAB) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                double newTime = data.getDoubleExtra("time", 0);
                double avgTime = updateAvg(newTime);
                tvShowerTime.setText(stringifyTime(avgTime));

                // calculate points awarded
                double newPoints = 2 * (SHOWER_BASELINE - newTime);
                points += newPoints;
                tvPoints.setText(String.valueOf(points));

            }
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        SharedPreferences.Editor editor = waterData.edit();

        putDouble(editor, "showerTime", totalShowerTime);
        putDouble(editor, "points", points);
        putDouble(editor, "sampleSize", sampleSize);

        // Commit the edits!
        editor.commit();
    }

    private double updateAvg(Double newTime) {
        //        Log.d("newtime", String.valueOf(newTime));
        totalShowerTime += newTime;
//        Log.d("totaltime", String.valueOf(totalShowerTime));
        sampleSize += 1;
        double newAvg = (totalShowerTime) / sampleSize;
//        Log.d("avgtime", String.valueOf(newAvg));
        return newAvg;
    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
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
