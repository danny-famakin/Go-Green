package com.codepath.gogreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class RecycleActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE = 20;
    int total;
    TextView tvBottles;
    TextView tvCans;
    TextView tvPoints;
    double points;
    double materials[] = {0,0};
    double pointValues[] = {1,1};
    SharedPreferences recMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        getSupportActionBar().setTitle("Recycle");
        FloatingActionButton fabLog = (FloatingActionButton) findViewById(R.id.fabLog);
        fabLog.setOnClickListener(this);
        tvBottles = (TextView) findViewById(R.id.tvBottles);
        tvCans = (TextView) findViewById(R.id.tvCans);
        tvPoints = (TextView) findViewById(R.id.tvPoints);
        recMaterial = getSharedPreferences("recs", 0);
        double[] storedRecs = new double[] {getDouble(recMaterial, "material1", 0), getDouble(recMaterial,"material2", 0)};
        materials = storedRecs;
        double storedPoints = getDouble(recMaterial, "points", 0);
        points = storedPoints;

        tvBottles.setText(String.valueOf(materials[0]));
        tvCans.setText(String.valueOf(materials[1]));
        tvPoints.setText(String.valueOf(points));
    }
    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, LogRecycleActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                switch (data.getStringExtra("material")){
                    case "bottles":
                        updateNumber(data , 0,tvBottles);
                        break;
                    case "cans":
                        updateNumber(data, 1, tvCans);
                        break;
                }
            }
        }
            //total += data.getIntExtra("input", 20);
            //tvBottles.setText(String.valueOf(total));
        }

    public void updateNumber(Intent data, int index, TextView tv){
        /*tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) +*/
        double numMaterial = data.getIntExtra("input", 20);
        materials[index] += numMaterial;
        tv.setText(String.valueOf(materials[index]));
        points += (pointValues[index] * numMaterial);
        tvPoints.setText(String.valueOf(points));
    }
    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences.Editor editor = recMaterial.edit();
        for (int i = 0; i < 2; i++){
            putDouble(editor, "material", materials[i]);
        }
        putDouble(editor, "points", points);
        // Commit the edits!
        editor.commit();
    }
    double getDouble(final SharedPreferences prefs, final String key, final double defaultvalue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultvalue)));
    }
    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value){
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
}
