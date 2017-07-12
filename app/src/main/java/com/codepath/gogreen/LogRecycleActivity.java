package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;


public class LogRecycleActivity extends AppCompatActivity implements  View.OnClickListener {
    private int MAX_BOTTLES = 20;
    EditText etBottles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_recycle);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        etBottles = (EditText) findViewById(R.id.etBottles);

    }

    //Code for controlling user input
    //final int bottles = Integer.valueOf(bottleNum);
    //if (bottles > MAX_BOTTLES) {
    //etBottles.setError("You cannot go beyond 20 bottles");
    //}

    @Override
   public void onClick(View view) {

        if (isValid(etBottles, "Bottle number")) {
            Intent i = new Intent(this, RecycleActivity.class);
            String bottleNum = etBottles.getText().toString();
            i.putExtra("input", Integer.valueOf(bottleNum));
            setResult(RESULT_OK, i);
            finish();
        }
    }

    public boolean isValid(EditText editText, String description) {
        Log.d("text", editText.getText().toString());

        if (StringUtils.isBlank(editText.getText().toString())) {
            Log.d("text", "blank");
            editText.setError(description + " is required!");
            return false;
        } else {
            Log.d("text", "not blank");
            editText.setError(null);
            return true;
        }
    }

}
