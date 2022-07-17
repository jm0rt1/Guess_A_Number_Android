package com.example.lab2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

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

    GameSettings[] gameSettings = {new GameSettings(0,1), new GameSettings(1,10), new GameSettings(1,100)};




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



    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.ints(min, max+1)
                .findFirst()
                .getAsInt();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clickedGuessButton(View view){
        Log.i(TAG, "Guess Rendered");
        int myNumber = getRandomNumber(gameSettings[selectedGame].min,gameSettings[selectedGame].max);
        myNumberTextView.setText(getString(R.string.my_number)+" "+myNumber);
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

}

class GameSettings {

    int min;
    int max;
    GameSettings(int min, int max){
        this.min = min;
        this.max = max;
    }
}

