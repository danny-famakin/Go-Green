package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RecycleLog extends AppCompatActivity implements  View.OnClickListener {
    private int MAX_BOTTLES = 20;
    EditText etBottles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_log);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        etBottles = (EditText) findViewById(R.id.etBottles);
        String bottleNum = etBottles.getText().toString();
        if(TextUtils.isEmpty(bottleNum)) {
            etBottles.setError("Field cannot be empty");
            return;
        }
    }

    //Code for controlling user input
    //final int bottles = Integer.valueOf(bottleNum);
    //if (bottles > MAX_BOTTLES) {
    //etBottles.setError("You cannot go beyond 20 bottles");
    //}

    @Override
   public void onClick(View view) {
       Intent i = new Intent(this, RecycleActivity.class);
        String bottleNum = etBottles.getText().toString();
       i.putExtra("input", Integer.valueOf(bottleNum));
       setResult(RESULT_OK, i);
        finish();
    }
}
