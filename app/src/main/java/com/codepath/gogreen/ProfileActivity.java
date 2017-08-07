package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.codepath.gogreen.fragments.AboutFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.view.View.GONE;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfilePic;
    Context context;
    ParseUser currentUser;
    TextView tvName, resTrees, resEmissions, resWater, resFuel;
    View line;
    String imageURL;
    ToggleButton addFriend;
    TextView tvJoinDate, tvPoints;
    BarChart barChart;
    DateFormat fmt;
    int counter;
    Calendar cal;
    Calendar calendar;
    int i;
    double TREES_PER_POUND = 0.0085;

    double recyclePoints;
    double reusePoints;
    double waterPoints;
    double transitPoints;
    JSONObject resourceData;
    final List<BarEntry> entries = new ArrayList<BarEntry>();
    ArrayList labels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setLogo(R.drawable.toolbar_logo);
        context = this;

        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePicDet);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPoints = (TextView) findViewById(R.id.tvPoints);
        tvJoinDate = (TextView) findViewById(R.id.tvJoin);
        addFriend = (ToggleButton) findViewById(R.id.addFriends);
        resTrees = (TextView) findViewById(R.id.resTrees);
        resEmissions = (TextView) findViewById(R.id.resEmissions);
        resFuel = (TextView) findViewById(R.id.resFuel);
        resWater = (TextView) findViewById(R.id.resWater);
        barChart = (BarChart) findViewById(R.id.barChart);
        addFriend.setVisibility(GONE);
        currentUser = ParseUser.getCurrentUser();
        fmt = DateFormat.getDateInstance(DateFormat.LONG);
        labels = new ArrayList<>();
        resourceData = currentUser.getJSONObject("resourceData");
        String trees = null;
        String emissions = null;
        String fuel = null;
        String water = null;
        try {
            trees = String.format("%.2f", resourceData.getDouble("trees")  * TREES_PER_POUND);
            emissions = String.format("%.2f",resourceData.getDouble("emissions"));
            fuel = String.format("%.2f",resourceData.getDouble("fuel"));
            water = String.format("%.3f",resourceData.getDouble("water"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resTrees.setText("trees saved: " + trees );
        resEmissions.setText("emissions reduced: " + emissions + " pounds");
        resFuel.setText("fuel saved: " + fuel + " liters");
        resWater.setText("water saved: "+ water + " liters");

        cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        calendar = new GregorianCalendar();
        calendar.set(2017,6,29);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        long diff = cal.getTimeInMillis() - calendar.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);

        int daysCounter = (int) days;
        Log.d("daysCounter", String.valueOf(daysCounter));
            labels.add("");

        try {
            barChart(daysCounter + 1);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        // ensure user logged in
        if((currentUser != null)){
            Log.d("loggedin", "true");
            try {
                loadData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Log.d("loggedin", "false");
            ParseLoginBuilder builder = new ParseLoginBuilder(ProfileActivity.this);
            startActivityForResult(builder.build(), 0);

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_anchor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View menuItem = findViewById(R.id.overflow);
        PopupMenu popup = new PopupMenu(this, menuItem);
        popup.inflate(R.menu.menu_profile);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logOut:
                        ParseUser.logOut();
                        Intent i = new Intent(context, FeedActivity.class);
                        context.startActivity(i);
                        return  true;
                    case R.id.info:
                        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager()
                                .beginTransaction();
                        // make change
                        ft.replace(R.id.flContainer, new AboutFragment(), "TAG_FRAGMENT");
                        // commit
                        ft.commit();


                }
                return false;
            }
        });
        popup.show();
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == android.app.Activity.RESULT_OK) {
            try {
                loadData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    public void loadData() throws JSONException {
        currentUser = ParseUser.getCurrentUser();
        Glide.with(context)
                .load(currentUser.getString("profileImgUrl"))
                .placeholder(R.drawable.ic_placeholder)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(ivProfilePic);
        double pts = currentUser.getDouble("totalPoints");
        String points = String.format("%.1f", pts);
        tvPoints.setText(points);
        tvName.setText(currentUser.getString("name"));
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM YYYY", Locale.US);
        Date joinDate = currentUser.getDate("joinDate");
        if (joinDate != null) {
            String dateString = "Joined in " + sdf.format(joinDate);
            tvJoinDate.setText(dateString);
        }
    }

    public void barChart(final int counter) throws JSONException, java.text.ParseException {

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setDragEnabled(true);
        barChart.getDescription().setEnabled(false);
        barChart.setScaleEnabled(true);

        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(false);
        xl.setAxisMinimum(0f);



        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(0) {
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
        leftAxis.setSpaceTop(20f);
       // xl.setAxisMinimum((float) 0); // this replaces setStartAtZero(true
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
   //     barChart.moveViewTo();


        //data
        float groupSpace = 0.12f;
        float barSpace = 0f; // x2 dataset
        float barWidth = 0.42f; // x2 dataset




        for (i = 1; i < counter; i++) {
            // start of today


            cal.add(Calendar.DAY_OF_MONTH, -counter + i + 1 );
            Date initialDate = cal.getTime();

            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            String month_name = month_date.format(cal.getTime());

            SimpleDateFormat day_date = new SimpleDateFormat("dd");
            String day_number = day_date.format(cal.getTime());

            String addLabel = month_name + " " + day_number;

            labels.add(addLabel);

            xl.setValueFormatter(new IndexAxisValueFormatter(labels));

            Log.d("hopee", String.valueOf(initialDate));

            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date finalDate = cal.getTime();

           Log.d("desperatee", String.valueOf(finalDate));

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Action");
            query.whereEqualTo("uid", currentUser.getString("fbId"));
            query.whereGreaterThanOrEqualTo("createdAt", initialDate);
            query.whereLessThan("createdAt", finalDate);
            List<ParseObject> actionList;
            try {
                actionList = query.find();
                recyclePoints = 0;
                reusePoints = 0;
                waterPoints = 0;
                transitPoints = 0;
                for (int j = 0; j < actionList.size(); j++) {
                    switch (actionList.get(j).getString("actionType")) {
                        case "recycle":
                            recyclePoints += actionList.get(j).getDouble("points");
                            break;
                        case "reuse":
                            reusePoints += actionList.get(j).getDouble("points");
                            break;
                        case "water":
                            waterPoints += actionList.get(j).getDouble("points");
                            break;
                        case "transit":
                            transitPoints += actionList.get(j).getDouble("points");
                            break;
                    }
                }
                onLoadData(i);
                cal.add(Calendar.DAY_OF_MONTH, counter - i - 2);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        BarDataSet entry, water, trees, emissions;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            entry = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            entry.setValues(entries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {

            entry = new BarDataSet(entries, "");
            entry.setColors(getColors());
            entry.setStackLabels(new String[]{"Recycling", "Reusing", "Showers", "Transit"});
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(entry);
            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);
            barChart.setData(data);
            barChart.setVisibleXRangeMaximum(7);
            barChart.moveViewToX(entries.size() - 6);
        }

        barChart.getBarData().setBarWidth(barWidth);
        barChart.animateY(1500);
    }


    public void onLoadData(int i) {
        entries.add(new BarEntry(i, new float[] { (float) recyclePoints, (float) reusePoints, (float) waterPoints, (float) transitPoints }));
    }


    public int[] getColors() {
        int stacksize = 4;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        colors[0] = ResourcesCompat.getColor(getResources(), R.color.lightGreen, null);
        colors[1] = ResourcesCompat.getColor(getResources(), R.color.darkBlue, null);
        colors[2] = ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, null);
        colors[3] = ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null);

        return colors;
    }
}



