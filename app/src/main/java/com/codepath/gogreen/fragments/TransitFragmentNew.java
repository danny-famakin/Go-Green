//package com.codepath.gogreen.fragments;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//import com.codepath.gogreen.R;
//import com.codepath.gogreen.models.Action;
//import com.parse.GetCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * Created by anyazhang on 7/13/17.
// */
//
//public class TransitFragment extends ModalFragment implements View.OnClickListener {
//    String vehicleType;
//    //TextView distance;
//    LayoutInflater inflater;
//    View v;
//    double[] pointValues = {5, 3, 1.5};
//    double newPoints;
//    MapFragment mapFragment;
//    private TextView distance;
//    Map<String, Double[]> resourceConstants = new HashMap<String, Double[]>();
//
//
//    //Toggle button to start tracking activity
//    ToggleButton btnTrack;
//
//
//    public static TransitFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        TransitFragment fragment = new TransitFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        resourceConstants.put("bus", new Double[] {0.2976, .0077, 0., 0.});
//        resourceConstants.put("subway", new Double[] {.7408, .0454, 0., 0.});
//        resourceConstants.put("train", new Double[] {0.7408, .0454, 0., 0.});
//        resourceConstants.put("bike", new Double[] {0.9590, .0856, 0., 0.});
//        resourceConstants.put("walk", new Double[] {0.9590, 0.0843, 0., 0.});
//
//
//        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(R.layout.activity_log_transit, null);
//
//            Spinner spVehicle = (Spinner) v.findViewById(R.id.spVehicle);
//            distance = (TextView) v.findViewById(R.id.tvDistCounter);
//
//            spVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    vehicleType = parent.getItemAtPosition(pos).toString();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    vehicleType = parent.getItemAtPosition(0).toString();
//                }
//            });
//
//            // Create an ArrayAdapter using the string array and a default spinner layout
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
//                    R.array.transportation_types, android.R.layout.simple_spinner_item);
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            // Apply the adapter to the spinner
//            spVehicle.setAdapter(adapter);
//
//            openModal(v);
//
//
//
//
//            btnTrack = (ToggleButton) v.findViewById(R.id.btnTrack);
//            btnTrack.setOnClickListener(this);
//
//
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mapFragment = new MapFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.flMap, mapFragment).commit();
//    }
//
//    double newDistance;
//    public void onSave() {
//        Intent i = new Intent();
//
//        if (distance != null) {
//            newDistance = Double.parseDouble(distance.getText().toString());
//            switch (vehicleType) {
//                case "bus":
//                case "subway":
//                case "train":
//                    updateData(0);
//                    break;
//                case "walking":
//                case "bike":
//                    updateData(1);
//                    break;
//                case "carpool":
//                    updateData(2);
//                    break;
//                default:
//                    Log.d("update", "failure");
//
//            }
//
//
//            modal.dismiss();
//        }
//        else {
//        }
//    }
//
//    private void updateData(int index) {
//        newPoints = (pointValues[index] * newDistance);
//
//        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                ParseUser currentUser = (ParseUser) object;
//                double points = currentUser.getInt("totalPoints");
//                points += newPoints;
//                currentUser.put("totalPoints", points);
//
//                Double[] resourceData = new Double[0];
//                JSONArray resourceJSON = currentUser.getJSONArray("resourceData");
//                Double[] vehicleConstants = resourceConstants.get(vehicleType);
//
//                for (int j = 0; j < resourceJSON.length(); j ++) {
//                    try {
//                        resourceData[j] = (Double) resourceJSON.get(j) + (vehicleConstants[j] * newDistance);
//                    } catch (JSONException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//                currentUser.put("resourceData", resourceData);
//
//                currentUser.saveInBackground();
//            }
//        });
//
//
////        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
////        query.whereEqualTo("fbId", currentUser.get("fbId"));
////        query.findInBackground(new FindCallback<ParseUser>() {
////            public void done(List<ParseUser> userList, ParseException e) {
////                if (e == null && userList.size() > 0) {
////                    ParseUser user =
////                    double points = userList.get(0).getInt("totalPoints");
////                    points += newPoints;
////                    currentUser.put("totalPoints", points);
////                    currentUser.saveInBackground();
////                    for (int i = 0; i <  )
////                } else if (e != null) {
////                    Log.e("points", "Error: " + e.getMessage());
////                }
////            }
////        });
//
//
//        // Add action to database
//        final Action action = new Action();
//        action.setUid(USER_ID);
//        action.setActionType("transit");
//        action.setSubType(vehicleType);
//        action.setMagnitude(newDistance);
//        action.setPoints(newPoints);
//
//        action.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                Toast.makeText(getActivity(), "Action logged", Toast.LENGTH_SHORT).show();
//                listener.updateFeed(action);
//            }
//        });
//    }
//
//    public void updateDistance(Double totalDistance) {
//        ((TextView) v.findViewById(R.id.tvDistCounter)).setText(String.format("%.2f", totalDistance));
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        if (btnTrack.isChecked()) {
//            Toast.makeText(getContext(), "Tracking", Toast.LENGTH_SHORT).show();
//            btnTrack.setText("pause");
//            btnTrack.setTextColor(getActivity().getResources().getColor(R.color.red));
//            mapFragment.beginTrack();
//        } else {
//            Toast.makeText(getContext(), "Stopped Tracking", Toast.LENGTH_SHORT).show();
//            btnTrack.setText("resume");
//            btnTrack.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
//            mapFragment.stopTrack();
//            //logDistance();
//        }
//    }
//}
