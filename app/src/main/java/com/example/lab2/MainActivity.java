package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MyActivity";
    String[] labels;

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

    @Override
    public void onItemSelected(AdapterView <?> parent, View view,int position, long id){
        String selection = (String) parent.getItemAtPosition(position);
        Log.i(TAG, "Spinner selection made: "+selection);
        boolean result = selection.toLowerCase(Locale.ROOT).equals(labels[0]);

        if (result == true) {
            int i = 0;
            i = 1;
            Log.i(TAG,"Heads or tails was selected");
        }
    }

    @Override
    public void onNothingSelected(AdapterView <?> parent){
    // sometimes you need nothing here
        int i = 0;
    }

}


