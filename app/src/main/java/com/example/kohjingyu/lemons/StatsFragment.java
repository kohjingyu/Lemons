package com.example.kohjingyu.lemons;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
        // Inflate the layout for this fragment
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

            JSONArray friendList = Player.getPlayer().getFriends();

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
        }
        return view;
    }

    public void updateStats() {
        Player.Scores scores = player.getScores();
        System.out.println(scores);

        usernameText.setText(player.getName());

        int academicScoreRaw = scores.getAcademics();
        int academicMax = 100;
        int academicScoreAdjusted = academicScoreRaw % academicMax;
        int academicQuotient = academicScoreRaw / academicMax;
        academicProgress.setMax(academicMax);
        academicProgress.setProgress(academicScoreAdjusted);
        academicProgressText.setText(academicScoreAdjusted + "/" + academicMax);

        int fitnessScoreRaw = scores.getFitness();
        int fitnessMax = 100;
        int fitnessScoreAdjusted = fitnessScoreRaw%fitnessMax;
        int fitnessQuotient = fitnessScoreRaw/fitnessMax;
        fitnessProgress.setMax(fitnessMax);
        fitnessProgress.setProgress(fitnessScoreAdjusted);
        fitnessProgressText.setText(fitnessScoreAdjusted + "/" + fitnessMax);

        int dietScoreRaw = scores.getDiet();
        int dietMax = 100;
        int dietScoreAdjusted = dietScoreRaw%dietMax;
        int dietQuotient = dietScoreRaw/dietMax;
        dietProgress.setMax(dietMax);
        dietProgress.setProgress(dietScoreAdjusted);
        dietProgressText.setText(dietScoreAdjusted + "/" + dietMax);

        int mentalScoreRaw = scores.getMentalWellness();
        int mentalMax = 100;
        int mentalScoreAdjusted = mentalScoreRaw % mentalMax;
        int mentalQuotient = mentalScoreRaw/mentalMax;
        mentalProgress.setMax(mentalMax);
        mentalProgress.setProgress(mentalScoreAdjusted);
        mentalProgressText.setText(mentalScoreAdjusted + "/" + mentalMax);


        int level = (academicQuotient + dietQuotient + fitnessQuotient + mentalQuotient)/4;
        String levelString = String.format("Level %s", (1+level));
        userLevel.setText(levelString);

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
                String response = performPostCall("http://devostrum.no-ip.info:12345/friend", postParams);

                JSONObject jsonObj = new JSONObject(response);
                boolean success = (boolean)jsonObj.get("success");

                if(success) {
                    Log.i("add friend", "player accepted stranger's friend request (requester = stranger, requestee = player)");
                    return true;
                }
                else {
                    String errorMessage = (String)jsonObj.get("message");
                    System.out.println(errorMessage);
                }

                requesterID = Player.getPlayer().getId();
                requesteeID = userId;

                postParams = new JSONObject();
                postParams.put("requester", requesterID);
                postParams.put("requestee", requesteeID);
                response = performPostCall("http://devostrum.no-ip.info:12345/friend", postParams);

                jsonObj = new JSONObject(response);
                success = (boolean)jsonObj.get("success");

                if(success) {
                    Log.i("add friend", "stranger accepted player's friend request (requester = player, requestee = player)");
                    Toast.makeText(getActivity(), "Added friend!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else {
                    String errorMessage = (String)jsonObj.get("message");
                    System.out.println(errorMessage);
                }
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean success){
            Log.i("Angelia","here");
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
        return jsonArray.toString().contains("\"id\":\""+idToFind+"\"");
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
