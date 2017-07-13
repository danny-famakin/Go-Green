package com.codepath.gogreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ReuseActivity extends AppCompatActivity {

    int REQUEST = 1;
    TextView totalBags;
    TextView pointsEarned;
    int total;
    int totalBagPoints;
    SharedPreferences storedBags;
    SharedPreferences storedPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuse);

        // Creates fab and when clicked moves to LogReuseActivity Activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReuseActivity.this, LogReuseActivity.class);
                startActivityForResult(i, REQUEST);
            }
        });

        storedBags = getSharedPreferences("bags", 0);
        storedPoints = getSharedPreferences("points", 0);
        total = storedBags.getInt("bagCount", 0);
        totalBagPoints = storedPoints.getInt("pointCount", 0);
        totalBags = (TextView) findViewById(R.id.totalBags);
        pointsEarned = (TextView) findViewById(R.id.pointsEarned);
        totalBags.setText(String.valueOf(total));
        pointsEarned.setText(String.valueOf(totalBagPoints));

    }
        // Receives results from LogReuseActivity
        // Changes the value from the amount of total bags
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent i) {
            Log.d("request", String.valueOf(requestCode));
            Log.d("result", String.valueOf(resultCode));
            if (requestCode == REQUEST) {
                if (resultCode == RESULT_OK) {
                    Log.d("result", "received");
                    total += i.getIntExtra("bags", 1);
                    totalBagPoints += i.getIntExtra("points", 1);
                    totalBags.setText(String.valueOf(total));
                    pointsEarned.setText(String.valueOf(totalBagPoints));

                }
            }
        }

        @Override
        protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences storedBags = getSharedPreferences("bags", 0);
        SharedPreferences.Editor editor = storedBags.edit();
        editor.putInt("bagCount",total);
        //Commit the edits
        editor.commit();

        SharedPreferences storedPoints = getSharedPreferences("points", 0);
        SharedPreferences.Editor pointsEditor = storedPoints.edit();
        pointsEditor.putInt("pointCount", totalBagPoints);

        // Commit the edits!
        pointsEditor.commit();
    }
        //http://oceancrusaders.org/plastic-crusades/plastic-statistics/
        //One of the biggest issues in our environment is plastic marine debris.
        //Every year around 100,000 marine creatures year because of it.
        //Because plastic bags take so long to decompose - 10 to 1000 years - it is ideal to start reusing it.



}
