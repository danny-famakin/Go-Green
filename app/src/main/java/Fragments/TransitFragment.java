package Fragments;

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

/**
 * Created by anyazhang on 7/13/17.
 */

public class TransitFragment extends ModalFragment {
    String vehicleType;
    EditText etDistance;
    LayoutInflater inflater;
    View v;
    double[] pointValues = {5, 3, 1.5};


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
        points += (pointValues[index] * newDistance);

        Log.d("total points", String.valueOf(points));

        // push local changes to storage
        SharedPreferences.Editor editor = transitData.edit();

        for (int i = 0; i < 3; i ++) {
            putDouble(editor, "dist" + i, distances[i]);
        }
        putDouble(editor, "points", points);

        // Commit the edits!
        editor.commit();
    }

}
