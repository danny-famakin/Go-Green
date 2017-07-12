package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RecycleLog extends AppCompatActivity implements  View.OnClickListener {
    private int MAX_BOTTLES = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_log);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    //Code for controlling user input
    //if(TextUtils.isEmpty(bottleNum)) {
    //etBottles.setError("Field cannot be empty");
    //return;
    //}
    //final int bottles = Integer.valueOf(bottleNum);

    //if (bottles > MAX_BOTTLES) {
    //etBottles.setError("You cannot go beyond 20 bottles");
    //}

    @Override
   public void onClick(View view) {
       Intent i = new Intent(this, RecycleActivity.class);
        EditText etBottles = (EditText) findViewById(R.id.etBottles);
        String bottleNum = etBottles.getText().toString();
       i.putExtra("input", etBottles.getText().toString());
       setResult(RESULT_OK, i);
        finish();
    }
}
