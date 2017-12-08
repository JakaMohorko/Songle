package com.example.s1552344.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * Success screen displayed when the user correctly guesses the song title
 */

public class Success extends AppCompatActivity {

    //Data obtained from previous activity
    private Song selectedSong;
    private double distanceWalked;
    private long timeSpent;
    private int collectedPlacemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Intent intent = getIntent();

        //obtain data to be displayed
        setSelectedSong((Song) intent.getSerializableExtra("selectedSong"));
        setDistanceWalked((double) intent.getSerializableExtra("distance"));
        setTimeSpent((long) intent.getSerializableExtra("time"));
        setCollectedPlacemarks((int) intent.getSerializableExtra("placemarks"));
        int time = (int) (getTimeSpent() / 60);

        //Craft the string containing gameplay data to be displayed
        String output = "Artist: " + getSelectedSong().getArtist() + "\n" +
                "Title: " + getSelectedSong().getTitle() + "\n" + "\n\n\n" +
                "Time spent: " + time + " min " + (getTimeSpent() - time * 60) + " s" + "\n" +
                "Placemarks collected: " + getCollectedPlacemarks() + "\n" +
                "Distance walked: " + (int) getDistanceWalked() + " m";


        //set the text view contents
        TextView mytxt = (TextView) findViewById(R.id.textView7);
        mytxt.setText(output);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    //Switch to MainMenu by reordering it to the top of the activity stack and closing other activities
    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        finish();
    }

    //Switch to MainMenu on back button press
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        finish();

    }

    //Getters and Setters
    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public void setDistanceWalked(double distanceWalked) {
        this.distanceWalked = distanceWalked;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getCollectedPlacemarks() {
        return collectedPlacemarks;
    }

    public void setCollectedPlacemarks(int collectedPlacemarks) {
        this.collectedPlacemarks = collectedPlacemarks;
    }
}
