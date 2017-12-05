package com.example.kohjingyu.lemons;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
/*
Activity for searching
 */

public class SearchableActivity extends ListActivity {
    JSONArray userObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_searchable);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            GetDataFromServerTask getDataFromServerTask = new GetDataFromServerTask();
            getDataFromServerTask.execute(query);
        }
    }


    //This method return the json string when given the server url
//    private void getDataFromServer(String query){
//        URL requestURL = buildUrl(query);
//
//        GetDataFromServerTask getDataFromServerTask;
//
//
//        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//        boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
//
//        if (true){
//            Log.i("WIFI", "here");
//            getDataFromServerTask = new GetDataFromServerTask();
//            getDataFromServerTask.execute(requestURL); //To use the non-static method execute, intialise an instance of the static class
//        } else {
//            Toast.makeText(this,"not on wifi connection", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


    private ArrayList<String> generateJsonArray(JSONArray jsonArray){

        ArrayList<String> stringArrayList = new ArrayList<>();

        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
        int counter = 0;
        while (counter < jsonArray.length()){
            try {
                JSONObject temp = jsonArray.getJSONObject(counter);
                String information = temp.getString("name") + "\n" + temp.getString("username") + "\n" + temp.getString("email");
                stringArrayList.add(information);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            counter++;
        }

        return stringArrayList;

    }

    private String generateGetRequestURL(JSONObject getParams){
        String requestURL = "http://devostrum.no-ip.info:12345/user/search?";
        Iterator<String> jsonIterator = getParams.keys();
        try {
            while (jsonIterator.hasNext()) {
                String key = jsonIterator.next();
                requestURL += key + "=" + getParams.get(key);
                if (jsonIterator.hasNext()) {
                    requestURL += "&";
                }
            }
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return requestURL;
    }

    private class GetDataFromServerTask extends AsyncTask<String, Void, Boolean>{


        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                JSONObject getParams = new JSONObject();
                getParams.put("searchTerm",strings[0]);
                String requestURL = generateGetRequestURL(getParams);
                String response = LoginActivity.performGetCall(requestURL, getParams);
                Log.i("url", requestURL);
                Log.i("login", response);
                JSONObject jsonObj = new JSONObject(response);
                boolean success = (boolean)jsonObj.get("success");

                if(success) { //if success, set userobject to the user data
                    userObj = (JSONArray) jsonObj.get("users");
//                    System.out.println("ID: " + userObj.get("id"));
                    return true;
                }
                else {
                    //TODO set textview to display message
                    String errorMessage = (String)jsonObj.get("message");
                    System.out.println(errorMessage);
                    return false;
                }
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean success){
            if (success){
                ArrayList<String> stringArrayList = generateJsonArray(userObj);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_list_item_1, stringArrayList);
                setListAdapter(adapter);

            }
        }
    }
}
