package com.example.kohjingyu.lemons;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kohjingyu.lemons.shop.ShopActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountActivity extends AppCompatActivity {
    TextView userNameText;
    ImageView avatarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        String name = Player.getPlayer().getName();

        userNameText = (TextView)findViewById(R.id.username);
        userNameText.setText(name);

        avatarImage = (ImageView)findViewById(R.id.avatarImage);

        GetAvatarTask getAvatarTask = new GetAvatarTask();
        getAvatarTask.execute(String.valueOf(Player.getPlayer().getId()));
    }

    @Override
    protected void onResume(){
        super.onResume();
        GetAvatarTask getAvatarTask = new GetAvatarTask();
        getAvatarTask.execute(String.valueOf(Player.getPlayer().getId()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetAvatarTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params){
            Bitmap avatar = Player.getPlayer().getAvatar();
            Player.getPlayer().updateFriends();
            return avatar;
        }

        @Override
        protected void onPostExecute(Bitmap avatar){
            if(avatar != null) {
                avatarImage.setImageBitmap(avatar);
            }
        }
    }

    public void logout(View view) {
        Log.i("Lemons", "Logging out...");

        // Set Player singleton to null (logout)
        Player.setPlayer(null);

        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        AccountActivity.this.startActivity(intent);
    }

    public void loadStats(View view) {
        Log.i("Lemons", "Loading stats");
        Intent intent = new Intent(AccountActivity.this, StatsActivity.class);
        AccountActivity.this.startActivity(intent);
    }

    public void loadShop(View view) {
        Log.i("Lemons", "Loading shop");
        Intent intent = new Intent(AccountActivity.this, ShopActivity.class);
        AccountActivity.this.startActivity(intent);
    }

    public void loadLeaderboard(View view) {
        Log.i("Lemons", "Loading leaderboard");
        Intent intent = new Intent(AccountActivity.this, LeaderboardActivity.class);
        AccountActivity.this.startActivity(intent);
    }

    public void loadFriends(View view) {
        Log.i("Lemons", "Loading friends");
        Intent intent = new Intent(AccountActivity.this, FriendsActivityNew.class);
        AccountActivity.this.startActivity(intent);
    }

    public void loadLog(View view) {
        Log.i("Lemons", "Loading log");
        Intent intent = new Intent(AccountActivity.this, LogActivity.class);
        AccountActivity.this.startActivity(intent);
    }
}
