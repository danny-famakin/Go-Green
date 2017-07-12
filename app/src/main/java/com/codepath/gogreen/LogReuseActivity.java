package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

public class LogReuseActivity extends AppCompatActivity {


    EditText bags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reuse);
        bags = (EditText) findViewById(R.id.bags);

    }

    //Sends result to Reuse Activity
    public void onSave(View view){
        if(isValid(bags, "Number of bags reused")) {
            Intent i = new Intent();
            i.putExtra("bags", Integer.valueOf(bags.getText().toString()));
            setResult(RESULT_OK, i);
            finish();
        }

    }

    // adapted from https://www.excella.com/insights/how-do-i-validate-an-android-form
    // Checks that there are no invalid options
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

