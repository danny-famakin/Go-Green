package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Action;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by anyazhang on 7/14/17.
 */

public class WaterFragment extends ModalFragment {
   // EditText etTime;
    LayoutInflater inflater;
    View v;
    public static final int SHOWER_BASELINE = 10;
    public static final int SECS_PER_MIN = 60;
    double newTime;
    TextView tvTimer;
    Button btStartPause;
    TextView tvErrorMsg;
    long millisecond;
    long start;
    long timeBuff;
    long updateTime;
   // Handler handler;
    int seconds;
    int minutes;
    int milliseconds;
    boolean showering;
    double newPoints;
    int MIN_SECS = 30;

    public static WaterFragment newInstance() {

        Bundle args = new Bundle();

        WaterFragment fragment = new WaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_log_water, null);

     //   etTime = (EditText) v.findViewById(R.id.etTime);
        showering = true;
        tvTimer = (TextView) v.findViewById(R.id.tvTimer);
        tvErrorMsg = (TextView) v.findViewById(R.id.tvErrorMsg);
        btStartPause = (Button) v.findViewById(R.id.btStartPause);
        btStartPause.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.offWhite), PorterDuff.Mode.MULTIPLY);
        btStartPause.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));

//        handler = new Handler() {
//            @Override
//            public void publish(LogRecord record) {
//
//            }
//
//            @Override
//            public void flush() {
//
//            }
//
//            @Override
//            public void close() throws SecurityException {
//
//            }
//        };



        openModal(v);

        btStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showering) {
                    start = SystemClock.uptimeMillis();
                    v.postDelayed(runnable, 0);
                    showering = false;
                    btStartPause.setText("Pause");
                    btStartPause.setTextColor(getActivity().getResources().getColor(R.color.red));


                }
                else {
                    timeBuff += millisecond;
                    v.removeCallbacks(runnable);
                    showering = true;
                    btStartPause.setText("Resume");
                    btStartPause.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));


//                    btStartPause.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                }

            }
        });


    }

    public Runnable runnable = new Runnable() {

        public void run() {

            millisecond = SystemClock.uptimeMillis() - start;

            updateTime = timeBuff + millisecond;

            seconds = (int) (updateTime / 1000);

            minutes = seconds / 60;

            seconds = seconds % 60;

            milliseconds = (int) (updateTime % 1000);

            tvTimer.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));

            v.postDelayed(this,0);
        }

    };

    public boolean isValid() {
        if (60 * minutes + seconds > MIN_SECS) {
            tvErrorMsg.setVisibility(View.GONE);
            return true;
        }
        tvErrorMsg.setVisibility(View.VISIBLE);
        return false;
    }

    public void onSave() {
        if (isValid()) {
            updateData();

            modal.dismiss();
        }
    }

    private void updateData() {
        // Get stored data




        SharedPreferences waterData = this.getActivity().getSharedPreferences("water", 0);
        double totalShowerTime = getDouble(waterData, "showerTime", 0);
        double sampleSize = getDouble(waterData, "sampleSize", 0);
        double points = getDouble(waterData, "points", 0);

        // update local copies of data

        //newTime = Double.parseDouble(etTime.getText().toString());
        newTime = minutes + (((double) seconds) / 60.0);
        Log.d("newTime", String.valueOf(newTime));
        totalShowerTime += newTime;

        if (newTime < SHOWER_BASELINE) {
            newPoints = 2 * (SHOWER_BASELINE - newTime);
        }
        else {
            newPoints = 0;
        }

        final ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("fbId", currentUser.get("fbId"));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null && userList.size() > 0) {
                    double points = userList.get(0).getInt("totalPoints");
                    points += newPoints;
                    currentUser.put("totalPoints", points);
                    currentUser.saveInBackground();
                } else if (e != null) {
                    Log.e("points", "Error: " + e.getMessage());
                }
            }
        });

        points += newPoints;
        sampleSize += 1;

        // push local changes to storage
        SharedPreferences.Editor editor = waterData.edit();
        putDouble(editor, "showerTime", totalShowerTime);
        putDouble(editor, "points", points);
        putDouble(editor, "sampleSize", sampleSize);
        editor.commit();



        // save action in database
        final Action action = new Action();
        action.setUid(USER_ID);
        action.setActionType("water");
        action.setMagnitude(newTime);
        action.setPoints(newPoints);

        action.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getActivity(), "Action logged", Toast.LENGTH_SHORT).show();
                listener.updateFeed(action);
            }
        });



    }


    public String stringifyTime(double timeDouble) {
        String timeStr = "";
        long minutes = (long) timeDouble;
        long seconds = (long) ((timeDouble - (double) minutes) * SECS_PER_MIN);
        timeStr = String.valueOf(minutes) + ":" + String.format("%02d", seconds);
        return timeStr;
    }

}
