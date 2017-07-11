package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ReuseActivity extends AppCompatActivity {

    int REQUEST = 1;
    TextView totalBags;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuse);

        // Creates fab and when clicked moves to PointsCounter Activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReuseActivity.this, PointsCounter.class);
                startActivityForResult(i, REQUEST);
            }
        });
    }
        // Receives results from PointsCounter
        // Changes the value from the amount of total bags
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent i) {
            Log.d("request", String.valueOf(requestCode));
            Log.d("result", String.valueOf(resultCode));
            if (requestCode == REQUEST) {
                if (resultCode == RESULT_OK) {
                    Log.d("result", "received");
                    totalBags = (TextView) findViewById(R.id.totalBags);
                    total += i.getIntExtra("bags", 1);
                    totalBags.setText(String.valueOf(total));
                }
            }
        }

        //http://oceancrusaders.org/plastic-crusades/plastic-statistics/
        //One of the biggest issues in our environment is plastic marine debris.
        //Every year around 100,000 marine creatures year because of it.
        //Because plastic bags take so long to decompose - 10 to 1000 years - it is ideal to start reusing it.



}
