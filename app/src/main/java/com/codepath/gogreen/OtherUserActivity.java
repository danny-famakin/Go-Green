package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.view.View.GONE;
import static com.codepath.gogreen.R.id.myProfile;

/**
 * Created by famakindaniel7 on 7/25/17.
 */

public class OtherUserActivity extends AppCompatActivity {
    Context context;
    ImageView profileImage;
    TextView tvName, tvJoinDate, tvPoints;
    String screenName;
    String profImg;
    String Id;
    String points;
    ToggleButton addFriends;
    Button logOut;
    ParseUser currentUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        profileImage = (ImageView) findViewById(R.id.ivProfilePicDet);
        tvName = (TextView) findViewById(R.id.tvName);
        tvJoinDate = (TextView) findViewById(R.id.tvJoin);
        addFriends = (ToggleButton) findViewById(R.id.addFriends);
        tvPoints = (TextView) findViewById(R.id.tvPoints);


        screenName = getIntent().getStringExtra("screenName");
        profImg = getIntent().getStringExtra("profImage");
        Id = getIntent().getStringExtra("Id");
        points = getIntent().getStringExtra("points");

        tvPoints.setText(points);

        loadData();
        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user, ParseException e) {
                currentUser = ParseUser.getCurrentUser();

                JSONArray friendsList = currentUser.getJSONArray("friends");
                for (int i = 0; i < friendsList.length(); i++) {
                    try {
                        if (friendsList.get(i).equals(Id)){
                            addFriends.setText("Friends");
                        }
                        if (Id.equals(currentUser.get("fbId"))){
                            addFriends.setVisibility(GONE);
                            tvName.setText("Your Profile");
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        //Add friends
        addFriends.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                currentUser = ParseUser.getCurrentUser();
                JSONArray friendsList = currentUser.getJSONArray("friends");

                if (addFriends.isChecked()) {
                    if (friendIndex(friendsList) == -1) {
                        friendsList.put(Id);
                    }

                }  else if(!addFriends.isChecked()){
                    int i = friendIndex(friendsList);
                    if (i != -1) {
                        friendsList.remove(i);
                    }                }
                currentUser.put("friends", friendsList);
                currentUser.saveInBackground();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_other_profile, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == myProfile) {
            Intent profile = new Intent(OtherUserActivity.this, ProfileActivity.class);
            startActivity(profile);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void loadData() {
        Glide.with(context)
                .load(profImg)
                .placeholder(R.drawable.ic_placeholder)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(profileImage);
        tvName.setText(screenName);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM YYYY", Locale.US);
        Date joinDate = (Date)getIntent().getSerializableExtra("joinDate");
        if (joinDate != null) {
        String dateString = "Joined in " + sdf.format(joinDate);
        tvJoinDate.setText(dateString);
        }
    }


    public int friendIndex(JSONArray friendsList) {
        for (int i = 0; i < friendsList.length(); i++) {
            try {
                if (friendsList.get(i).equals(Id)) {
                    return i;
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return -1;
    }

        //SimpleDateFormat sdf = new SimpleDateFormat("MMMM YYYY", Locale.US);
        //Date joinDate = otherUser.getDate("joinDate");
        //if (joinDate != null) {
            //String dateString = "Joined in " + sdf.format(joinDate);
            //tvJoinDate.setText(dateString);
        //}

}

