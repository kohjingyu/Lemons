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
            userInformationTextView = this.v.findViewById(R.id.viewholder_textview);
            String activityName = playerActivities.get(position).getActivityType();
            int activityScore = playerActivities.get(position).getScore();
            String activityRemarks = playerActivities.get(position).getRemarks();
            userInformationTextView.setText(activityName + "\n" + activityRemarks + "\n" + "Score: " + activityScore);
        }

        @Override
        public void onClick(View v) {
            // Nothing for now
        }

    }
}
