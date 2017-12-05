package com.example.kohjingyu.lemons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivityNew extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_new);
        recyclerView = findViewById(R.id.friends_recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject1.put("name", "Vincent");
            jsonObject1.put("username", "vincentcent");
            jsonObject2.put("name", "Vincent Setiawan");
            jsonObject2.put("username", "vincentcentcent");
            jsonArray.put(jsonObject1);
            jsonArray.put(jsonObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap[] bitmaps = new Bitmap[2];
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.placeholder);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.placeholder);
        bitmaps[0] = bitmap1;
        bitmaps[1] = bitmap2;

        FriendsAdapter friendsAdapter = new FriendsAdapter(this, jsonArray,bitmaps);

        recyclerView.setAdapter(friendsAdapter);

    }
}
