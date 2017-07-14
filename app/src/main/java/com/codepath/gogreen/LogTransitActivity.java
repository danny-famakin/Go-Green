package com.codepath.gogreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public class LogTransitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String vehicleType;
    TextView tvDistance;
    EditText etDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_transit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner spVehicle = (Spinner) findViewById(R.id.spVehicle);
        tvDistance = (TextView) findViewById(R.id.tvShowerTime);
        etDistance = (EditText) findViewById(R.id.etTransDist);
        spVehicle.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transportation_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spVehicle.setAdapter(adapter);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        vehicleType = parent.getItemAtPosition(pos).toString();
        Log.d("vehicle", vehicleType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        vehicleType = parent.getItemAtPosition(0).toString();
        Log.d("vehicle", vehicleType);
    }

    public void onSave(View view) {
        Intent i = new Intent();
        if (isValid(etDistance, "Distance")) {
            i.putExtra("vehicle", vehicleType);
            i.putExtra("distance", Integer.parseInt(etDistance.getText().toString()));
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
