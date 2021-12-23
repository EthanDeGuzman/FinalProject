package edu.edeguzman.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.Arrays;

public class PlayGame extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;
    int[] userSequence = new int[120], gameSequence;
    int userIndex = 0, arrayIndex, score, clicks = 0;
    Button bRed, bBlue, bYellow, bGreen;

    //Sensor variables
    private SensorManager mSensorManager;
    private Sensor mSensor;
    boolean isFlat = true;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        //Keep the game landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Set on Click Listeners to all buttons
        bRed = findViewById(R.id.btnRed);
        bRed.setOnClickListener(this);
        bBlue = findViewById(R.id.btnBlue);
        bBlue.setOnClickListener(this);
        bYellow = findViewById(R.id.btnYellow);
        bYellow.setOnClickListener(this);
        bGreen = findViewById(R.id.btnGreen);
        bGreen.setOnClickListener(this);

        //Sensor Rotation Vector
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        //Receive the extras in the intent
        gameSequence = getIntent().getExtras().getIntArray("numbers");
        arrayIndex = getIntent().getIntExtra("index", 0);
    }

    //When button is clicked execute this method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBlue:
                userSequence[userIndex++] = BLUE;
                break;
            case R.id.btnRed:
                userSequence[userIndex++] = RED;
                break;
            case R.id.btnYellow:
                userSequence[userIndex++] = YELLOW;
                break;
            case R.id.btnGreen:
                userSequence[userIndex++] = GREEN;
                break;
            default:
                break;
        }
        //Record how many buttons are clicked
        clicks++;
        //When the user clicks the same amount of buttons as the sequence
        //Check the sequence to see if it is right
        if (clicks >= arrayIndex){
            checkSequence();
        }
    }

    //Method to check if the correct sequence was entered
    public void checkSequence() {
        //Check how many the user matched
        for (int i = 0; i < arrayIndex; i++) {
            if (gameSequence[i] == userSequence[i])
                score++;
        }
        //Check if the user matched the sequence or not
        if (Arrays.equals(gameSequence, userSequence)) {
            //Bring back to home screen
            Toast.makeText(this, "Match Correctly", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //Bring to GameOver Screen
            Toast.makeText(this, "Incorrect Sequence", Toast.LENGTH_SHORT).show();
            Intent gameOver = new Intent(this, GameOver.class);
            gameOver.putExtra("score", score);
            startActivity(gameOver);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        if(Math.abs(x) >  0.46f & Math.abs(x) < 0.53f & Math.abs(y) > 0.43f & Math.abs(y) < 0.52f){
            isFlat = true;
        }
        else{
            if (Math.abs(x) < 0.4f & Math.abs(y) < 0.4f & isFlat == true) {
                flashButton(bBlue);
                userSequence[userIndex++] = BLUE;
                clicks++;
                isFlat = false;
            }
            if (Math.abs(x) > 0.62f & Math.abs(y) > 0.6f & isFlat == true) {
                flashButton(bYellow);
                userSequence[userIndex++] = YELLOW;
                clicks++;
                isFlat = false;
            }
            if (Math.abs(y) > 0.55f & Math.abs(x) < 0.42f & isFlat == true) {
                flashButton(bRed);
                userSequence[userIndex++] = RED;
                clicks++;
                isFlat = false;
            }
            if (Math.abs(y) < 0.4f & isFlat == true) {
                flashButton(bGreen);
                userSequence[userIndex++] = GREEN;
                clicks++;
                isFlat = false;
            }
        }

        if (clicks >= arrayIndex & isFlat == true){
            checkSequence();
        }
    }

    private void flashButton(Button button) {
        anim = new AlphaAnimation(1,0);
        anim.setDuration(1000);

        anim.setRepeatCount(0);
        button.startAnimation(anim);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power    }
    }
}