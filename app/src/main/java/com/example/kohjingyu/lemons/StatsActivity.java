package com.example.kohjingyu.lemons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Load stats for current player
        StatsFragment statsFragment = StatsFragment.newInstance(Player.getPlayer().getId());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.statsFragmentContainer, statsFragment, "statsFragment")
                .commit();
    }
}
