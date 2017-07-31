package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
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
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    }
}
