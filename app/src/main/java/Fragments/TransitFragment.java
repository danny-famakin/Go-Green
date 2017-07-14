package Fragments;

import android.content.Context;
import android.content.Intent;
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
                Log.d("vehicle", vehicleType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicleType = parent.getItemAtPosition(0).toString();
                Log.d("vehicle", vehicleType);
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


    public void onSave() {
        Intent i = new Intent();

        if (isValid(etDistance, "Distance")) {
            Log.d("transit", "save");
//            i.putExtra("vehicle", vehicleType);
//            i.putExtra("distance", Integer.parseInt(etDistance.getText().toString()));
//            setResult(RESULT_OK, i);
//            finish();
            modal.dismiss();
        }
        else {
        }
    }



}
