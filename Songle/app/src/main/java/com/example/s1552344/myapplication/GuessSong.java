package com.example.s1552344.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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

    //test vars
    private String songTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guess_song);

        setmEdit((EditText) findViewById(R.id.editText));
        Intent intent = getIntent();
        setSelectedSong((Song) intent.getSerializableExtra("selectedSong"));
        setDistanceWalked((double) intent.getSerializableExtra("distance"));
        setTimeSpent((long) intent.getSerializableExtra("time"));
        setCollectedPlacemarks((int) intent.getSerializableExtra("placemarks"));
        setSongTitle(getSelectedSong().getTitle());
    }

    public void switchActivity(View view) {
        System.out.println("Song Guess " + getmEdit().getText().toString() + " " + getSelectedSong().getTitle());

        if (getSelectedSong().getTitle().toLowerCase().equals(getmEdit().getText().toString().toLowerCase())) {
            System.out.println("correct");


            //if the user guesses correctly, switch to Success and mark the solved shared preferences variable as true
            //to inform other activities of the fact
            SharedPreferences settings = getSharedPreferences("prefsFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("solved", true);
            editor.commit();

            Intent intent = new Intent(this, Success.class);
            intent.putExtra("selectedSong", getSelectedSong());
            intent.putExtra("distance", getDistanceWalked());
            intent.putExtra("placemarks", getCollectedPlacemarks());
            setTimeSpent((System.currentTimeMillis() - getTimeSpent()) / 1000);
            intent.putExtra("time", getTimeSpent());

            startActivity(intent);
            finish();

        } else {
            //if user guesses incorrectly, display a dialog notifying them
            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("Incorrect Guess!")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

        }

    }

    //back button press function
    public void back(View view) {
        finish();
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    //getters and setters
    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public int getCollectedPlacemarks() {
        return collectedPlacemarks;
    }

    public void setCollectedPlacemarks(int collectedPlacemarks) {
        this.collectedPlacemarks = collectedPlacemarks;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public void setDistanceWalked(double distanceWalked) {
        this.distanceWalked = distanceWalked;
    }

    public EditText getmEdit() {
        return mEdit;
    }

    public void setmEdit(EditText mEdit) {
        this.mEdit = mEdit;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
}
