package com.example.kohjingyu.lemons;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by setia on 12/5/2017.
 */

public class Player {
    private final String BASE_URL = "http://devostrum.no-ip.info:12345";

    private String id;
    private String name;
    private String username;
    private String email;
    private JSONArray friends;
    private ArrayList<PlayerActivity> playerActivities;
    private Scores scores;

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
    public Scores getScores() {
        return scores;
    };

    private void setPlayerActivitiesJSON(JSONArray jsonArrayPlayerActivities){
        playerActivities = new ArrayList<>();
        JSONObject tempJson;
        int counter = 0;
        while (counter < jsonArrayPlayerActivities.length()){
            try {
                tempJson = jsonArrayPlayerActivities.getJSONObject(counter);
                PlayerActivity newPlayerActivity = new PlayerActivity(tempJson);
                playerActivities.add(newPlayerActivity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            counter++;
        }
    }

    private void setFriends(JSONArray jsonArrayFriends){
        friends = jsonArrayFriends;
    }

    private void setScores(JSONObject jsonObjectScores){
        this.scores = new Scores(jsonObjectScores);
    }

    public Player(String id, String name, String username, String email){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public Player(String id, String name, String email,
                  JSONArray playerActivities, JSONArray friends, JSONObject scores){
        this.id = id;
        this.name = name;
        this.email = email;
        setPlayerActivitiesJSON(playerActivities);
        setFriends(friends);
        setScores(scores);
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

    private class Scores {
        private int fitness;
        private int academics;
        private int mentalWellness;
        private int diet;

        public Scores(JSONObject jsonObject){
            try {
                this.fitness = jsonObject.getInt("fitness");
                this.academics = jsonObject.getInt("academics");
                this.mentalWellness = jsonObject.getInt("mentalWellness");
                this.diet = jsonObject.getInt("diet");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public int getFitness() {
            return fitness;
        }
        public int getAcademics() {
            return academics;
        }
        public int getMentalWellness() {
            return mentalWellness;
        }
        public int getDiet() {
            return diet;
        }
    }

    public void updateFriends(){
        String urlString = BASE_URL + "/friend?userId=" + this.id;
        String response = "";
        //Open connection to get the json response from server
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null){
                    response += line;
                }
            } else {
                response = null;
                Log.i("connection","Connection not successful");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Processing the JSON response. Update friend lists if success, if not log the error message
        try {
            JSONObject JSONresponse = new JSONObject(response);
            boolean success = JSONresponse.getBoolean("success");

            if (success){
                JSONArray friendJsonArray = JSONresponse.getJSONArray("friends");
                setFriends(friendJsonArray);
            } else {
                String message = JSONresponse.getString("message");
                Log.i("Friend", message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateActivities() {
        String urlString = BASE_URL + "/activity?userId=" + this.id;
        String response = "";
        //Open connection to get the json response from server
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = null;
                Log.i("HTTP", "Connection not successful");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Processing the JSON response. Update user activities lists if success, if not log the error message
        try {
            JSONObject JSONresponse = new JSONObject(response);
            boolean success = JSONresponse.getBoolean("success");
            if (success) {
                JSONArray activitiesJsonArray = JSONresponse.getJSONArray("activities");
                setPlayerActivitiesJSON(activitiesJsonArray);
            } else {
                String message = JSONresponse.getString("message");
                Log.i("Activities", message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    public void updateScores() {
        String urlString = BASE_URL + "/score?userId=" + this.id;
        String response = "";
        //Open connection to get the json response from server
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = null;
                Log.i("HTTP", "Connection not successful");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Processing the JSON response. Update user scores if success, if not log the error message
        try {
            JSONObject JSONresponse = new JSONObject(response);
            boolean success = JSONresponse.getBoolean("success");
            if (success) {
                JSONObject friendJsonObject = JSONresponse.getJSONObject("user");
                setScores(friendJsonObject);
            } else {
                String message = JSONresponse.getString("message");
                Log.i("Scores", message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;

    }

}
