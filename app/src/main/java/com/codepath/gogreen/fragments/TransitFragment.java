package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Action;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Date;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by anyazhang on 7/13/17.
 */

public class TransitFragment extends ModalFragment {
    String vehicleType;
    EditText etDistance;
    LayoutInflater inflater;
    View v;
    double[] pointValues = {5, 3, 1.5};
    public int USER_ID = 0;


    public static TransitFragment newInstance() {

        Bundle args = new Bundle();

        TransitFragment fragment = new TransitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_log_transit, null);

        Spinner spVehicle = (Spinner) v.findViewById(R.id.spVehicle);
        etDistance = (EditText) v.findViewById(R.id.etDistance);

        spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                vehicleType = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicleType = parent.getItemAtPosition(0).toString();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
        R.array.transportation_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spVehicle.setAdapter(adapter);

        openModal(v);
    }

    double newDistance;
    public void onSave() {
        Intent i = new Intent();

        if (isValid(etDistance, "Distance")) {
            newDistance = Integer.parseInt(etDistance.getText().toString());
            switch (vehicleType) {
                case "bus":
                case "subway":
                case "train":
                    updateData(0);
                    break;
                case "walking":
                case "bike":
                    updateData(1);
                    break;
                case "carpool":
                    updateData(2);
                    break;
                default:
                    Log.d("update", "failure");

            }
            listener.updateFeed(vehicleType, newDistance);

//            i.putExtra("vehicle", vehicleType);
//            i.putExtra("distance", Integer.parseInt(etDistance.getText().toString()));
//            setResult(RESULT_OK, i);
//            finish();
            modal.dismiss();
        }
        else {
        }
    }

    private void updateData(int index) {
        // Get stored data
        SharedPreferences transitData = this.getActivity().getSharedPreferences("transit", 0);
        double[] distances = new double[] {getDouble(transitData, "dist0", 0), getDouble(transitData, "dist1", 0),getDouble(transitData, "dist2", 0)};
        double points = getDouble(transitData, "points", 0);

       // update local copies of data
        distances[index] += newDistance;
        double newPoints = (pointValues[index] * newDistance);
        points += newPoints;

        Log.d("total points", String.valueOf(points));

        // push local changes to storage
        SharedPreferences.Editor editor = transitData.edit();

        for (int i = 0; i < 3; i ++) {
            putDouble(editor, "dist" + i, distances[i]);
        }
        putDouble(editor, "points", points);

        // Commit the edits!
        editor.commit();

        // Add action to database
        final Action action = new Action();
        action.put("uid", USER_ID);
        action.put("actionType", "transit");
        action.put("subType", vehicleType);
        action.put("magnitude", newDistance);
        action.put("points", newPoints);
//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
//        String date = df.format(Calendar.getInstance().getTime());

//        Log.d("objectID", action.getObjectId());
//        Log.d("createdAt", String.valueOf(action.getCreatedAt()));
        action.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                String actionId = action.getObjectId();  //Save objectID that was just created
                Date createdAt = action.getCreatedAt();
                Log.d(TAG, "objId:" + actionId);
                Log.d(TAG, "createdAt:" + String.valueOf(createdAt));

            }
        });

    }

}
