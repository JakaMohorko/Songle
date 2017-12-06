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
        int time = (int)(timeSpent/60);

        String output = "Artist: " + selectedSong.getArtist() + "\n" +
                "Title: " + selectedSong.getTitle() + "\n" + "\n\n\n"+
                "Time spent: " + time + " min " + (timeSpent-time*60) + " s" + "\n"+
                "Placemarks collected: " + collectedPlacemarks + "\n"+
                "Distance walked: " + (int)distanceWalked + " m";


        TextView mytxt=(TextView ) findViewById(R.id.textView7);
        mytxt.setText(output);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void mainMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP );

        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP );

        startActivity(intent);
        finish();

    }
}
