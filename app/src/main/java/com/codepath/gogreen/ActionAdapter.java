package com.codepath.gogreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.gogreen.models.Action;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by melissaperez on 7/17/17.
 */

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {

    private List<Action> mActions;
    Context context;

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
        holder.tvAction.setText(composeActionBody(action));

        Date timeStamp = (action.getCreatedAt());
//        if (timeStamp == null) {
//            timeStamp = Date
//        }
        holder.tvTimeStamp.setText(shortenTimeStamp(getRelativeTimeAgo(timeStamp)));
        holder.tvPoints.setText("Points earned: " + String.format("%.1f", action.getDouble("points")));

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("fbId", action.getUid());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                Log.d("actionListsize", String.valueOf(userList.size()));
                if (e == null && userList.size() > 0) {
                    String imgUrl = userList.get(0).getString("profileImgUrl");
                    Glide.with(context)
                        .load(imgUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        
                        .into(holder.ivProfilePic);

                } else {
                    Log.d("action", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAction;
        public TextView tvTimeStamp;
        public TextView tvPoints;
        public ImageView ivProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAction = (TextView) itemView.findViewById(R.id.tvAction);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvPoints = (TextView) itemView.findViewById(R.id.tvPoints);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
        }
    }

    public String getRelativeTimeAgo(Date date) {
        long dateMillis = date.getTime();
        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }

    public String shortenTimeStamp(String timestamp) {
        String[] splitTime = timestamp.trim().split("\\s+");
        List<String> times = Arrays.asList("second", "seconds", "minute", "minutes", "hour", "hours", "day", "days", "week", "weeks");
        // deal with recent tweets of form "# _ ago"
        if (times.contains(splitTime[1])) {
            timestamp = splitTime[0] + splitTime[1].charAt(0);
        }
        // deal with old tweets of form M D, Y
        else if (splitTime[2].equals(context.getString(R.string.current_year))) {
            timestamp = splitTime[0] + " " + splitTime[1].substring(0, splitTime[1].length() - 1);
        }
        return timestamp;
    }

    public String composeActionBody(Action action) {
        String body = "";
        switch (action.getString("actionType").toString()) {
            case "transit":
                String vehicle;
                String subType = action.getSubType();
                Log.d("transit", subType);

                if (subType.equals("bus") || subType.equals("subway") || subType.equals("train")) {
                    body = "took the " + subType + " for";
                }
                else if (subType.equals("walk") || subType.equals("bike")) {
                    body = checkPastTense(subType);
                }
                else if (subType.equals("carpool")) {
                    body = "carpooled for";
                }
                else {
                    Log.d("transit", "subtype none of the above");
                }
                body += " " + checkUnits(action.getMagnitude(), context.getResources().getString(R.string.distance_units), false);
                break;
            case "water":
                body = "took a " + String.format("%.1f", action.getMagnitude()) + " " + context.getResources().getString(R.string.shower_units) + " shower";
                break;
            case "reuse":
                body = "reused " + checkUnits(action.getMagnitude(), "bag", true);
                break;
            case "recycle":
                body = "recycled " + checkUnits(action.getMagnitude(), action.getSubType(), true);
                break;
        }

        return body;

    }

    // returns string containing plural form of unit only if magnitude != 1
    public String checkUnits(double magnitude, String units, boolean castToInt) {
        if (magnitude == 1.) {
            if (castToInt) {
                return (int) magnitude + " " + units;
            }
            else {
                return String.format("%.1f", magnitude) + " " + units;
            }
        }
        else {
            if (castToInt) {
                return (int) magnitude + " " + units + "s";
            }
            else {
                return String.format("%.1f", magnitude) + " " + units + "s";
            }
        }
    }

    // appends "d" or "ed"
    public String checkPastTense(String word) {
        if (word.charAt(word.length() - 1) == 'e') {
            return word + 'd';
        }
        else {
            return word + "ed";
        }
    }
}
