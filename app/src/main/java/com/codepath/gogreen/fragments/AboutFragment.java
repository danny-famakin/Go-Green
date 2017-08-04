package com.codepath.gogreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.gogreen.R;

/**
 * Created by anyazhang on 8/4/17.
 */

public class AboutFragment extends Fragment {
    Context context;
    ArrayAdapter<String> listAdapter;
    ListView sourceList;
    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_about, null);

        sourceList = (ListView) v.findViewById(R.id.lvSources);
        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
//        ArrayList<String> planetList = new ArrayList<String>();
//        planetList.addAll( Arrays.asList(planets) );
//
//        // Create ArrayAdapter using the planet list.
//        listAdapter = new ArrayAdapter<String>(context, R.layout.item_source, planetList);
//
//        // Add more planets. If you passed a String[] instead of a List<String>
//        // into the ArrayAdapter constructor, you must not add more items.
//        // Otherwise an exception will occur.
//        listAdapter.add( "Ceres" );
//        listAdapter.add( "Pluto" );
//        listAdapter.add( "Haumea" );
//        listAdapter.add( "Makemake" );
//        listAdapter.add( "Eris" );

        // Set the ArrayAdapter as the ListView's adapter.
        sourceList.setAdapter( listAdapter );


        MaterialDialog modal = new MaterialDialog.Builder(context)
                .customView(v, false)
                .show();
    }
}
