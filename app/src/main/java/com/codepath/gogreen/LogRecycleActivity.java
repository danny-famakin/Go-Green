package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.commons.lang3.StringUtils;


public class LogRecycleActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, View.OnClickListener {
    //private int MAX_BOTTLES = 20;
    EditText etNumber;
    //Spinner spMaterial;
    String materialType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_recycle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        etNumber = (EditText) findViewById(R.id.etNumber);

        Spinner spMaterial = (Spinner) findViewById(R.id.spMaterial);
        spMaterial.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.materials, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spMaterial.setAdapter(adapter);
    }

    //Code for controlling user input
    //final int bottles = Integer.valueOf(bottleNum);
    //if (bottles > MAX_BOTTLES) {
    //etBottles.setError("You cannot go beyond 20 bottles");
    //}
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        materialType = parent.getItemAtPosition(pos).toString();
        Log.d("material", materialType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        materialType = parent.getItemAtPosition(0).toString();
        Log.d("material", materialType);
    }

    @Override
   public void onClick(View view) {
        Intent i = new Intent(this, RecycleActivity.class);
        if (isValid(etNumber, "number")) {
            String Num = etNumber.getText().toString();
            i.putExtra("material", materialType);
            i.putExtra("input", Integer.valueOf(Num));
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
