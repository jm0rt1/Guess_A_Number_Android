package com.example.lab2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MyActivity";
    String[] labels;

    interface GameSetup {
        void game();
    }
    private GameSetup[] gameSetups = new GameSetup[] {
            new GameSetup() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void game() {gameHeadsOrTails();}},
            new GameSetup() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void game() {game1to10();}},
            new GameSetup() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void game() {game1to100();}},
    };


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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void game1to100(){
        SeekBar seekbar = findViewById(R.id.seekBar4);
        Log.i(TAG, "Starting 1 to 100 Setup...");
        seekbar.setMin(1);
        seekbar.setMax(100);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void game1to10(){
        SeekBar seekbar = findViewById(R.id.seekBar4);
        Log.i(TAG, "Starting 1 to 10 Setup...");
        seekbar.setMin(1);
        seekbar.setMax(10);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void gameHeadsOrTails(){
        SeekBar seekbar = findViewById(R.id.seekBar4);
        Log.i(TAG, "Starting Heads or Tails Setup...");
        seekbar.setMin(0);
        seekbar.setMin(1);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String selection = (String) parent.getItemAtPosition(position);
        Log.i(TAG, "Spinner selection made: "+selection);
        boolean result = selection.toLowerCase(Locale.ROOT).equals(labels[0]);
        int i;
        for (i=0; i<labels.length; i++) {
            if (selection.equals(labels[i])){
                gameSetups[i].game();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView <?> parent){
        // sometimes you need nothing here
    }

}


