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

        // Update the player scores
        UpdateScores updateScoresTask = new UpdateScores();
        updateScoresTask.execute(0);
    }

    public void updateStats() {
        Player.Scores scores = Player.getPlayer().getScores();
        System.out.println(scores);
    }

    private class UpdateScores extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            Player.getPlayer().updateScores();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                updateStats();
            }
        }
    }
}
