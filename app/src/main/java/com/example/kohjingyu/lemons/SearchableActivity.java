package com.example.kohjingyu.lemons;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/*
Activity for searching
 */

public class SearchableActivity extends ListActivity {
    final String query_BASE = "devostrum.no-ip.info:12345";
    final String query_BODY = "user";
    final String query_SCHEME = "http";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            getDataFromServer(query);
        }


    }

    private URL buildUrl(String userId){ //build URL to search the database for user
        URL searchURL = null;
        Uri.Builder builder = new Uri.Builder();
        userId.trim();
        builder.scheme(query_SCHEME)
                .encodedAuthority(query_BASE)
                .appendPath(query_BODY)
                .appendQueryParameter("userId", userId);

        Uri uri = builder.build();
        try {
            searchURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return searchURL;
    }

    //This method return the json string when given the server url
    private static String getHttpURL(URL url){
        InputStream inputStream;
        String line;
        String JSONoutput = "";
        //TO DO 3.1 query the API with a URL
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //open connection with server
            conn.connect(); //connect to the server
            int response = conn.getResponseCode(); //get the response to query
            Log.i("URLResponse", "the response is " + response); //log it to the logcat
            inputStream = conn.getInputStream();    //get input stream from server

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line = reader.readLine()) != null){ //Write the JSON into a string
                JSONoutput =  JSONoutput + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JSONoutput;
    }

    private void getDataFromServer(String query){
        URL requestURL = buildUrl(query);

        GetDataFromServerTask getDataFromServerTask;


        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        if (isWifi){
            Log.i("WIFI", "here");
            getDataFromServerTask = new GetDataFromServerTask();
            getDataFromServerTask.execute(requestURL); //To use the non-static method execute, intialise an instance of the static class
        } else {
            Toast.makeText(this,"not on wifi connection", Toast.LENGTH_SHORT).show();
        }


    }

    private static class GetDataFromServerTask extends AsyncTask<URL, Void, String>{


        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            Log.i("URL", url.toString());
            String JsonResultString = getHttpURL(url);
            return JsonResultString;
        }

        @Override
        protected void onPostExecute(String jsonResultString){
            Log.i("JSON", jsonResultString);
        }
    }
}
