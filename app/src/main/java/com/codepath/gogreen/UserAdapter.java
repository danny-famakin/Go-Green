package com.codepath.gogreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by anyazhang on 7/21/17.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<ParseUser> mUsers;
    Context context;
    boolean isSourceLeaderboard;



    public UserAdapter(List<ParseUser> users, boolean source) {
        mUsers = users;
        isSourceLeaderboard = source;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(itemView);
    }

    @Override

    public void onBindViewHolder(final UserAdapter.ViewHolder holder, int position) {
        final ParseUser user = mUsers.get(position);
        if (isSourceLeaderboard) {
            holder.tvRank.setVisibility(View.VISIBLE);
            holder.tvRank.setText(String.valueOf(position + 1));
        }
        else {
            holder.tvRank.setVisibility(View.GONE);
        }

        holder.tvName.setText(user.getString("name"));
        Log.d("userAdapter", user.getString("name"));
        double points = user.getDouble("totalPoints");
        holder.tvPoints.setText(String.format("%.1f", points));


        holder.rlUser.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent (context, OtherUserActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, holder.ivProfilePic,
                                ViewCompat.getTransitionName(holder.ivProfilePic));
                i.putExtra("screenName", String.valueOf(user.get("name")));
                i.putExtra("profImage", user.getString("profileImgUrl"));
                i.putExtra("Id", String.valueOf(user.get("fbId")));
                i.putExtra("joinDate", String.valueOf(user.get("joinDate")));
                double pts = user.getDouble("totalPoints");
                String points = String.format("%.1f", pts);
                i.putExtra("points", points);
                context.startActivity(i, options.toBundle());
            }
        });

        Glide.with(context)
            .load(user.getString("profileImgUrl"))
            .bitmapTransform(new CropCircleTransformation(context))
            .into(holder.ivProfilePic);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRank;
        public TextView tvName;
        public TextView tvPoints;
        public ImageView ivProfilePic;
        public LinearLayout userInfo;
        public RelativeLayout rlUser;


        public ViewHolder(View itemView) {
            super(itemView);
            tvRank = (TextView) itemView.findViewById(R.id.tvRank);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPoints = (TextView) itemView.findViewById(R.id.tvPoints);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            userInfo = (LinearLayout) itemView.findViewById(R.id.userInfo);
            rlUser = (RelativeLayout) itemView.findViewById(R.id.rlUser);

        }
    }

    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

}
