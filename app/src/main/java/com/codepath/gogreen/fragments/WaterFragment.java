package com.codepath.gogreen.fragments;

import android.content.Context;
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
import com.parse.ParseException;
import com.parse.SaveCallback;

/**
 * Created by anyazhang on 7/14/17.
 */

public class WaterFragment extends ModalFragment {
   // EditText etTime;
    LayoutInflater inflater;
    View v;
    public static final double SHOWER_BASELINE = 8.2;
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
    Double[] waterConstants = new Double[] {0.1499, 0.0102, 9.46, 0.};

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

        showering = true;
        tvTimer = (TextView) v.findViewById(R.id.tvTimer);
        tvErrorMsg = (TextView) v.findViewById(R.id.tvErrorMsg);
        btStartPause = (Button) v.findViewById(R.id.btStartPause);
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

        //newTime = Double.parseDouble(etTime.getText().toString());
        newTime = minutes + (((double) seconds) / 60.0);
        Log.d("newTime", String.valueOf(newTime));

        double savedTime;
        if (newTime < SHOWER_BASELINE) {
            savedTime = (SHOWER_BASELINE - newTime);
        }
        else {
            savedTime = 0;
        }
        newPoints = 2 * savedTime;

        updateResources("water", null, newPoints, savedTime, waterConstants, newTime);



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
