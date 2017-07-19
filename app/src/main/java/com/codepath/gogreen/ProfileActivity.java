package com.codepath.gogreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfilePic;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        Glide.with(context)
                .load("http://via.placeholder.com/300.png")
                .placeholder(R.drawable.ic_placeholder)
                .into(ivProfilePic);
    }
}
