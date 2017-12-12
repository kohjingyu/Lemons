package com.example.kohjingyu.lemons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.friends_recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        JSONArray jsonArray = new JSONArray();

        LeaderboardActivity.GetLeaderboardTask getFriendsTask = new LeaderboardActivity.GetLeaderboardTask();
        getFriendsTask.execute();

        // Just set empty array, fill it later
        friendsAdapter = new LeaderboardAdapter(this, jsonArray, new Bitmap[1]);
        recyclerView.setAdapter(friendsAdapter);
    }

    private RecyclerView recyclerView;
    private LeaderboardAdapter friendsAdapter;
    public final String BASE_URL = "http://devostrum.no-ip.info:12345";
    private JSONArray leaderboardJSONArray;
    private boolean global = true;

    private Bitmap[] generateNakedAvatars(int numberOfAvatars){
        Bitmap[] bitmaps = new Bitmap[numberOfAvatars];
        for (int i = 0; i < numberOfAvatars;i++){
            bitmaps[i] = BitmapFactory.decodeResource(getResources(),R.drawable.avatar);
        }
        return bitmaps;
    }

    public void changeLeaderboard(View view) {
        Button button = view.findViewById(R.id.leaderboardButton);
        if (global){
            button.setText("friends");
            global = false;
            LeaderboardActivity.GetFriendsLeaderboardTask getFriendsTask = new LeaderboardActivity.GetFriendsLeaderboardTask();
            getFriendsTask.execute();

        } else {
            button.setText("global");
            global = true;
            LeaderboardActivity.GetLeaderboardTask getFriendsTask = new LeaderboardActivity.GetLeaderboardTask();
            getFriendsTask.execute();
        }
    }

    public class GetAvatarTask extends AsyncTask<JSONArray, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(JSONArray... jsonArrays){
            JSONArray leaderboardList = jsonArrays[0];

            Bitmap[] avatars = new Bitmap[leaderboardList.length()]; //Create new array of bitmap with length depending on the url
            int i = 0;
            while (i < avatars.length) {
                JSONObject temp = null; //each friend jsonobject containing userid
                try {
                    temp = leaderboardList.getJSONObject(i);
                    String userId = String.valueOf(temp.getInt("id"));
                    avatars[i] = Player.getAvatarForUser(userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                i++;
                Log.i("I",String.valueOf(i) + " out of " + leaderboardList.length());
            }
            return avatars;
        }

        @Override
        protected void onPostExecute(Bitmap[] avatars){
            friendsAdapter.update(leaderboardJSONArray,avatars);
            friendsAdapter.notifyDataSetChanged();
        }
    }

    public class GetLeaderboardTask extends AsyncTask<Integer, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Integer... params){
            String response = "";
            try {
                URL url = new URL(BASE_URL + "/leaderboard");
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
                    Log.i("Response", response);
                } else {
                    response = null;
                    Log.i("HTTP", "Connection not successful");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Processing the JSON response. Update user scores if success, if not log the error message
            try {
                JSONObject JSONresponse = new JSONObject(response);
                boolean success = JSONresponse.getBoolean("success");
                if (success) {
                    leaderboardJSONArray = JSONresponse.getJSONArray("leaderboard");
                } else {
                    String message = JSONresponse.getString("message");
                    Log.i("Leaderboard", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return leaderboardJSONArray;
        }

        @Override
        protected void onPostExecute(JSONArray friendList){
            Bitmap[] tempAvatars = generateNakedAvatars(friendList.length());
            friendsAdapter.update(friendList,tempAvatars);
            friendsAdapter.notifyDataSetChanged();

            LeaderboardActivity.GetAvatarTask getAvatarTask = new LeaderboardActivity.GetAvatarTask();
            getAvatarTask.execute(friendList);
        }
    }

    public class GetFriendsLeaderboardTask extends AsyncTask<Integer, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Integer... params){
            String response = "";
            try {
                int userId = Player.getPlayer().getId();
                URL url = new URL(BASE_URL + "/friend?userId=" + userId);

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
                    Log.i("Response", response);
                } else {
                    response = null;
                    Log.i("HTTP", "Connection not successful");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Processing the JSON response. Update user scores if success, if not log the error message
            try {
                JSONObject JSONresponse = new JSONObject(response);
                boolean success = JSONresponse.getBoolean("success");
                if (success) {
                    leaderboardJSONArray = JSONresponse.getJSONArray("friends");
                } else {
                    String message = JSONresponse.getString("message");
                    Log.i("Leaderboard", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return leaderboardJSONArray;
        }

        @Override
        protected void onPostExecute(JSONArray friendList){
            Bitmap[] tempAvatars = generateNakedAvatars(friendList.length());
            friendsAdapter.update(friendList,tempAvatars);
            friendsAdapter.notifyDataSetChanged();

            LeaderboardActivity.GetAvatarTask getAvatarTask = new LeaderboardActivity.GetAvatarTask();
            getAvatarTask.execute(friendList);
        }
    }
}
