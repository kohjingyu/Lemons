package com.example.kohjingyu.lemons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        int id = intent.getIntExtra(FriendsAdapter.putExtraKey, Player.getPlayer().getId());

        StatsFragment statsFragment = StatsFragment.newInstance(id);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.statsFragmentContainer, statsFragment, "statsFragment")
                .commit();
    }


}
