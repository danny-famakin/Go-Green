package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_main);

        context = this;
        Button btnTransit = (Button) findViewById(R.id.btnTransit);
        Button btnWater = (Button) findViewById(R.id.btnWater);
        Button btnBags = (Button) findViewById(R.id.btnBags);
        Button btnBottles = (Button) findViewById(R.id.btnBottles);

        btnTransit.setOnClickListener(this);
        btnWater.setOnClickListener(this);
        btnBags.setOnClickListener(this);
        btnBottles.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.btnTransit:
                Log.d("clicked", "transit");
                i = new Intent(context, TransitActivity.class);
                context.startActivity(i);
                break;

            case R.id.btnWater:
                Log.d("clicked", "water");
                i = new Intent(context, WaterActivity.class);
                context.startActivity(i);
                break;

            case R.id.btnBags:
                i = new Intent(context, ReuseActivity.class);
                context.startActivity(i);
                Log.d("clicked", "bags");
                break;

            case R.id.btnBottles:
                //Launch new Activity
                i = new Intent(this, RecycleActivity.class);
                startActivity(i);

                Log.d("clicked", "bottles");
                break;

        }
    }

}
