package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class RecycleActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE = 20;

    TextView tvBottles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        getSupportActionBar().setTitle("Recycle");
        //getActionBar().setIcon();
        FloatingActionButton fabLog = (FloatingActionButton) findViewById(R.id.fabLog);
        fabLog.setOnClickListener(this);
        tvBottles = (TextView) findViewById(R.id.tvBottles);
    }
    //@Override
    public void onClick(View view) {
        Intent i = new Intent(this, RecycleLog.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onactivityresult", "starting");
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Log.d("onactivityresult", "request received");
            String output = data.getStringExtra("input");
            tvBottles.setText(output);
        }
    }
}
