package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class DifficultySelect extends AppCompatActivity {

    Song selectedSong;
    private static final String TAG = "Difficulty Select";
    private String difficulty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_select);
        Intent intent = getIntent();
        selectedSong = (Song) (intent.getSerializableExtra("song"));
    }

    public void easiest(View view){
        difficulty = "5";
        switchActivity(view);
    }
    public void easy(View view){
        difficulty = "4";
        switchActivity(view);
    }
    public void medium(View view){
        difficulty = "3";
        switchActivity(view);
    }
    public void hard(View view){
        difficulty = "2";
        switchActivity(view);
    }
    public void hardest(View view){
        difficulty = "1";
        switchActivity(view);
    }

    public void switchActivity (View view){
        Intent intent = new Intent(this, LoadMap.class);


        intent.putExtra("difficulty", difficulty);
        intent.putExtra("song", selectedSong);
        Log.d(TAG, "Difficulty " + difficulty);
        startActivity(intent);
    }
}
