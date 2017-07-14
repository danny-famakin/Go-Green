package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.codepath.gogreen.R;

/**
 * Created by melissaperez on 7/14/17.
 */

public class ReuseFragment extends ModalFragment {

    EditText bags;
    LayoutInflater inflater;
    View v;
    int total;
    int totalBagPoints;
    SharedPreferences storedBags;
    SharedPreferences storedPoints;



    public static ReuseFragment newInstance() {

        Bundle args = new Bundle();

        ReuseFragment fragment = new ReuseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_log_reuse, null);
        bags = (EditText) v.findViewById(R.id.bags);
        storedBags = this.getActivity().getSharedPreferences("bags", 0);
        storedPoints = this.getActivity().getSharedPreferences("points", 0);
        total = storedBags.getInt("bagCount", 0);
        totalBagPoints = storedPoints.getInt("pointCount", 0);

        openModal(v);
    }

    public void onSave() {
        Intent i = new Intent();

        if(isValid(bags, "Number of bags reused")) {
            total += Integer.valueOf(bags.getText().toString());
            totalBagPoints = total * 10;
            updatePoints();

            Log.d("total", String.valueOf(total));
            Log.d("totalBagPoiints", String.valueOf(totalBagPoints));


//            i.putExtra("vehicle", vehicleType);
//            i.putExtra("distance", Integer.parseInt(etDistance.getText().toString()));
//            setResult(RESULT_OK, i);
//            finish();
            listener.updateFeed("bags", total);
            modal.dismiss();
        }
        else {
        }
    }

    private void updatePoints() {
        SharedPreferences storedBags = this.getActivity().getSharedPreferences("bags", 0);
        SharedPreferences.Editor editor = storedBags.edit();
        editor.putInt("bagCount",total);
        //Commit the edits
        editor.commit();

        SharedPreferences storedPoints = this.getActivity().getSharedPreferences("points", 0);
        SharedPreferences.Editor pointsEditor = storedPoints.edit();
        pointsEditor.putInt("pointCount", totalBagPoints);

        // Commit the edits!
        pointsEditor.commit();
    }
}
