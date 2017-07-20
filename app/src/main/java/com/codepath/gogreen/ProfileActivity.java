package com.codepath.gogreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfilePic;
    Context context;
    ParseUser currentUser;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isLoggedIn()){
            ParseLoginBuilder builder = new ParseLoginBuilder(ProfileActivity.this);
            startActivityForResult(builder.build(), 0);
        }

        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        tvName = (TextView) findViewById(R.id.tvName);

        loadData();

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            showProfileLoggedIn();
        } else {
            showProfileLoggedOut();
        }
    }


    private void showProfileLoggedIn() {
        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
        Log.d("profile", currentUser.getString("name"));
        Log.d("profile", currentUser.toString());

//        titleTextView.setText(R.string.profile_title_logged_in);
//        emailTextView.setText(currentUser.getEmail());
//        String fullName = currentUser.getString("name");
//        if (fullName != null) {
//            nameTextView.setText(fullName);
//        }
//        loginOrLogoutButton.setText(R.string.profile_logout_button_label);
    }

    private void showProfileLoggedOut() {
        Log.d("profile", "logged out");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == android.app.Activity.RESULT_OK) {
            loadData();
        } else {

        }
    }

    public void loadData() {
        currentUser = ParseUser.getCurrentUser();
        Log.d("User", "@" + currentUser.getString("profileImgUrl"));
        Log.d("User", "@" + currentUser.getString("name"));

        Glide.with(context)
                .load(currentUser.getString("profileImgUrl"))
                .placeholder(R.drawable.ic_placeholder)
                .into(ivProfilePic);

        tvName.setText(currentUser.getString("name"));
    }
}
