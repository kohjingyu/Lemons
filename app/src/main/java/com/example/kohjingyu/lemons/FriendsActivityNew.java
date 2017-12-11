package com.example.kohjingyu.lemons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FriendsActivityNew extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendsAdapter friendsAdapter;
    private Bitmap[] bitmaps;
    public final String BASE_URL = "http://devostrum.no-ip.info:12345";
    public final String MAPLE_URL_HEAD = "https://labs.maplestory.io/api/gms/latest/character/center/2000/20001,30037";
    public final String MAPLE_URL_TAIL = "/stand1?showears=false&resize=1";
    private JSONArray friendsJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_new);
        recyclerView = findViewById(R.id.friends_recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        JSONArray friendsList = new JSONArray();

        friendsJSONArray = Player.getPlayer().getFriends();
        Log.i("friends", friendsList.toString());
        bitmaps = generateNakedAvatars(friendsList.length());
        new GetAvatarTask().execute(friendsJSONArray);

        friendsAdapter = new FriendsAdapter(this, friendsList,bitmaps);

        recyclerView.setAdapter(friendsAdapter);

    }

    private Bitmap[] generateNakedAvatars(int numberOfAvatars){
        Bitmap[] bitmaps = new Bitmap[numberOfAvatars];
        for (int i = 0; i < numberOfAvatars;i++){
            bitmaps[i] = BitmapFactory.decodeResource(getResources(),R.drawable.avatar);
        }
        return bitmaps;
    }

    public void SearchFriends(View view) {
        EditText editText = findViewById(R.id.friends_editText);
        String searchTerm = editText.getText().toString().trim();
        Log.i("search",searchTerm);
        String finalURL = BASE_URL + "/user/search?searchTerm=" + searchTerm;
        Log.i("url", finalURL);
        URL url = null;
        try {
            url = new URL(finalURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        GetFriendsTask getFriendsTask = new GetFriendsTask();
        getFriendsTask.execute(url);
    }

    //AsyncTask to generate the avatar for each user
    public class GetAvatarTask extends AsyncTask<JSONArray, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(JSONArray... jsonArrays){
            //Generate the maple url by getting the equipment id from the server
            JSONArray friendsArray = jsonArrays[0];

            Bitmap[] avatars = new Bitmap[friendsArray.length()]; //Create new array of bitmap with length depending on the url
            int i = 0;
            while (i < friendsArray.length()) {
                try {
                    JSONObject obj = (JSONObject)friendsArray.get(i);
                    String userId = String.valueOf(obj.getInt("id"));
                    avatars[i] = Player.getAvatarForUser(userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i++;
                Log.i("I",String.valueOf(i) + " out of " + friendsArray.length());
            }
            return avatars;
        }

        @Override
        protected void onPostExecute(Bitmap[] avatars){
            friendsAdapter.update(friendsJSONArray,avatars);
            friendsAdapter.notifyDataSetChanged();
        }
    }

    //Async Task to get the arrays of users returned by the search from the server
    public class GetFriendsTask extends AsyncTask<URL, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... urls){
            String response = "";
            try {
                HttpURLConnection conn = (HttpURLConnection) urls[0].openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
//                    Log.i("Response", response);
                } else {
                    response = null;
                    Log.i("searchf HTTP", "Connection not successful");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Processing the JSON response. Update user scores if success, if not log the error message
            try {
                JSONObject JSONresponse = new JSONObject(response);
                boolean success = JSONresponse.getBoolean("success");
                if (success) {
                    friendsJSONArray = JSONresponse.getJSONArray("users");
                } else {
                    String message = JSONresponse.getString("message");
                    Log.i("Scores", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return friendsJSONArray;
        }

        @Override
        protected void onPostExecute(JSONArray friendList){
            Bitmap[] tempAvatars = generateNakedAvatars(friendList.length());
            // update the adapter with the user info first while getting the images
            friendsAdapter.update(friendList,tempAvatars);
            friendsAdapter.notifyDataSetChanged();

            //Get the avatar pictures since we have the equipment id now
            GetAvatarTask getAvatarTask = new GetAvatarTask();
            getAvatarTask.execute(friendList);
        }
    }
}
