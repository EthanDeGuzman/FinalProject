package edu.edeguzman.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HighScorePage extends AppCompatActivity {
    int score;
    String username;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_page);

        //Keep the game landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        score = getIntent().getIntExtra("score", 0);
        username = getIntent().getStringExtra("name");

        listView = findViewById(R.id.lv);
        DatabaseHandler db = new DatabaseHandler(this);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        //Check top 5 compared to actual score
        List<HiScore> top5HiScores = db.getTopFiveScores();
        HiScore hiScore = top5HiScores.get(top5HiScores.size() - 1);

        if (hiScore.getScore() < score) {
            db.addHiScore(new HiScore(currentDate, username, score));
        }

        // Calling SQL statement
        top5HiScores = db.getTopFiveScores();
        List<String> scoresStr;
        scoresStr = new ArrayList<>();

        int j = 1;
        for (HiScore hs : top5HiScores) {

            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // store score in string array
            scoresStr.add(j++ + ") "  +
                    "Date: " + hs.getGame_date() + "\t UserName: " +
                    hs.getPlayer_name() + "\t Score: " +
                    hs.getScore());
            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        Log.i("divider", "====================");
        Log.i("divider", "Scores in list <>>");
        for (String ss : scoresStr) {
            Log.i("Score: ", ss);
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoresStr);
        listView.setAdapter(itemsAdapter);
    }

    public void playAgain(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }
}