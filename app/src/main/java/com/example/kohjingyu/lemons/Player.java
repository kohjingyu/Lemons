package com.example.kohjingyu.lemons;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by setia on 12/5/2017.
 */

public class Player {
    public static final String BASE_URL = "http://devostrum.no-ip.info:12345";
    public final String MAPLE_URL_HEAD = "https://labs.maplestory.io/api/gms/latest/character/center/2000/20001,30037";
    public final String MAPLE_URL_TAIL = "/stand1?showears=false&resize=1";

    //for stats activity
    public static int level;
    public static int academicMilestone;
    public static int fitnessMilestone;
    public static int dietMilestone;
    public static int mentalMilestone;
    public static final String ACADEMICS = "Academics";
    public static final String FITNESS = "Fitness";
    public static final String DIET = "Diet";
    public static final String MENTAL = "Mental Wellness";

    private int id;
    private String name;
    private String username;
    private String email;
    private JSONArray friends;
    private ArrayList<PlayerActivity> playerActivities;
    private Scores scores;
    private static Player player;
    private JSONObject equipped = new JSONObject();

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player newPlayer) {
        player = newPlayer;
    }

    public int getId() {
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
    public JSONObject getEquipped(){ return equipped;}

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

    public URL mapleURLGenerator(JSONObject jsonObject){
        String requestURL = "";
        URL url = null;
        requestURL = MAPLE_URL_HEAD;
        Iterator<String> jsonIterator = jsonObject.keys();
            try {
                while (jsonIterator.hasNext()) {
                    String key = jsonIterator.next();
                    String itemId = jsonObject.getString(key);
                    if (!itemId.equals("0")){
                        requestURL += "," + itemId;
                    }
                }
                requestURL += MAPLE_URL_TAIL;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    private String generateGetScoreRequestURL(JSONObject getParams){
        String requestURL = "http://devostrum.no-ip.info:12345/score?";
        Iterator<String> jsonIterator = getParams.keys();
        try {
            while (jsonIterator.hasNext()) {
                String key = jsonIterator.next();
                requestURL += key + "=" + getParams.get(key);
                if (jsonIterator.hasNext()) {
                    requestURL += "&";
                }
            }
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return requestURL;
    }

    private void setScores(JSONObject jsonObjectScores){
        this.scores = new Scores(jsonObjectScores);
    }

    public void addEquipment(String type, int equipmentId){
        try {
            equipped.put(type,equipmentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Player(int id, String name, String username, String email){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        try {
            this.equipped.put("hat",1001112);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        level = 1;
        academicMilestone = 1;
        dietMilestone = 1;
        fitnessMilestone =1;
        mentalMilestone =1;
    }

    public Player(int id, String name, String email,
                  JSONArray playerActivities, JSONArray friends, JSONObject scores){
        this.id = id;
        this.name = name;
        this.email = email;
        setPlayerActivitiesJSON(playerActivities);
        setFriends(friends);
        setScores(scores);
    }

    class PlayerActivity {
        private int activityId;
        private String activityType;
        private int score;
        private String remarks;
        private Timestamp timestamp;

        public PlayerActivity(JSONObject jsonObject){
            try {
                this.activityId = jsonObject.getInt("id");
                this.activityType = jsonObject.getString("activityType");
                this.score = jsonObject.getInt("score");
                this.remarks = jsonObject.getString("remarks");

                String timestampStr = jsonObject.getString("timestamp");
                this.timestamp = Timestamp.valueOf(timestampStr);

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
        public String getRemarks() { return remarks; }
        public Timestamp getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return this.activityId + ": " + this.activityType + " Score: " + this.score + " Remarks: " + this.remarks;
        }
    }

    class Scores {
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

        @Override
        public String toString() {
            return "Fitness: " + this.fitness + " Academics: " + this.academics + " Mental Wellness: " + this.mentalWellness + " Diet: " + this.diet;
        }
    }

    public void updateEquipment(JSONObject equipped){
        this.equipped = equipped;
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
        updateScores(this.id);
    }

    public void updateScores(int userId) {
        String urlString = BASE_URL + "/score?userId=" + userId;
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

    public static Player getPlayerForId(int userId) {
        String urlString = BASE_URL + "/user?userId=" + userId;
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
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject JSONresponse = new JSONObject(response);
            boolean success = JSONresponse.getBoolean("success");
            if (success) {
                JSONObject user = JSONresponse.getJSONObject("user");
                String newName = user.getString("name");
                String newEmail = user.getString("email");
                int newId = Integer.valueOf(user.getString("id"));
                String newUsername = user.getString("username");

                return new Player(newId, newName, newUsername, newEmail);
            } else {
                String message = JSONresponse.getString("message");
                Log.i("Update", message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
