package edu.edeguzman.finalproject;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;

    Button bRed, bBlue, bYellow, bGreen, fb;
    int sequenceCount = 4, n = 0;
    int[] gameSequence = new int[120];
    int arrayIndex = 0, millisFuture = 6000, round = 1;

    Animation anim;

    //Timer for sequence
    void startTimer(){
        CountDownTimer ct = new CountDownTimer(millisFuture,  1500) {

            public void onTick(long millisUntilFinished) {
                oneButton();
            }

            public void onFinish() {
                //Log the sequence
                for (int i = 0; i< arrayIndex; i++)
                    Log.d("game sequence", String.valueOf(gameSequence[i]));

                //Increase to next round
                round++;

                //Start Next Activity
                Intent playGame = new Intent(MainActivity.this, PlayGame.class);
                playGame.putExtra("numbers", gameSequence);
                playGame.putExtra("index", arrayIndex);
                startActivity(playGame);
            }
        };

        ct.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Keep the game landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        bRed = findViewById(R.id.btnRed);
        bBlue = findViewById(R.id.btnBlue);
        bYellow = findViewById(R.id.btnYellow);
        bGreen = findViewById(R.id.btnGreen);
    }

    //When Play button is clicked start the app
    public void doPlay(View view) {
        if (round > 1){
            //Reset array for next sequence
            gameSequence = new int[120];
            arrayIndex = 0;

            //Change how long timer is active to increase sequence
            if (round % 2 != 0){
                round += 1;
                millisFuture = 6000 + 1500 * round;
            }
            else {
                millisFuture = 6000 + 1500 * round;
            }
        }

        //Start sequence
        startTimer();
    }

    //Flash one button at a time
    private void oneButton() {
        n = getRandom(sequenceCount);
        switch (n) {
            case 1:
                flashButton(bBlue);
                gameSequence[arrayIndex++] = BLUE;
                break;
            case 2:
                flashButton(bRed);
                gameSequence[arrayIndex++] = RED;
                break;
            case 3:
                flashButton(bYellow);
                gameSequence[arrayIndex++] = YELLOW;
                break;
            case 4:
                flashButton(bGreen);
                gameSequence[arrayIndex++] = GREEN;
                break;
            default:
                break;
        } // end switch
    }

    // return a number between 1 and maxValue
    private int getRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    //Animate Button flash
    private void flashButton(Button button) {
        anim = new AlphaAnimation(1,0);
        anim.setDuration(1000);

        anim.setRepeatCount(0);
        button.startAnimation(anim);
    }
}