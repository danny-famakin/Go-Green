package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.codepath.gogreen.R.array.materials;

/**
 * Created by famakindaniel7 on 7/14/17.
 */

public class RecycleFragment extends ModalFragment {
    String materialType;
    EditText etNumber;
    LayoutInflater inflater;
    View view;
    double pointValues[] = {1,1};

    public static RecycleFragment newInstance() {
        Bundle args = new Bundle();
        RecycleFragment fragment = new RecycleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_log_recycle, null);
        Spinner spMaterial = (Spinner) view.findViewById(R.id.spMaterial);
        etNumber = (EditText) view.findViewById(R.id.etItems);

        spMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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
        Intent i = new Intent ();
        if (isValid(etNumber, "number")) {
            newMaterials = Integer.parseInt(etNumber.getText().toString());
            switch (materialType){
                case "bottles":
                    updateNumber(0);
                    break;
                case "cans":
                    updateNumber(1);
                    break;
            }
            modal.dismiss();
        }else{
        }
    }

    public void updateNumber(int index){
        /*tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString()) +*/
        //double numMaterial = data.getIntExtra("input", 20);
        //
        //tv.setText(String.valueOf(materials[index]));
        //
        //tvPoints.setText(String.valueOf(points));

        // get stored data
        SharedPreferences recMaterials = this.getActivity().getSharedPreferences("recs", 0);
        double[] materials = new double[] {getDouble(recMaterials, "material1", 0), getDouble(recMaterials, "material2", 0)};
        double points = getDouble(recMaterials, "points", 0);

        // update local copies of data
        materials[index] += newMaterials;
        double newPoints = pointValues[index] * newMaterials;
        points += newPoints;

        // push changes
        SharedPreferences.Editor editor = recMaterials.edit();
        for (int i = 0; i < 2; i++){
            putDouble(editor, "material", materials[i]);
        }
        putDouble(editor, "points", points);
        editor.commit();

        // save action in database
        final Action action = new Action();
        action.setUid(USER_ID);
        action.setActionType("recycle");
        action.setSubType(materialType.substring(0,materialType.length() - 1));
        action.setMagnitude(newMaterials);
        action.setPoints(newPoints);


        action.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getActivity(), "Action logged", Toast.LENGTH_SHORT).show();

            }
        });
        listener.updateFeed(action);

    }
}
