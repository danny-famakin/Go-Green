package com.codepath.gogreen.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.codepath.gogreen.CommentAdapter;
import com.codepath.gogreen.DividerItemDecoration;
import com.codepath.gogreen.R;
import com.codepath.gogreen.models.Action;
import com.codepath.gogreen.models.Comment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class DetailFragment extends Fragment {

    LayoutInflater inflater;
    View v;
    ImageView ivProfilePicDet;
    TextView tvAction;
    TextView tvPoints;
    TextView tvTimeStamp;
    TextView tvLikes;
    ImageButton ivFavorite;
    ImageButton ivReply;
    Context context;
    EditText etWriteComment;
    Button btComment;
    String actionID;
    ArrayList<Comment> comments;
    RecyclerView rvComments;
    CommentAdapter commentAdapter;
    MaterialDialog modal;
    ParseUser user;

    public static DetailFragment newInstance() {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.comment_fragment, null);
        user = ParseUser.getCurrentUser();

//        if (v != null) {
//            ViewGroup parent = (ViewGroup) v.getParent();
//            if (parent != null) {
//                parent.removeView(v);
//            }
//        }
//        try {
//            v = inflater.inflate(R.layout.comment_fragment, container, false);
//        } catch (InflateException e) {
//
//        }

        String fbId = getArguments().getString("fbId");
        String points = getArguments().getString("points");
        String relativeTime = getArguments().getString("relativeTime");
        actionID = getArguments().getString("objectID");
        final String body = getArguments().getString("body");
        context = getActivity();

        ivProfilePicDet = (ImageView) v.findViewById(R.id.ivProfilePicDet);
        tvAction = (TextView) v.findViewById(R.id.tvAction);
        tvPoints = (TextView) v.findViewById(R.id.tvPoints);
        tvTimeStamp = (TextView) v.findViewById(R.id.tvTimeStamp);
        tvLikes = (TextView) v.findViewById(R.id.tvLikes);
        ivFavorite = (ImageButton) v.findViewById(R.id.ivFavorite);
        ivReply = (ImageButton) v.findViewById(R.id.ivReply);
        etWriteComment = (EditText) v.findViewById(R.id.etWriteComment);
        btComment = (Button) v.findViewById(R.id.btComment);
        rvComments = (RecyclerView) v.findViewById(R.id.rvComments);
        tvPoints.setText(points);
        tvTimeStamp.setText(relativeTime);

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("fbId", fbId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null && userList.size() > 0) {
                    // load propic
                    String imgUrl = userList.get(0).getString("profileImgUrl");
                    Log.d("imageeee", imgUrl);
                    Glide.with(context)
                            .load(imgUrl)
                            .placeholder(R.drawable.ic_placeholder)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(ivProfilePicDet);
//
//                    load action body: bold name, compose rest of body using function below
                    String name = userList.get(0).getString("name");
                    SpannableString str = new SpannableString(name + body);
                    str.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvAction.setText(str);
                } else if (e != null) {
                    Log.e("action", "Error: " + e.getMessage());
                }
            }
        });


        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments);
        rvComments.setAdapter(commentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.addItemDecoration(new DividerItemDecoration(getContext()));
        // set the adapter
        //  rvComments.setAdapter(commentAdapter);



        modal = new MaterialDialog.Builder(getContext())
                .customView(v, false)
                .show();
        update();

        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<Action> query = ParseQuery.getQuery("Action");
                query.whereEqualTo("objectId", actionID);
                query.findInBackground(new FindCallback<Action>() {
                    public void done(List<Action> actionList, ParseException e) {
                        Log.d("listSizeee", String.valueOf(actionList.size()));
                        if (e == null && actionList.size() > 0) {
                            Comment comment = new Comment();
                            comment.setUid(user.getString("fbId"));
                            comment.setBody(etWriteComment.getText().toString());
                            comment.setAid(actionID);
                            Log.d("commentUidddd", comment.getUid());

                            comment.saveInBackground();
                            update();
                            etWriteComment.setText("");
                        } else if (e != null) {
                            Log.e("points", "Error: " + e.getMessage());
                        }
                    }
                });

            }
        });

    }


    public void update() {
        ParseQuery<Comment> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo("aid", actionID);
        query.findInBackground(new FindCallback<Comment>() {
            public void done(List<Comment> commentList, ParseException e) {
                if (e == null) {
                    commentAdapter.clear();
                    addItems(commentList);
                } else {
                    Log.d("action", "Error: " + e.getMessage());
                }
            }
        });
    }


    public void addItems(List<Comment> commentList) {
        // iterate through JSON array
        // for each entry, deserialize the JSON object
        for (int i = 0; i < commentList.size(); i++) {
            Comment comment = commentList.get(i);
            Log.d("comment", comment.getString("uid"));
            comments.add(comments.size(), comment);
            commentAdapter.notifyItemInserted(comments.size());
        }
    }

}
