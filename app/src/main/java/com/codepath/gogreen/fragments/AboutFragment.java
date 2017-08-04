package com.codepath.gogreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.gogreen.R;

/**
 * Created by anyazhang on 8/4/17.
 */

public class AboutFragment extends Fragment {
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_about, null);
        MaterialDialog modal = new MaterialDialog.Builder(context)
                .customView(v, false)
                .show();
    }
}
