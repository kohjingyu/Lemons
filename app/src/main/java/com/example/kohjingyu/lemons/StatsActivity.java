package com.example.kohjingyu.lemons;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;


public class StatsActivity extends AppCompatActivity {

    ProgressBar academicProgress;
    TextView academicProgressText;
    ProgressBar fitnessProgress;
    TextView fitnessProgressText;
    ProgressBar dietProgress;
    TextView dietProgressText;
    ProgressBar mentalProgress;
    TextView mentalProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        academicProgress = (ProgressBar) findViewById(R.id.academicProgress);
        academicProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        academicProgressText = findViewById(R.id.academicProgressText);

        fitnessProgress = (ProgressBar) findViewById(R.id.fitnessProgress);
        fitnessProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        fitnessProgressText = findViewById(R.id.fitnessProgressText);

        dietProgress = (ProgressBar) findViewById(R.id.dietProgress);
        dietProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        dietProgressText = findViewById(R.id.dietProgressText);

        mentalProgress = (ProgressBar) findViewById(R.id.mentalProgress);
        mentalProgress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ed3b27")));
        mentalProgressText = findViewById(R.id.mentalProgressText);

        // Update the player scores
        UpdateScores updateScoresTask = new UpdateScores();
        updateScoresTask.execute(0);
    }


    public void updateStats() {
        Player.Scores scores = Player.getPlayer().getScores();
        System.out.println(scores);

        // TODO: Update with max (goal set by player)

        int academicScore = 50;//scores.getAcademics();
        int academicMax = 100;
        academicProgress.setMax(academicMax);
        academicProgress.setProgress(academicScore);
        academicProgressText.setText(academicScore + "/" + academicMax);

        int fitnessScore = 10;//scores.getFitness();
        int fitnessMax = 100;
        fitnessProgress.setMax(fitnessMax);
        fitnessProgress.setProgress(fitnessScore);
        fitnessProgressText.setText(fitnessScore + "/" + fitnessMax);

        int dietScore = 30;//scores.getDiet();
        int dietMax = 100;
        dietProgress.setMax(dietMax);
        dietProgress.setProgress(dietScore);
        dietProgressText.setText(dietScore + "/" + dietMax);

        int mentalScore = 20;//scores.getMentalWellness());
        int mentalMax = 100;
        mentalProgress.setMax(mentalMax);
        mentalProgress.setProgress(mentalScore);
        mentalProgressText.setText(mentalScore + "/" + mentalMax);
    }

    private class UpdateScores extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            Player.getPlayer().updateScores();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                updateStats();
            }
        }
    }
}
