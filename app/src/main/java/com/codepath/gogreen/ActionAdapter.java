package com.codepath.gogreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.gogreen.models.Action;

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
    public void onBindViewHolder(ActionAdapter.ViewHolder holder, int position) {
        final Action action = mActions.get(position);
        holder.tvAction.setText(action.get("actionType").toString());

        String relativeTime = getRelativeTimeAgo(action.getCreatedAt());
        holder.tvTimeStamp.setText(shortenTimeStamp(relativeTime));
        holder.tvPoints.setText(action.get("points").toString());
        Log.d("action", String.valueOf(action.getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAction;
        public TextView tvTimeStamp;
        public TextView tvPoints;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAction = (TextView) itemView.findViewById(R.id.tvAction);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvPoints = (TextView) itemView.findViewById(R.id.tvPoints);
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
}
