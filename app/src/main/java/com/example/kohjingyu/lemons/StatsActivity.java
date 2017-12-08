package com.example.kohjingyu.lemons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kohjingyu.lemons.LoginActivity.performPostCall;

public class StatsActivity extends AppCompatActivity {

    public static final String key = "StatsActivityMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        Intent intent = getIntent();
        int id = intent.getIntExtra(FriendsAdapter.putExtraKey, Player.getPlayer().getId());

        // Load stats for current player
        StatsFragment statsFragment = StatsFragment.newInstance(id);
//        StatsFragment statsFragment = StatsFragment.newInstance(getIntent().getIntExtra("id", Player.getPlayer().getId()));

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.statsFragmentContainer, statsFragment, "statsFragment")
                .commit();
    }


}
