package com.example.kohjingyu.lemons;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ActivityLogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        recyclerView = findViewById(R.id.friends_recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Player.PlayerActivity> playerActivities = new ArrayList<>();
        UpdateActivityLog updateActivityLog = new UpdateActivityLog();
        updateActivityLog.execute();

        logAdapter = new ActivityLogAdapter(this, playerActivities);
        recyclerView.setAdapter(logAdapter);
    }

    public class UpdateActivityLog extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params){
            Player.getPlayer().updateActivities();
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            logAdapter.update(Player.getPlayer().getPlayerActivities());
            logAdapter.notifyDataSetChanged();
        }
    }
}
