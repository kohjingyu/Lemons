package com.example.kohjingyu.lemons;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        System.out.println(userId);
        GetDataFromServerTask getDataFromServerTask = new GetDataFromServerTask();
        getDataFromServerTask.execute(userId);
    }

    private String generateGetRequestURL(JSONObject getParams){
        String requestURL = "http://devostrum.no-ip.info:12345/activity?";
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

    private class GetDataFromServerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                JSONObject getParams = new JSONObject();
                getParams.put("userId", strings[0]);
                String requestURL = generateGetRequestURL(getParams);
                String response = LoginActivity.performGetCall(requestURL, getParams);
                JSONObject jsonObj = new JSONObject(response);
                boolean success = (boolean) jsonObj.get("success");

                if (success) { //if success, set userobject to the user data
                    System.out.println(jsonObj);
                    return true;
                } else {
                    //TODO set textview to display message
                    String errorMessage = (String) jsonObj.get("message");
                    System.out.println(errorMessage);
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
                // TODO: Update UI
            }
        }
    }
}
