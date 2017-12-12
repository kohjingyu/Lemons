package com.example.kohjingyu.lemons;

/**
 * Created by kohjingyu on 8/12/17.
 */

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;


public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ActivityViewHolder> {

    Context parentContext;
    ArrayList<Player.PlayerActivity> playerActivities;
    private static final String ACADEMICS = "Academics";
    private static final String FITNESS = "Fitness";
    private static final String DIET = "Diet";
    private static final String MENTAL = "Mental Wellness";

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
        ImageView potion;
        View v;
        ActivityViewHolder(View v){
            super(v);
            this.v = v;
            v.setOnClickListener(this);
        }

        public void bind(int position){
            Player.PlayerActivity activity = playerActivities.get(position);

            userInformationTextView = this.v.findViewById(R.id.viewholder_textview);
            potion = this.v.findViewById(R.id.potion);
            String activityName = activity.getActivityType();
            int activityScore = activity.getScore();
            String activityRemarks = activity.getRemarks();
            Timestamp timestamp = activity.getTimestamp();
            String timestampStr = String.format("On %1$TD at %1$TT", timestamp);
            userInformationTextView.setText(activityName + " (" + timestampStr + ")" + "\n" + activityScore + " " + activityRemarks);
            String msg="";

            if(activityName.equals("academics")) {
                potion.setImageResource(R.drawable.acadspotion);
                activityName = ACADEMICS;
                msg =String.format("(%s at %s) %s: %s \n", activityName, timestamp, activityRemarks, activityScore);
                if(activityScore<=2) msg += "Don't worry, there's time to buck up!";
                else if(activityScore<=4) msg+= "Keep it up!";
                else msg += "Well done!!!!!";
            }
            if(activityName.equals("fitness")) {
                potion.setImageResource(R.drawable.fitnesspotion);
                activityName = FITNESS;
                msg =String.format("(%s at %s) %s: %s\n", activityName, timestamp, activityRemarks, activityScore);
                if(activityScore>=1000000) msg += "You're a fitspo!!";
                else if(activityScore>=10000&&activityScore<1000000) msg+= "Keep it up!";
                else msg += "Walk more please, couch potato.";
            }
            if(activityName.equals("diet")) {
                potion.setImageResource(R.drawable.dietpotion);
                activityName = DIET;
                msg =String.format("(%s at %s) %s: %s\n", activityName, timestamp, activityRemarks, activityScore);
            }
            if(activityName.equals("mentalWellness")) {
                potion.setImageResource(R.drawable.mentalpotion);
                activityName = MENTAL;
                msg =String.format("(%s at %s) %s\n", activityName, timestamp, activityRemarks);
                msg += "You do you!";
            }

            userInformationTextView.setText(msg);
        }

        @Override
        public void onClick(View v) {
            // Nothing for now
        }

    }
}
