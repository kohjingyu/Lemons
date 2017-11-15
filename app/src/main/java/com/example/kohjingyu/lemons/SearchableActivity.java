package com.example.kohjingyu.lemons;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SearchView;

public class SearchableActivity extends ListActivity {
    final String query_BASE = "";
    final String query_TAIL = "";
    final String query_SCHEME = "https";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.friends_searchView);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            buildUrl(query);
        }


    }

    public void buildUrl(String username){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(query_SCHEME)
                .authority(query_BASE)
                .appendPath(username)
                .appendPath(query_TAIL);

        Uri uri = builder.build();

    }
}
