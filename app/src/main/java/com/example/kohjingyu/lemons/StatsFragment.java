package com.example.kohjingyu.lemons;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String LEVEL = "Level";
    private static final String ACADEMICS = "Academics";
    private static final String FITNESS = "Fitness";
    private static final String DIET = "Diet";
    private static final String MENTAL = "Mental Wellness";

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

        for(int i = 0; i < friendsJSONArray.length(); i ++) {
            try {
                JSONObject obj = friendsJSONArray.getJSONObject(i);
                int id = obj.getInt("id");

                // Check if they are already friends
                if(id == userId) {
                    addFriendButton.setVisibility(View.VISIBLE);
                    addFriendButton.setEnabled(false);
                    addFriendButton.setText("Already friends");
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
            addFriendButton.setVisibility(View.VISIBLE);
            addFriendButton.setEnabled(true);

        }
        return view;
    }

    public void updateStats() {
        usernameText.setText(player.getName());

        Player.Scores scores = player.getScores();
        int academicScoreRaw = scores.getAcademics();
        int fitnessScoreRaw = scores.getFitness();
        int dietScoreRaw = scores.getDiet();
        int mentalScoreRaw = scores.getMentalWellness();

        boolean bool = (userId == Player.getPlayer().getId());
        int academicMilestone = statsManager(bool, ACADEMICS, academicScoreRaw, academicProgress, academicProgressText, academicLevel);
        int fitnessMilestone = statsManager(bool, FITNESS, fitnessScoreRaw, fitnessProgress, fitnessProgressText, fitnessLevel);
        int dietMilestone = statsManager(bool, DIET, dietScoreRaw, dietProgress, dietProgressText, dietLevel);
        int mentalMilestone = statsManager(bool, MENTAL, mentalScoreRaw, mentalProgress, mentalProgressText, mentalLevel);

        int tempLevel = (academicMilestone + fitnessMilestone + dietMilestone + mentalMilestone)/4;

        if(bool){
            SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
            int previousLevel = pref.getInt(LEVEL, 1);
            if(tempLevel>previousLevel){
                LevelUpAlertDialogFragment alertDialogFragment = new LevelUpAlertDialogFragment();
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                alertDialogFragment.show(fm, "level up alert dialog");
                SharedPreferences.Editor edt = pref.edit();
                edt.putInt(LEVEL, tempLevel).commit();
            }
        }

        String levelString = String.format("Level %s", tempLevel+1); //add one for displayed level because starting level is 1
        userLevel.setText(levelString);

    }

    public int statsManager(boolean bool, String category, int rawScore, ProgressBar progressBar, TextView textView, TextView categoryLevel){
        int maxScore = 100;
        int adjustedScore = rawScore%maxScore;
        int milestone = rawScore/maxScore;
        progressBar.setMax(maxScore);
        progressBar.setProgress(adjustedScore);

        String progress = adjustedScore + "/" + maxScore;
        textView.setText(progress);

        String categoryLevelMessage = String.format("%s (Level %s): ", category, milestone+1);
        categoryLevel.setText(categoryLevelMessage);

        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        int previousMilestone = pref.getInt(category, 1);

        if(bool){
            if(milestone>previousMilestone){
                String message = String.format("Congratulations! Reached new Milestone for %s!", category.toUpperCase());
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setMessage(message)
                        .setCancelable(true)
                        .create()
                        .show();
                if (category.equals(ACADEMICS)) edt.putInt(ACADEMICS, milestone).commit();
                if (category.equals(FITNESS)) edt.putInt(FITNESS, milestone).commit();
                if (category.equals(DIET)) edt.putInt(DIET, milestone).commit();
                if (category.equals(MENTAL)) edt.putInt(MENTAL, milestone).commit();

            }
        }

        return milestone;

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
            int requesterID = userId;
            int requesteeID = Player.getPlayer().getId();

            try {
                JSONObject postParams = new JSONObject();
                postParams.put("requester", requesterID);
                postParams.put("requestee", requesteeID);
                try{
                    String response = performPostCall("http://devostrum.no-ip.info:12345/friend", postParams);
                    JSONObject jsonObj = new JSONObject(response);
                    boolean success = (boolean)jsonObj.get("success");
                    Log.i("add friend?", "response: "+ response);
                    Log.i("add friend?", "success: "+ success);

                    if(success) {
                        return true;
                    }
                    else {
                        String errorMessage = (String)jsonObj.get("message");
                        System.out.println(errorMessage);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
                Toast.makeText(getActivity(), "Added friend!", Toast.LENGTH_SHORT).show();
            }
            new UpdateFriendList().execute();
        }
    }

    private class UpdateFriendList extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            try{
                Player.getPlayer().updateFriends();
                return true;
            }catch (Exception ex){
                ex.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.i("update friends", success.toString());
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
            // params[0] is the id for avatar
            return Player.getAvatarForUser(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap avatar){
            avatarImage.setImageBitmap(avatar);
        }
    }

}
