package com.codepath.gogreen.models;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.gogreen.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by melissaperez on 7/27/17.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder>  {

    private List<Bundle> mSources;
    Context context;

    public SourceAdapter(ArrayList<Bundle> sources) {
        mSources = sources;
    }

    @Override
    public SourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("SourceAdapter", "oncreateviewholder");
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_source, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final SourceAdapter.ViewHolder holder, final int position) {
        final Bundle source= mSources.get(position);
        Log.d("SourceAdapter", "onBindViewHolder");
        //holder.tvPost.setText();


        String sectTitle = source.getString("sectTitle");
        String[] sourceLinks = source.getStringArray("sourceLinks");
        String[] sourceIcons = source.getStringArray("sourceIcons");
        int titleColor = source.getInt("titleColor");

        if (!holder.tvSectTitle.getText().toString().equals(sectTitle)) {
            holder.tvSectTitle.setText(sectTitle);
            holder.tvSectTitle.setTextColor(context.getResources().getColor(titleColor));

            for (int i = 0; i < sourceLinks.length; i++) {
                LinearLayout sourceLayout = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 32, 0, 0);
                sourceLayout.setLayoutParams(layoutParams);

                sourceLayout.setOrientation(LinearLayout.HORIZONTAL);
                ImageView sourceIcon = new ImageView(context);
                LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.icon_height), (int) context.getResources().getDimension(R.dimen.icon_width));
                iconParams.setMargins(0, 0, 32, 0);
                sourceIcon.setLayoutParams(new LinearLayout.LayoutParams(iconParams));

                String imgUrl = sourceIcons[i];

                Glide.with(context)
                        .load(imgUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .override(48, 48)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(sourceIcon);

                TextView sourceText = new TextView(context);
                Log.d("stringid", sectTitle + String.valueOf(i));
                sourceText.setText(Html.fromHtml(sourceLinks[i]));
                sourceText.setLinkTextColor(context.getResources().getColor(R.color.colorLink));
                sourceText.setMovementMethod(LinkMovementMethod.getInstance());
                sourceLayout.addView(sourceIcon);
                sourceLayout.addView(sourceText);
                holder.llSources.addView(sourceLayout);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSectTitle;
        LinearLayout llSources;


        public ViewHolder(View itemView) {
            super(itemView);
            tvSectTitle = (TextView) itemView.findViewById(R.id.tvSectTitle);
            llSources = (LinearLayout) itemView.findViewById(R.id.llSources);

        }
    }
    public void clear() {
        int size = this.mSources.size();
        this.mSources.clear();
        notifyItemRangeRemoved(0, size);
    }


}
