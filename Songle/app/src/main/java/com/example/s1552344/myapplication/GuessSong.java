package com.example.s1552344.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import java.util.Random;

/**
 * Song guessing activity.
 * The user enters their guess and is presented either the success screen
 * or a dialog notifying them that their guess is wrong.
 */
public class GuessSong extends AppCompatActivity {
    //obtained variables
    private Song selectedSong;
    private int collectedPlacemarks;
    private long timeSpent;
    private double distanceWalked;

    //text input
    private EditText mEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guess_song);

        mEdit   = (EditText)findViewById(R.id.editText);
        Intent intent = getIntent();
        selectedSong = (Song)intent.getSerializableExtra("selectedSong");
        distanceWalked = (double)intent.getSerializableExtra("distance");
        timeSpent = (long)intent.getSerializableExtra("time");
        collectedPlacemarks = (int)intent.getSerializableExtra("placemarks");
    }

    public void switchActivity (View view){
        System.out.println("Song Guess " + mEdit.getText().toString() + " " + selectedSong.getTitle());

        if(selectedSong.getTitle().toLowerCase().equals(mEdit.getText().toString().toLowerCase())) {
            System.out.println("correct");


            //if the user guesses correctly, switch to Success and mark the solved shared preferences variable as true
            //to inform other activities of the fact
            SharedPreferences settings = getSharedPreferences("prefsFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("solved", true);
            editor.commit();

            Intent intent = new Intent(this, Success.class);
            intent.putExtra("selectedSong", selectedSong);
            intent.putExtra("distance", distanceWalked);
            intent.putExtra("placemarks", collectedPlacemarks);
            timeSpent = (System.currentTimeMillis()-timeSpent) /1000;
            intent.putExtra("time", timeSpent);

            startActivity(intent);
            finish();

        }else{
            //if user guesses incorrectly, display a dialog notifying them
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Incorrect Guess!")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

        }

    }
    //back button press function
    public void back (View view){
        finish();
    }

}
