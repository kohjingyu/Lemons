package com.example.kohjingyu.lemons;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StatsFragment extends Fragment {

    ProgressBar academicProgress;
    TextView academicProgressText;
    ProgressBar fitnessProgress;
    TextView fitnessProgressText;
    ProgressBar dietProgress;
    TextView dietProgressText;
    ProgressBar mentalProgress;
    TextView mentalProgressText;

    TextView usernameText;

    int userId;
    Player player;

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

        academicProgress = (ProgressBar) view.findViewById(R.id.academicProgress);
        academicProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        academicProgressText = view.findViewById(R.id.academicProgressText);

        fitnessProgress = (ProgressBar) view.findViewById(R.id.fitnessProgress);
        fitnessProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        fitnessProgressText = view.findViewById(R.id.fitnessProgressText);

        dietProgress = (ProgressBar) view.findViewById(R.id.dietProgress);
        dietProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        dietProgressText = view.findViewById(R.id.dietProgressText);

        mentalProgress = (ProgressBar) view.findViewById(R.id.mentalProgress);
        mentalProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        mentalProgressText = view.findViewById(R.id.mentalProgressText);

        usernameText = view.findViewById(R.id.usernameText);

        Bundle bundle = this.getArguments();
        userId = bundle.getInt("userId");
        System.out.println(userId);

        if(userId == Player.getPlayer().getId()) {
            player = Player.getPlayer();
            // Update the player scores
            UpdateScores updateScoresTask = new UpdateScores();
            updateScoresTask.execute(0);
        }
        else {
            System.out.println("getting info");
            GetPlayerInfo getInfo = new GetPlayerInfo();
            getInfo.execute(userId);
        }
        return view;
    }

    public void updateStats() {
        Player.Scores scores = player.getScores();
        System.out.println(scores);

        usernameText.setText(player.getName());

        int academicScore = scores.getAcademics();
        int academicMax = 1000;
        academicProgress.setMax(academicMax);
        academicProgress.setProgress(academicScore);
        academicProgressText.setText(academicScore + "/" + academicMax);

        int fitnessScore = scores.getFitness();
        int fitnessMax = 1000;
        fitnessProgress.setMax(fitnessMax);
        fitnessProgress.setProgress(fitnessScore);
        fitnessProgressText.setText(fitnessScore + "/" + fitnessMax);

        int dietScore = scores.getDiet();
        int dietMax = 1000;
        dietProgress.setMax(dietMax);
        dietProgress.setProgress(dietScore);
        dietProgressText.setText(dietScore + "/" + dietMax);

        int mentalScore = scores.getMentalWellness();
        int mentalMax = 1000;
        mentalProgress.setMax(mentalMax);
        mentalProgress.setProgress(mentalScore);
        mentalProgressText.setText(mentalScore + "/" + mentalMax);
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
}
