package com.example.lab2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GestureDetectorCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MyActivity";
    String[] labels;
    TextView yourNumberTextView;
    TextView myNumberTextView;
    SeekBar seekbar;
    Button guessButton;
    int selectedGame;
    int yourNumber = 0;
    int yourScore = 0;
    int myScore = 0;
    boolean pinching = false;

    GameSettings[] gameSettings = {new GameSettings(0,1), new GameSettings(1,10), new GameSettings(1,100)};
    ScaleGestureDetector mDetector;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "MainActivity.onCreate running...");

        try {
            initSpinner();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Resources res = getResources();
        labels = res.getStringArray(R.array.options);
        yourNumberTextView = findViewById(R.id.textView3);
        seekbar = findViewById(R.id.seekBar4);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yourNumber = progress;
                yourNumberTextView.setText(getText(R.string.your_number)+" "+yourNumber);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        guessButton = findViewById(R.id.button2);
        guessButton.setOnClickListener(this::clickedGuessButton);
        myNumberTextView = findViewById(R.id.textView5);
        mDetector = new ScaleGestureDetector(this, new Lab2GestureListener());


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        //noinspection OptionalGetWithoutIsPresent
        return random.ints(min, max+1)
                .findFirst()
                .getAsInt();
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clickedGuessButton(View view){
        Log.i(TAG, "Guess Rendered");
        int myNumber = getRandomNumber(gameSettings[selectedGame].min,gameSettings[selectedGame].max);
        myNumberTextView.setText(getString(R.string.my_number)+" "+myNumber);
        if (myNumber == yourNumber){
            yourScore++;
            Log.i(TAG,"Player wins!");
        } else {
            myScore++;
            Log.i(TAG, "I win!");
        }
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if (yourScore>myScore) {
            String text = "Player winning: " + yourScore + " - " + myScore;
            Log.i(TAG, text);
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (yourScore<myScore){
            String text = "I am winning: " + yourScore + " - " + myScore;
            Log.i(TAG,text );
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            String text = "tie game: " + yourScore + " - " + myScore;
            Log.i(TAG, text);
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
    private void initSpinner(){
        Spinner spinner = findViewById(R.id.gamesSelectionSpinner);
        try{
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> array_adapter = ArrayAdapter.createFromResource(this,
                    R.array.options, android.R.layout.simple_spinner_dropdown_item);
            // Specify the layout to use when the list of choices appears
            array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(array_adapter);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            throw e;
        }
        spinner.setOnItemSelectedListener(this);
    }
    private void displayYourNumber(){
        yourNumber = seekbar.getProgress();
        String text = getString(R.string.your_number) + " "+ yourNumber;
        yourNumberTextView.setText(text);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setSeekbarRange(int min, int max){
        seekbar.setMin(min);
        seekbar.setMax(max);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupGame(){
        if (selectedGame == 0){
            Log.i(TAG, "Starting Heads or Tails Setup...");
        }else{
            Log.i(TAG, "Starting " + gameSettings[selectedGame].min + " to "+gameSettings[selectedGame].max+ " Setup...");
        }
        setSeekbarRange(gameSettings[selectedGame].min,gameSettings[selectedGame].max);
        displayYourNumber();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String selection = (String) parent.getItemAtPosition(position);
        Log.i(TAG, "Spinner selection made: "+selection);
        int i;
        for (i=0; i<labels.length; i++) {
            if (selection.equals(labels[i])){
                selectedGame = i;
                setupGame();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView <?> parent){
        // sometimes you need nothing here
    }

    class Lab2GestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private static final String DEBUG_TAG = "Gestures";
        @Override
        public boolean onScale (ScaleGestureDetector detector) {
            Log.i(TAG,"double tap: " + detector.toString());
            float scaleFactor = detector.getScaleFactor();
            if (scaleFactor < 1) {
                // pinching
                if (pinching == false) {
                    pinching = true;
                    myScore = 0;
                    yourScore = 0;

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    String text = "Score reset: " + yourScore + " - " + myScore;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }

            return true;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            pinching = false;
        }
    }

}

class GameSettings {

    int min;
    int max;
    GameSettings(int min, int max){
        this.min = min;
        this.max = max;
    }
}

