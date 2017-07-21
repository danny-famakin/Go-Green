package com.codepath.gogreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.gogreen.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by anyazhang on 7/21/17.
 */

public class FloatingMenuFragment extends Fragment {
    LayoutInflater inflater;
    View v;
    Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_floating_menu, null);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("fab", "clicked");
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


// repeat many times:

        SubActionButton button1 = createSubActionButton(R.drawable.ic_transit2);
        SubActionButton button2 = createSubActionButton(R.drawable.ic_water);
        SubActionButton button3 = createSubActionButton(R.drawable.ic_bag3);
        SubActionButton button4 = createSubActionButton(R.drawable.ic_can2);



        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(fab)
                .setRadius(500)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitFragment transitFragment = TransitFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, transitFragment);
                // commit
                ft.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaterFragment waterFragment = WaterFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, waterFragment);
                // commit
                ft.commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReuseFragment reuseFragment = ReuseFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, reuseFragment);
                // commit
                ft.commit();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecycleFragment recycleFragment = RecycleFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // make change
                ft.replace(R.id.flContainer, recycleFragment);
                // commit
                ft.commit();
            }
        });

    }

    public SubActionButton createSubActionButton(int iconId) {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
        int subActionButtonSize = 280;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);
        ImageView icon = new ImageView(context);
        Glide.with(context)
                .load(iconId)
                .into(icon);
        //icon.setImageResource(R.drawable.ic_add_circle_black_24dp);
        return itemBuilder.setContentView(icon).setLayoutParams(params).build();
    }

}
