package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.view.View.GONE;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfilePic;
    Context context;
    ParseUser currentUser;
    TextView tvName;
    String imageURL;
    ToggleButton addFriend;
    TextView tvJoinDate;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePicDet);
        tvName = (TextView) findViewById(R.id.tvName);
        tvJoinDate = (TextView) findViewById(R.id.tvJoin);
        addFriend = (ToggleButton) findViewById(R.id.addFriends);
        barChart = (BarChart) findViewById(R.id.barChart);
        addFriend.setVisibility(GONE);
        currentUser = ParseUser.getCurrentUser();

        // ensure user logged in
        if((currentUser != null)){
            Log.d("loggedin", "true");
            loadData();
        }

        else {
            Log.d("loggedin", "false");
            ParseLoginBuilder builder = new ParseLoginBuilder(ProfileActivity.this);
            startActivityForResult(builder.build(), 0);

        }

        Button btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(context, FeedActivity.class);
                context.startActivity(i);

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == android.app.Activity.RESULT_OK) {
            loadData();
        } else {

        }
    }

    public void loadData() {
        currentUser = ParseUser.getCurrentUser();
        Glide.with(context)
                .load(currentUser.getString("profileImgUrl"))
                .placeholder(R.drawable.ic_placeholder)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(ivProfilePic);

        tvName.setText(currentUser.getString("name"));
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM YYYY", Locale.US);
        Date joinDate = currentUser.getDate("joinDate");
        if (joinDate != null) {
            String dateString = "Joined in " + sdf.format(joinDate);
            tvJoinDate.setText(dateString);
        }

//        ArrayList<BarEntry> BarEntry = new ArrayList<>();
//
//        BarEntry.add(new BarEntry(2f, 0));
//        BarEntry.add(new BarEntry(4f, 1));
//        BarEntry.add(new BarEntry(6f, 2));
//        BarEntry.add(new BarEntry(8f, 3));
//        BarEntry.add(new BarEntry(7f, 4));
//        BarEntry.add(new BarEntry(3f, 5));
//
//        BarDataSet dataSet = new BarDataSet(BarEntry, "Projects");
//
//        ArrayList<String> labels = new ArrayList<>();
//
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");
//
//        BarData data = new BarData(dataSet);
//
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        barChart.setData(data);

      //  barChart.setDescription("No of Projects");

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
       // barChart.setDescription("");
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        xl.setValueFormatter(new DefaultAxisValueFormatter(20) {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(20) {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true
        barChart.getAxisRight().setEnabled(false);

        //data
        float groupSpace = 0.1f;
        float barSpace = 0f; // x2 dataset
        float barWidth = 0.46f; // x2 dataset
        // (0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"

        int startYear = 1980;
        int endYear = 1985;


        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();


        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, 0.4f));
        }

        for (int i = startYear; i < endYear; i++) {
            yVals2.add(new BarEntry(i, 0.7f));
        }


        BarDataSet set1, set2;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet)barChart.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            // create 2 datasets with different types
            set1 = new BarDataSet(yVals1, "Company A");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "Company B");
            set2.setColor(Color.rgb(164, 228, 251));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
        }

        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinValue(startYear);
        barChart.groupBars(startYear, groupSpace, barSpace);
        barChart.invalidate();





    }
}
