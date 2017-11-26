package com.example.kohjingyu.lemons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kohjingyu.lemons.shop.ShopActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static JSONObject userInfo = null;

    TextView userNameText;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String name = "";

        try {
            name = (String)userInfo.get("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        userNameText = (TextView)findViewById(R.id.userNameText);
        userNameText.setText(name);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View view) {
        // TODO: Implement logout
        Log.i("Lemons", "Logging out...");
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
        Intent intent = new Intent(AccountActivity.this, FriendsActivity.class);
        AccountActivity.this.startActivity(intent);
    }
}
