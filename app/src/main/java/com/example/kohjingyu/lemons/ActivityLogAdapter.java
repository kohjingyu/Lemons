package com.example.kohjingyu.lemons;

/**
 * Created by kohjingyu on 8/12/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;


public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ActivityViewHolder> {

    Context parentContext;
    ArrayList<Player.PlayerActivity> playerActivities;

    ActivityLogAdapter(Context context, ArrayList<Player.PlayerActivity> playerActivities){
        this.parentContext = context;
        this.playerActivities = playerActivities;
    }

    public void update(ArrayList<Player.PlayerActivity> playerActivities){
        this.playerActivities = playerActivities;
    }


    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View view = inflater.inflate(R.layout.activity_log_view_holder, parent, false);

        ActivityViewHolder activityLogAdapter = new ActivityViewHolder(view);

        return activityLogAdapter;
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return playerActivities.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userInformationTextView;
        View v;
        ActivityViewHolder(View v){
            super(v);
            this.v = v;
            v.setOnClickListener(this);
        }

        public void bind(int position){
            Player.PlayerActivity activity = playerActivities.get(position);

            userInformationTextView = this.v.findViewById(R.id.viewholder_textview);
            String activityName = activity.getActivityType();
            int activityScore = activity.getScore();
            String activityRemarks = activity.getRemarks();
            Timestamp timestamp = activity.getTimestamp();

            // TODO: Format timestamp

            userInformationTextView.setText(activityName + " (" + timestamp + ")" + "\n" + activityScore + " " + activityRemarks);
        }

        @Override
        public void onClick(View v) {
            // Nothing for now
        }

    }
}
