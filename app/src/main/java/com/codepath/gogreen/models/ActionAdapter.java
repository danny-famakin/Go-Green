package com.codepath.gogreen.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.gogreen.OtherUserActivity;
import com.codepath.gogreen.ProfileActivity;
import com.codepath.gogreen.R;
import com.codepath.gogreen.ResourceUtils;
import com.codepath.gogreen.TimeStampUtils;
import com.codepath.gogreen.fragments.DetailFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by melissaperez on 7/17/17.
 */

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {

    private List<Action> mActions;
    Context context;
    String relativeTime;
    String timeShort;
    ParseUser currentUser;

    public ActionAdapter(List<Action> actions) {
        mActions = actions;
    }

    @Override
    public ActionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_action, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ActionAdapter.ViewHolder holder, int position) {
        final Action action = mActions.get(position);

        Date timeStamp = (action.getCreatedAt());
        if (timeStamp == null) {
            timeStamp = new Date();
        }
        relativeTime = TimeStampUtils.getRelativeTimeAgo(timeStamp);
        timeShort = TimeStampUtils.shortenTimeStamp(relativeTime, context);
        holder.tvTimeStamp.setText(timeShort);
        holder.tvPoints.setText(String.format("%.1f", action.getDouble("points")));
        currentUser = ParseUser.getCurrentUser();
        final String uId = currentUser.getString("fbId");

        // get user associated with action
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("fbId", action.getUid());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null && userList.size() > 0) {
                    // load propic
                    final String imgUrl = userList.get(0).getString("profileImgUrl");
                    final String username = userList.get(0).getString("name");
                    final String Id = userList.get(0).getString("fbId");
                    final Date joinDate = userList.get(0).getDate("joinDate");
                    final double pts = userList.get(0).getDouble("totalPoints");
                    final String points = String.format("%.1f", pts);
                    Glide.with(context)
                            .load(imgUrl)
                            .placeholder(R.drawable.ic_placeholder)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(holder.ivProfilePic);
                    //Load user profile on clicking profile image
                    holder.ivProfilePic.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            if (action.getUid().equals(uId)){
                                Intent p = new Intent (context, ProfileActivity.class);
                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation((Activity) context, holder.ivProfilePic, ViewCompat.
                                                getTransitionName(holder.ivProfilePic));
                                context.startActivity(p, options.toBundle());
                            }

                            else{
                            Intent i = new Intent (context, OtherUserActivity.class);
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, holder.ivProfilePic, ViewCompat.
                                            getTransitionName(holder.ivProfilePic));
                            i.putExtra("profImage", imgUrl);
                            i.putExtra("screenName", username);
                            i.putExtra("Id", Id);
                            i.putExtra("joinDate", joinDate);
                            i.putExtra("points", points);
                            context.startActivity(i, options.toBundle());
                            }

                        }
                    });


                    // load action body: bold name, compose rest of body using function below
                    String name = userList.get(0).getString("name");
                    final int color = new ResourceUtils(context).getColorArray(action.getActionType(), 4).get(3);
                    SpannedString body = composeActionBody(action, color);
                    SpannableString str = new SpannableString(name);
                    str.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    str.setSpan(new ForegroundColorSpan(color), name.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tvAction.setText(TextUtils.concat(str," " ,body));
//                    holder.line.setBackgroundColor(color);
                    holder.tvComments.setText(String.valueOf(action.getJSONArray("comments").length()));

                    holder.ivReply.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String body = " " + composeActionBody(action, color);

                            Bundle bundle = new Bundle();

                            bundle.putString("fbId", action.getUid());
                            bundle.putString("body", body);
                            bundle.putString("points",String.format("%.1f", action.getDouble("points")));
                            bundle.putString("timeStamp", holder.tvTimeStamp.getText().toString());
                            bundle.putString("objectID", action.getObjectId().toString());


                            try {
                                bundle.putString("action", action.toJSON().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            bundle.putString("body", body);
                            bundle.putString("authorName", username);
                            bundle.putString("authorImg", imgUrl);
                            bundle.putString("objectID", action.getObjectId().toString());
                            bundle.putBoolean("comment", true);

                            DetailFragment detailFragment = DetailFragment.newInstance();
                            detailFragment.setArguments(bundle);

                            FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager()
                                    .beginTransaction();
                            // make change
                            ft.replace(R.id.flContainer, detailFragment, "TAG_FRAGMENT");
                            // commit
                            ft.commit();
                        }
                    });
                    holder.rlAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body = " " + composeActionBody(action, color);

                            Bundle bundle = new Bundle();

                            bundle.putString("fbId", action.getUid());
                            bundle.putString("body", body);
                            bundle.putString("points",String.format("%.1f", action.getDouble("points")));
                            bundle.putString("timeStamp", holder.tvTimeStamp.getText().toString());
                            bundle.putString("objectID", action.getObjectId().toString());
                            bundle.putBoolean("comment", false);

                            try {
                                bundle.putString("action", action.toJSON().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            bundle.putString("body", body);
                            bundle.putString("authorName", username);
                            bundle.putString("authorImg", imgUrl);
                            bundle.putString("objectID", action.getObjectId().toString());


                            DetailFragment detailFragment = DetailFragment.newInstance();
                            detailFragment.setArguments(bundle);

                            FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager()
                                    .beginTransaction();
                            // make change
                            ft.replace(R.id.flContainer, detailFragment, "TAG_FRAGMENT");
                            // commit
                            ft.commit();

                        }
                    });
                } else if (e != null) {
                    Log.e("action", "Error: " + e.getMessage());
                }
            }
        });

                    int counter = 0;

                    JSONArray ids = action.getJSONArray("favorited");
                    ParseUser current = ParseUser.getCurrentUser();

                    if(ids == null){
                        holder.ivFavorite.setImageResource(R.drawable.ic_fave);
                    }
                    else{
                        for(int a = 0; a < ids.length(); a++ ){
                            try {
                                if(String.valueOf(ids.get(a)).equals(current.getString("fbId"))){
                                    counter++;
                                    holder.ivFavorite.setImageResource(R.drawable.ic_faved);
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if(counter == 0){
                            holder.ivFavorite.setImageResource(R.drawable.ic_fave);

                        }
                    }
                    if(ids != null){
                        holder.tvLikes.setText(String.valueOf(ids.length()));
                    }
    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAction;
        public TextView tvTimeStamp;
        public TextView tvPoints;
        public TextView tvComments;
        public ImageView ivProfilePic;
        public ImageButton ivFavorite;
        public TextView tvLikes;
        public RelativeLayout rlAction;
        public ImageView ivReply;
        public View line;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAction = (TextView) itemView.findViewById(R.id.tvAction);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvPoints = (TextView) itemView.findViewById(R.id.tvPoints);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePicDet);
            tvComments = (TextView) itemView.findViewById(R.id.tvComments);
            ivFavorite = (ImageButton) itemView.findViewById(R.id.ivFavorite);
            ivReply = (ImageButton) itemView.findViewById(R.id.ivReply);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            rlAction = (RelativeLayout) itemView.findViewById(R.id.rlAction);
            line = (View) itemView.findViewById(R.id.line);

            //Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/JosefinSansSemiBold.ttf");
            //tvAction.setTypeface(myTypeFace);
            ivFavorite.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                int counter = 0;
                final Action toFavorite = mActions.get(position);
                ParseUser current = ParseUser.getCurrentUser();
                JSONArray ids = toFavorite.getJSONArray("favorited");
             //   Log.d("hopeee", String.valueOf(ids));
                if(ids == null){
                    ids = new JSONArray();
                    ids.put(current.getString("fbId"));
                    toFavorite.setFavorited(ids);
                    toFavorite.saveInBackground();
                    ivFavorite.setImageResource(R.drawable.ic_faved);
                    int likes = Integer.valueOf(tvLikes.getText().toString());
                    tvLikes.setText(String.valueOf(likes + 1));
                }
                else{
                    for(int i = 0; i < ids.length(); i++ ){
                        try {
                            if(ids.get(i).equals(current.getString("fbId")) ){
                                counter++;
                                ids.remove(i);
                                ivFavorite.setImageResource(R.drawable.ic_fave);
                                toFavorite.setFavorited(ids);
                                toFavorite.saveInBackground();
                                int likes = Integer.valueOf(tvLikes.getText().toString());
                                tvLikes.setText(String.valueOf(likes - 1));
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if(counter == 0){
                        ids.put(current.getString("fbId"));
                        ivFavorite.setImageResource(R.drawable.ic_faved);
                        toFavorite.setFavorited(ids);
                        toFavorite.saveInBackground();
                        int likes = Integer.valueOf(tvLikes.getText().toString());
                        tvLikes.setText(String.valueOf(likes + 1));
                    }
                }


            }
        });

        }
    }



    public SpannedString composeActionBody(Action action, int color) {
        SpannedString body = new SpannedString("");
        SpannableString str = new SpannableString("");

        switch (action.getString("actionType").toString()) {
            case "transit":
                String vehicle;
                String subType = action.getSubType();

                if (subType.equals("bus") || subType.equals("subway") || subType.equals("train")) {
                    str = new SpannableString("took the " + subType + " for");
                    str.setSpan(new ForegroundColorSpan(color), 0, 9 + subType.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (subType.equals("walking") || subType.equals("bike")) {
                    if (subType.substring(subType.length() - 3, subType.length()).equals("ing")) {
                        subType = subType.substring(0, subType.length() - 3);
                    }
                    str = new SpannableString(checkPastTense(subType));
                    str.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else if (subType.equals("carpool")) {
                    str = new SpannableString("carpooled for");
                    str.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else {
                    Log.e("transit", "subtype none of the above");
                }
                body = (SpannedString) TextUtils.concat(str, " ", new SpannableString(new ResourceUtils(context).checkUnits(action.getMagnitude(), "%.1f", context.getResources().getString(R.string.distance_units), false)));
                break;
            case "water":
                str = new SpannableString("took a " + String.format("%.1f", action.getMagnitude()) + " " + context.getResources().getString(R.string.shower_units) + " shower");
                str.setSpan(new ForegroundColorSpan(color), str.length() - 6, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                body = new SpannedString(str);
                break;
            case "reuse":
                str = new SpannableString("reused " + new ResourceUtils(context).checkUnits(action.getMagnitude(), "%.1f", "bag", true));
                str.setSpan(new ForegroundColorSpan(color), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                body = new SpannedString(str);
                break;
            case "recycle":
                String units = action.getSubType();
                String suffix = "";
                if (action.getSubType().equals("paper")) {
                    units = "sheet";
                    suffix = " of paper";
                }
                str = new SpannableString("recycled " + new ResourceUtils(context).checkUnits(action.getMagnitude(), "%.1f", units, true) + suffix);
                str.setSpan(new ForegroundColorSpan(color), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                body = new SpannedString(str);
                break;
        }

        return body;

    }



    // appends "d" or "ed"
    public String checkPastTense(String word) {
        if (word.charAt(word.length() - 1) == 'e') {
            return word + 'd';
        } else {
            return word + "ed";
        }
    }

    public void clear() {
        int size = this.mActions.size();
        this.mActions.clear();
        notifyItemRangeRemoved(0, size);
    }



}
