package com.codepath.gogreen;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.gogreen.models.Comment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by melissaperez on 7/27/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {

    private List<Comment> mComments;
    Context context;

    public CommentAdapter(List<Comment> comments) {
        mComments = comments;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("commentAdapter", "oncreateviewholder");
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, final int position) {
        final Comment comment = mComments.get(position);
        Log.d("commentAdapter", "onBindViewHolder");
        //holder.tvPost.setText();

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("fbId", comment.getUid());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null && userList.size() > 0) {
                    // load propic
                    String imgUrl = userList.get(0).getString("profileImgUrl");
                    Glide.with(context)
                            .load(imgUrl)
                            .placeholder(R.drawable.ic_placeholder)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(holder.ivComment);

                    // load action body: bold name, compose rest of body using function below
                    String name = userList.get(0).getString("name");
                    String body = comment.getBody();
                    SpannableString str = new SpannableString(name);
                    str.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tvName.setText(str);
                    holder.tvPost.setText(body);

                    Date timeStamp = comment.getDate();
                    if (timeStamp == null) {
                        timeStamp = new Date();
                    }
                    Log.d("timestamp", timeStamp.toString());
                    String relativeTime = TimeStampUtils.getRelativeTimeAgo(timeStamp);
                    Log.d("timestamp", relativeTime);
                    holder.tvTimeStamp.setText(TimeStampUtils.shortenTimeStamp(relativeTime, context));
                } else if (e != null) {
                    Log.e("action", "Error: " + e.getMessage());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivComment;
        TextView tvPost;
        TextView tvName;
        TextView tvTimeStamp;

        public ViewHolder(View itemView) {
            super(itemView);

            ivComment = (ImageView) itemView.findViewById(R.id.ivComment);
            tvPost = (TextView) itemView.findViewById(R.id.tvPost);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
        }
    }
    public void clear() {
        int size = this.mComments.size();
        this.mComments.clear();
        notifyItemRangeRemoved(0, size);
    }



}
