package com.codepath.gogreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfilePic;
    Context context;
    ParseUser currentUser;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        ParseUser currentUser = ParseUser.getCurrentUser();
        String email = currentUser.getEmail();
       // currentUser = ParseUser.getCurrentUser();
        //String imageURL = currentUser.getProfileImgUrl();

//        ParseQuery<User> query= ParseQuery.getQuery("User");
//        query.getInBackground("xVIrtIGYW7", new GetCallback<User>() {
//
//                    @Override
//                    public void done(User object, ParseException e) {
//                        if (e == null) {
//                            imageURL = object.getProfileImgUrl();
//
//                        } else {
//                            Log.d("score", "Error: " + e.getMessage());
//                        }
//                    }
//
//                });

//        ParseQuery<User> query = ParseQuery.getQuery("User");
//        query.whereEqualTo("_id","xVIrtIGYW7");
//        query.findInBackground(new FindCallback<User>() {
//            @Override
//            public void done(List<User> userList, ParseException e) {
//
//                if (e == null) {
//                            imageURL = us.getProfileImgUrl();
//
//                        } else {
//                            Log.d("score", "Error: " + e.getMessage());
//                        }
//                else {
//                    Log.d("score", "Error: " + e.getMessage());
//                    // Alert.alertOneBtn(getActivity(),"Something went wrong!");
//                }
//            }
//        });

//        ParseQuery<User> query = ParseQuery.getQuery("User");
//        query.whereEqualTo("name", "Melissa Perez");
//        query.findInBackground(new FindCallback<User>() {
//            public void done(List<User> profileImgUrl, ParseException e) {
//                if (e == null) {
//                    Log.d("score", "Retrieved " + profileImgUrl.size() + " scores");
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
//        query.whereEqualTo("email", email);
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (object == null) {
//                    Log.d("score", "The getFirst request failed.");
//                } else {
//                    Log.d("score", "Retrieved the object.");
//
//                }
//            }
//        });



        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        Glide.with(context)
                .load(imageURL)
                .into(ivProfilePic);
    }
}
