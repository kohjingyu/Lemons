package com.example.kohjingyu.lemons;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        int userId = Player.getPlayer().getId();
        GetDataFromServerTask getDataFromServerTask = new GetDataFromServerTask();
        getDataFromServerTask.execute(userId);
    }

    public void updateStats() {
        Player.Scores scores = Player.getPlayer().getScores();
        System.out.println(scores);
    }

    private String generateGetRequestURL(String requestURL, JSONObject getParams){
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

    private class GetDataFromServerTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                JSONObject getParams = new JSONObject();
                getParams.put("userId", params[0]);

                String requestURL = generateGetRequestURL("http://devostrum.no-ip.info:12345/score?", getParams);
                String response = LoginActivity.performGetCall(requestURL, getParams);
                JSONObject jsonObj = new JSONObject(response);
                boolean success = (boolean) jsonObj.get("success");

                if (success) {
                    JSONObject userScores = jsonObj.getJSONObject("user");
                    Player.getPlayer().setScores(userScores);


                    return true;
                } else {
                    String errorMessage = (String) jsonObj.get("message");
                    Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                updateStats();
            }
        }
    }
}
