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

public class GuessSong extends AppCompatActivity {
    Song selectedSong;
    EditText mEdit;
    int collectedPlacemarks;
    long timeSpent;
    double distanceWalked;

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
           //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP );

            startActivity(intent);
            finish();

        }else{
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

    public void back (View view){
        finish();
    }

}
