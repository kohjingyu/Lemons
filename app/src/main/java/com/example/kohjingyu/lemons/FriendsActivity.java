package com.example.kohjingyu.lemons;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Get SearchView and set searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.friends_searchView);

        //Set the searchview searchable
        searchView  .setSearchableInfo(searchManager
                    .getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
    }
}
