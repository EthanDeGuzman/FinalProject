package edu.edeguzman.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    int score;
    TextView scoreText;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //Keep the game landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        score = getIntent().getIntExtra("score", 0);
        scoreText = findViewById(R.id.score);

        //Show total score
        scoreText.setText("Your total score was: " + String.valueOf(score));
    }

    public void playAgain(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

    //Go to High Score Page
    public void goHighScore(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UserName");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Intent highScore = new Intent(GameOver.this, HighScorePage.class);
                highScore.putExtra("score", score);
                highScore.putExtra("name", m_Text);
                startActivity(highScore);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}