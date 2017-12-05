package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class Success extends AppCompatActivity {
    Song selectedSong;
    double distanceWalked;
    long timeSpent;
    int collectedPlacemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Intent intent = getIntent();

        selectedSong = (Song)intent.getSerializableExtra("selectedSong");
        distanceWalked = (double)intent.getSerializableExtra("distance");
        timeSpent = (long)intent.getSerializableExtra("time");
        collectedPlacemarks = (int)intent.getSerializableExtra("placemarks");

        String output = "Artist: " + selectedSong.getArtist() + "\n" +
                "Title: " + selectedSong.getTitle() + "\n" +
                "Link: " + selectedSong.getLink() + "\n\n\n"+
                "Time spent: " + (timeSpent/60) + " minutes " + (timeSpent-(timeSpent/60)) + " seconds" + "\n"+
                "Placemarks collected: " + collectedPlacemarks + "\n"+
                "Distance walked: " + distanceWalked + " meters";


        TextView mytxt=(TextView ) findViewById(R.id.textView);
        mytxt.setText(output);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void mainMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP );

        startActivity(intent);
        finish();
    }
}
