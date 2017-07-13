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

public class TransitActivity extends AppCompatActivity implements View.OnClickListener  {
    Context context;
    static final int REQUEST_FAB = 20;
    TextView tvTransDist;
    TextView tvBikeDist;
    TextView tvCarDist;
    TextView tvPoints;
    double[] distances = {0,0,0};
    double points;
    double[] pointValues = {5, 3, 1.5};
    SharedPreferences transitData;

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_transit);
        ImageView ivTransportation = (ImageView) findViewById(R.id.ivTransportation);
        ImageView ivBiking = (ImageView) findViewById(R.id.ivBiking);
        ImageView ivCarpooling = (ImageView) findViewById(R.id.ivCarpooling);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
        ivTransportation.setOnClickListener(this);
        ivBiking.setOnClickListener(this);
        ivCarpooling.setOnClickListener(this);

        tvTransDist = (TextView) findViewById(R.id.tvTransDist);
        tvBikeDist = (TextView) findViewById(R.id.tvBikeDist);
        tvCarDist = (TextView) findViewById(R.id.tvCarDist);
        tvPoints = (TextView) findViewById(R.id.tvPoints);



        TextView tvTransitInfo = (TextView) findViewById(R.id.tvTransitInfo);

        // Stat sources:
        // https://www.transit.dot.gov/regulations-and-guidance/environmental-programs/transit-environmental-sustainability/transit-role
        // http://www.sciencedirect.com/science/article/pii/S1877042812042139
        // http://www.peopleforbikes.org/statistics/category/environmental-statistics
//        tvTransitInfo.setText(
//            "Transportation annually consumes about 143.4 billion gallons of fuel and produces 27% of greenhouse gas emissions in the US. " +
//            "Alternatives to personal vehicles can greatly reduce this amount. " +
//                    "Statistics show that " +
//            "subways and metros produce 76% and buses 33% lower greenhouse gas emissions per passenger mile " +
//            "than single-occupancy vehicles. Similarly, a mere 25% increase in walking and biking trips " +
//            "could lead to a fuel savings of 6.3 billion gallons and a 4% decrease in total annual emissions."
//
//        );
        transitData = getSharedPreferences("transit", 0);
        double[] storedDistances = new double[] {getDouble(transitData, "dist0", 0), getDouble(transitData, "dist1", 0),getDouble(transitData, "dist2", 0)};
        distances = storedDistances;
        double storedPoints = getDouble(transitData, "points", 0);
        points = storedPoints;

        tvTransDist.setText(String.valueOf(distances[0]));
        tvBikeDist.setText(String.valueOf(distances[1]));
        tvCarDist.setText(String.valueOf(distances[2]));
        tvPoints.setText(String.valueOf(points));
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.fab:
                i = new Intent(context, LogTransitActivity.class);
                startActivityForResult(i, REQUEST_FAB);


//            case R.id.ivTransportation:
//            i = new Intent(context, )
//
//            case R.id.ivBiking:
//
//            case R.id.ivCarpooling:


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_FAB) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                switch (data.getStringExtra("vehicle")) {
                    case "bus":
                    case "subway":
                    case "train":
                        updateDistance(data, 0, tvTransDist);
                        break;
                    case "walking":
                    case "bike":
                        updateDistance(data, 1, tvBikeDist);
                        break;
                    case "carpool":
                        updateDistance(data, 2, tvCarDist);
                        break;
                    default:
                        Log.d("update", "failure");

                }
            }
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        SharedPreferences.Editor editor = transitData.edit();

        for (int i = 0; i < 3; i ++) {
            putDouble(editor, "dist" + i, distances[i]);
        }
        putDouble(editor, "points", points);

        // Commit the edits!
        editor.commit();
    }

    private void updateDistance(Intent data, int index, TextView textView) {
        double newDistance = data.getIntExtra("distance", 0);
        distances[index] += newDistance;
        textView.setText(String.valueOf(distances[index]));
        points += (pointValues[index] * newDistance);
        tvPoints.setText(String.valueOf(points));
    }



}
