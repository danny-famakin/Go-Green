package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
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
    static final int SECS_PER_MIN = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_water);
        ImageView ivShower = (ImageView) findViewById(R.id.ivShower);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);

        tvShowerTime = (TextView) findViewById(R.id.tvShowerTime);

        TextView tvWaterInfo = (TextView) findViewById(R.id.tvWaterInfo);

        // Stat sources:

        tvWaterInfo.setText(
                "Water statistics here"

        );


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
                String timeStr = "";
                double avgTime = updateAvg(data);
                long minutes = (long) avgTime;
                Log.d("minutes", String.valueOf(minutes));
                long seconds = (long) ((avgTime - (double) minutes) * SECS_PER_MIN);
                timeStr = String.valueOf(minutes) + ":" + String.format("%02d", seconds);
                tvShowerTime.setText(timeStr);

            }
        }
    }

    private double updateAvg(Intent data) {
        double newTime = data.getDoubleExtra("time", 0);
//        Log.d("newtime", String.valueOf(newTime));
        totalShowerTime += newTime;
//        Log.d("totaltime", String.valueOf(totalShowerTime));
        sampleSize += 1;
        double newAvg = (totalShowerTime) / sampleSize;
//        Log.d("avgtime", String.valueOf(newAvg));
        return newAvg;
    }
}
