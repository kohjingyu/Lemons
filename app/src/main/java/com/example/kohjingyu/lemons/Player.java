package com.example.kohjingyu.lemons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by setia on 12/5/2017.
 */

public class Player {
    private String id;
    private String name;
    private String username;
    private String email;
    private JSONArray friends;
    private ArrayList<PlayerActivity> playerActivities;

    public String getId() {
    return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public JSONArray getFriends() {
        return friends;
    }

    public ArrayList<PlayerActivity> getPlayerActivities() {
        return playerActivities;
    }

    public void setPlayerActivitiesJSON(JSONArray jsonArrayPlayerActivities){
        playerActivities = new ArrayList<>();
        int counter = 0;
        while (counter < jsonArrayPlayerActivities.length()){
            try {
                JSONObject tempJson = null;
                tempJson = jsonArrayPlayerActivities.getJSONObject(counter);
                PlayerActivity newPlayerActivity = new PlayerActivity(tempJson);
                playerActivities.add(newPlayerActivity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            counter++;
        }
    }

    public void setFriends(JSONArray jsonArrayFriends){
        friends = new JSONArray();

    }

    public Player(String id, String name, String username, String email){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public Player(String id, String name, String email,
                  JSONArray playerActivities, JSONArray friends){
        this.id = id;
        this.name = name;
        this.email = email;
        setPlayerActivitiesJSON(playerActivities);
        setFriends(friends);
    }

    private class PlayerActivity {
        private int activityId;
        private String activityType;
        private int score;
        private String remarks;

        public PlayerActivity(JSONObject jsonObject){
            try {
                this.activityId = jsonObject.getInt("activityId");
                this.activityType = jsonObject.getString("activityType");
                this.score = jsonObject.getInt("score");
                this.remarks = jsonObject.getString("remarks");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int getActivityId() {
            return activityId;
        }

        public String getActivityType() {
            return activityType;
        }

        public int getScore() {
            return score;
        }

        public String getRemarks() {
            return remarks;
        }
    }
}
