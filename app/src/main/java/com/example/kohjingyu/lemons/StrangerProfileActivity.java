package com.example.kohjingyu.lemons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StrangerProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stranger_profile);

        Intent intent = getIntent();
        int id = intent.getIntExtra(FriendsAdapter.putExtraKey, 1);

        // Load stats for stranger
        StatsFragment statsFragment = StatsFragment.newInstance(id);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.statsFragmentContainer, statsFragment, "statsFragment")
                .commit();
    }
}
