package com.codepath.gogreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.gogreen.models.Action;

import java.util.List;


/**
 * Created by melissaperez on 7/17/17.
 */

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {

    private List<Action> mActions;
    Context context;
    public ActionAdapter(List<Action> actions){mActions = actions;}

    @Override
    public ActionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActionAdapter.ViewHolder holder, int position) {
        final Action action = mActions.get(position);
        holder.tvAction.setText(action.get("actionType").toString());
//        Log.d("action", action.getActionType());

    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAction;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAction = (TextView) itemView.findViewById(R.id.tvAction);
        }
    }
}
