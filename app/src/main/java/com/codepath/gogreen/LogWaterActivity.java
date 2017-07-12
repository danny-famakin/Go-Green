package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;


public class LogWaterActivity extends AppCompatActivity {

    EditText etTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_water);
        TextView tvShower = (TextView) findViewById(R.id.tvShower);
        etTime = (EditText) findViewById(R.id.etTime);
    }


    public void onSave(View view) {
        Intent i = new Intent();
        if (isValid(etTime, "Shower length")) {
            i.putExtra("time", Double.parseDouble(etTime.getText().toString()));
//            Log.d("time", String.valueOf(Double.parseDouble(etTime.getText().toString())));
            setResult(RESULT_OK, i);
            finish();
        }
    }

    // adapted from https://www.excella.com/insights/how-do-i-validate-an-android-form
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
