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
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
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
    Action action;

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

        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");
        userQuery.whereEqualTo("fbId", fbId);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
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


        ParseQuery<Action> actionQuery = ParseQuery.getQuery("Action");
        actionQuery.whereEqualTo("objectId", actionID);
        actionQuery.findInBackground(new FindCallback<Action>() {
            public void done(List<Action> actionList, ParseException e) {
                if (e == null && actionList.size() > 0) {
                    Log.d("actionComments", "reloading");
                    action = actionList.get(0);
                    // load original comments
                    JSONArray commentJSON= action.getJSONArray("comments");
                    if (commentJSON == null) {
                        commentJSON = new JSONArray();
                        action.put("comments", commentJSON);
                    }
                    for (int i = 0; i < commentJSON.length(); i++) {
                        try {
                            Log.d("actionComments", "adding comment");
                            addComment(Comment.fromJSON(commentJSON.getJSONObject(i)));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                } else if (e != null) {
                    Log.e("points", "Error: " + e.getMessage());
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

        // add new comments from reply field
        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comment = new Comment();
                comment.setUid(user.getString("fbId"));
                comment.setBody(etWriteComment.getText().toString());
                Date date = new Date();
                comment.setDate(date);
                Log.d("timestamp created", date.toString());

                addComment(comment);
                JSONArray commentArray = action.getJSONArray("comments");
                commentArray.put(comment.toJSON());
                action.put("comments", commentArray);

                action.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("actionComments", "new: "+ String.valueOf(comments.size()));
                        etWriteComment.setText("");
                    }
                });

            }
        });

        modal = new MaterialDialog.Builder(getContext())
                .customView(v, false)
                .show();


    }



    public void addComment(Comment comment) {
        // iterate through JSON array
        // for each entry, deserialize the JSON object
        Log.d("comment", comment.getUid());
        comments.add(comments.size(), comment);
        commentAdapter.notifyItemInserted(comments.size());
    }

}
