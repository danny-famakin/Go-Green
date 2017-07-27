package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Action;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;

import static com.codepath.gogreen.R.array.materials;

/**
 * Created by famakindaniel7 on 7/14/17.
 */

public class RecycleFragment extends ModalFragment {
    String materialType;
    EditText etNumber;
    LayoutInflater inflater;
    View view;
    double pointValues[] = {1, 1, 0.1};
    double newPoints;
    int INT_MIN = 1;
    int INT_MAX = 200;
    Map<String, Double[]> recycleConstants = new HashMap<>();


    public static RecycleFragment newInstance() {
        Bundle args = new Bundle();
        RecycleFragment fragment = new RecycleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recycleConstants.put("bottle", new Double[] {0.1012, .0292, 0.36, 0.});
        recycleConstants.put("can", new Double[] {0.4270, .6512, 62.7, 0.});
        recycleConstants.put("paper", new Double[] {0.0877, .0019, 0., 0.03});

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_log_recycle, null);
        Spinner spMaterial = (Spinner) view.findViewById(R.id.spMaterial);
        etNumber = (EditText) view.findViewById(R.id.etItems);

        spMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                materialType = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                materialType = parent.getItemAtPosition(0).toString();
            }
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                materials, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spMaterial.setAdapter(adapter);
        openModal(view);
    }

    double newMaterials;

    public void onSave() {
        Intent i = new Intent();
        if (isValid(etNumber, INT_MIN, INT_MAX)) {
            newMaterials = Integer.parseInt(etNumber.getText().toString());
            switch (materialType) {
                case "bottles":
                    updateData(0, "bottle");
                    break;
                case "cans":
                    updateData(1, "can");
                    break;
                case "sheets of paper":
                    updateData(2, "paper");
                    break;
            }
            modal.dismiss();
        } else {
        }
    }

    public void updateData(int index, String materialType) {
        /*tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) +*/
        //double numMaterial = data.getIntExtra("input", 20);
        //
        //tv.setText(String.valueOf(materials[index]));
        //
        //tvPoints.setText(String.valueOf(points));

        newPoints = pointValues[index] * newMaterials;

        // save action in database
        final Action action = new Action();
        action.setUid(USER_ID);
        action.setActionType("recycle");
        action.setSubType(materialType);
        action.setMagnitude(newMaterials);
        action.setPoints(newPoints);


        action.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            Log.d("time", action.getCreatedAt().toString());
            Toast.makeText(getActivity(), "Action logged", Toast.LENGTH_SHORT).show();
            listener.updateFeed(action);
            }
        });

        updateResources(newPoints, newMaterials, recycleConstants.get(materialType));

    }

}
