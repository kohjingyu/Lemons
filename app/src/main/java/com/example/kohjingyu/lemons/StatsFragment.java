package com.example.kohjingyu.lemons;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.example.kohjingyu.lemons.LoginActivity.performPostCall;
import static com.example.kohjingyu.lemons.Player.ACADEMICS;
import static com.example.kohjingyu.lemons.Player.DIET;
import static com.example.kohjingyu.lemons.Player.FITNESS;
import static com.example.kohjingyu.lemons.Player.MENTAL;
import static com.example.kohjingyu.lemons.Player.getPlayer;


public class StatsFragment extends Fragment {

    TextView academicLevel;
    TextView fitnessLevel;
    TextView dietLevel;
    TextView mentalLevel;

    ProgressBar academicProgress;
    TextView academicProgressText;
    ProgressBar fitnessProgress;
    TextView fitnessProgressText;
    ProgressBar dietProgress;
    TextView dietProgressText;
    ProgressBar mentalProgress;
    TextView mentalProgressText;

    ImageView avatarImage;

    TextView usernameText;
    TextView userLevel;

    Button addFriendButton;

    int userId;
    Player player;

    JSONArray friendsJSONArray;


    public static final String key = "StatsFragmentMessage";

    public static StatsFragment newInstance(int userId) {
        StatsFragment statsFragment = new StatsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        statsFragment.setArguments(bundle);
        return statsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        academicProgress = view.findViewById(R.id.academicProgress);
        academicProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#06BA63")));
        academicProgress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EBEBEB")));
        academicProgressText = view.findViewById(R.id.academicProgressText);

        fitnessProgress = view.findViewById(R.id.fitnessProgress);
        fitnessProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ED8B14")));
        fitnessProgress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EBEBEB")));
        fitnessProgressText = view.findViewById(R.id.fitnessProgressText);

        dietProgress = view.findViewById(R.id.dietProgress);
        dietProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#CF2522")));
        dietProgress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EBEBEB")));
        dietProgressText = view.findViewById(R.id.dietProgressText);

        mentalProgress = view.findViewById(R.id.mentalProgress);
        mentalProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#1196E9")));
        mentalProgress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EBEBEB")));
        mentalProgressText = view.findViewById(R.id.mentalProgressText);

        usernameText = view.findViewById(R.id.usernameText);
        userLevel = view.findViewById(R.id.userLevel);

        academicLevel = view.findViewById(R.id.academicLevel);
        fitnessLevel = view.findViewById(R.id.fitnessLevel);
        dietLevel = view.findViewById(R.id.dietLevel);
        mentalLevel = view.findViewById(R.id.mentalLevel);

        addFriendButton = view.findViewById(R.id.addfriendbutton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //TODO check db
                addFriend addfriend = new addFriend();
                addfriend.execute();
            }
        });

        avatarImage = view.findViewById(R.id.avatarImage);

        Bundle bundle = this.getArguments();
        userId = bundle.getInt("userId");
        System.out.println(userId);

        GetAvatarTask getAvatarTask = new GetAvatarTask();
        getAvatarTask.execute(String.valueOf(userId));

        // Check if they are already friends
        friendsJSONArray = Player.getPlayer().getFriends();
        System.out.println("Friends: " + friendsJSONArray);

        for(int i = 0; i < friendsJSONArray.length(); i ++) {
            try {
                JSONObject obj = friendsJSONArray.getJSONObject(i);
                int id = obj.getInt("id");

                // Check if they are already friends
                if(id == userId) {
                    addFriendButton.setVisibility(View.INVISIBLE);
                    addFriendButton.setEnabled(false);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(userId == Player.getPlayer().getId()) {
            player = Player.getPlayer();
            // Update the player scores
            UpdateScores updateScoresTask = new UpdateScores();
            updateScoresTask.execute(0);
            addFriendButton.setVisibility(View.INVISIBLE);
            addFriendButton.setEnabled(false);

        }
        else {
            System.out.println("getting info");
            GetPlayerInfo getInfo = new GetPlayerInfo();
            getInfo.execute(userId);

            try{
                Player.getPlayer().updateFriends();
                JSONArray friendList = Player.getPlayer().getFriends();
                Log.i("add friend", "friend list: "+ friendList.toString());

                boolean alreadyFriend = false;
                if(friendList!=null){
                    alreadyFriend = userExists(friendList, userId);
                }

                if(alreadyFriend){
                    addFriendButton.setVisibility(View.VISIBLE);
                    addFriendButton.setEnabled(false);
                    addFriendButton.setText("Already friend");
                }else{
                    addFriendButton.setVisibility(View.VISIBLE);
                    addFriendButton.setEnabled(true);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
        return view;
    }

    public void updateStats() {
        Player.Scores scores = player.getScores();
        System.out.println(scores);

        usernameText.setText(player.getName());

        int academicScoreRaw = scores.getAcademics();
        int fitnessScoreRaw = scores.getFitness();
        int dietScoreRaw = scores.getDiet();
        int mentalScoreRaw = scores.getMentalWellness();

        boolean bool = (userId == Player.getPlayer().getId());
        statsManager(bool, ACADEMICS, academicScoreRaw, player.getAcademicMilestone(), academicProgress, academicProgressText, academicLevel);
        statsManager(bool, FITNESS, fitnessScoreRaw, player.getFitnessMilestone(), fitnessProgress, fitnessProgressText, fitnessLevel);
        statsManager(bool, DIET, dietScoreRaw, player.getDietMilestone(), dietProgress, dietProgressText, dietLevel);
        statsManager(bool, MENTAL, mentalScoreRaw, player.getMentalMilestone(), mentalProgress, mentalProgressText, mentalLevel);

        int tempLevel = (player.getAcademicMilestone() + player.getFitnessMilestone() + player.getMentalMilestone() + player.getDietMilestone())/4;
        if(bool){
            if(tempLevel>player.getLevel()){
                LevelUpAlertDialogFragment alertDialogFragment = new LevelUpAlertDialogFragment();
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                alertDialogFragment.show(fm, "level up alert dialog");
                player.setLevel(tempLevel);
            }
        }

        String levelString = String.format("Level %s", (1+Player.getPlayer().getLevel()));
        userLevel.setText(levelString);

    }

    public void statsManager(boolean bool, String category, int rawScore, int previousMilestone, ProgressBar progressBar, TextView textView, TextView categoryLevel){
        //TODO after i visit another stats profile the dialog and toasts appear
        int max = 100;
        int adjustedScore = rawScore%max;
        int milestone = rawScore/max;
        progressBar.setMax(max);
        progressBar.setProgress(adjustedScore);
        String progress = adjustedScore + "/" + max;
        textView.setText(progress);
        String categoryLevelMessage = String.format("%s (Level %s): ", category, milestone+1);
        categoryLevel.setText(categoryLevelMessage);

        if(bool){
            if(milestone>previousMilestone){
                String message = String.format("Congratulations! Reached new Milestone for %s!", category.toUpperCase());
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setMessage(message)
                        .setCancelable(true)
                        .create()
                        .show();
                if (category.equals(ACADEMICS)) player.setAcademicMilestone(milestone);
                if (category.equals(FITNESS)) player.setFitnessMilestone(milestone);
                if (category.equals(DIET)) player.setDietMilestone(milestone);
                if (category.equals(MENTAL)) player.setMentalMilestone(milestone);

            }
        }

    }

    private class UpdateScores extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            player.updateScores();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                updateStats();
            }
        }
    }


    private class addFriend extends AsyncTask<Integer, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Integer... integers) {
            //check if friend alr
            JSONArray friendsList = Player.getPlayer().getFriends();


            int requesterID = userId;
            int requesteeID = Player.getPlayer().getId();

            try {
                Log.i("add friend", "requester: " + requesterID);
                Log.i("add friend", "requestee: " + requesteeID);
                JSONObject postParams = new JSONObject();
                postParams.put("requester", requesterID);
                postParams.put("requestee", requesteeID);
                try{
                    String response = performPostCall("http://devostrum.no-ip.info:12345/friend", postParams);
                    JSONObject jsonObj = new JSONObject(response);
                    boolean success = (boolean)jsonObj.get("success");

                    if(success) {
//                        Log.i("add friend", "player accepted stranger's friend request (requester = stranger, requestee = player)");
                        return true;
                    }
                    else {
                        String errorMessage = (String)jsonObj.get("message");
                        System.out.println(errorMessage);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                requesterID = Player.getPlayer().getId();
                requesteeID = userId;

                postParams = new JSONObject();
                postParams.put("requester", requesterID);
                postParams.put("requestee", requesteeID);

                try{
                    String response = performPostCall("http://devostrum.no-ip.info:12345/friend", postParams);
                    JSONObject jsonObj = new JSONObject(response);
                    boolean success = (boolean)jsonObj.get("success");

                    if(success) {
//                        Log.i("add friend", "stranger accepted player's friend request (requester = player, requestee = player)");
                        return true;
                    }
                    else {
                        String errorMessage = (String)jsonObj.get("message");
                        System.out.println(errorMessage);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                Player.getPlayer().updateFriends();
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean success){
            Log.i("Angelia","here");
            Toast.makeText(getActivity(), "Added friend!", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetPlayerInfo extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            player = Player.getPlayerForId(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Update the player scores
                UpdateScores updateScoresTask = new UpdateScores();
                updateScoresTask.execute(0);
            }
        }
    }

    private boolean userExists(JSONArray jsonArray, int idToFind){
        if(jsonArray!= null){
            return jsonArray.toString().contains("\"id\":\""+idToFind+"\"");
        } else return false;
    }


    public class GetAvatarTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params){
            URL avatarURL = AccountActivity.generateAvatarURL(params[0]);
            Bitmap avatar = null;

            try {
                if(avatarURL != null) {
                    Log.i("URL",avatarURL.toString());
                    InputStream in = avatarURL.openStream();
                    avatar = BitmapFactory.decodeStream(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return avatar;
        }

        @Override
        protected void onPostExecute(Bitmap avatar){
            avatarImage.setImageBitmap(avatar);
        }
    }
}
