package com.codepath.gogreen.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.gogreen.models.Action;
import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by anyazhang on 7/13/17.
 */

public class ModalFragment extends Fragment {
    Context context;
    MaterialDialog modal;
    public OnItemSelectedListener listener;
    public String USER_ID = ParseUser.getCurrentUser().getString("fbId");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onOpenModal();
    }

    public static ModalFragment newInstance(String action_id) {

        Bundle args = new Bundle();
        args.putString("action_id", action_id);

        ModalFragment fragment = new ModalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onOpenModal() {
    }

    public void openModal(View view) {
//        String action_id = getArguments().getString("action_id");
//        int layoutResourceId = 0;
//        switch (action_id) {
//            case "transit":
//                layoutResourceId = R.layout.activity_log_transit;
//                transitFragment = new TransitFragment();
//                replaceFragment(transitFragment);
//                break;
//            case "water":
//                layoutResourceId = R.layout.activity_log_water;
//                break;
//            case "reuse":
//                layoutResourceId = R.layout.activity_log_reuse;
//                break;
//            case "recycle":
//                layoutResourceId = R.layout.activity_log_recycle;
//                break;
//        }

         modal = new MaterialDialog.Builder(getContext())
                .customView(view, false)
                .positiveText("LOG")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        onSave();
                    }
                })
                .autoDismiss(false)
                .show();
    }

    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void updateFeed(Action action);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ModalFragment.OnItemSelectedListener");
        }

    }

    public void onSave() {

    }

    // adapted from https://www.excella.com/insights/how-do-i-validate-an-android-form
    public boolean isValid(EditText editText, int min, int max) {
        String text = editText.getText().toString();
        String errormsg;
        if (StringUtils.isBlank(text)) {
            errormsg = "field required!";
        } else if (Integer.parseInt(text) < min){
            errormsg = "minimum = " + String.valueOf(min);

        } else if (Integer.parseInt(text) > max) {
            errormsg = "maximum = " + String.valueOf(max);
        } else {
            editText.setError(null);
            return true;
        }
        editText.setError(errormsg);
        return false;

    }

    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
