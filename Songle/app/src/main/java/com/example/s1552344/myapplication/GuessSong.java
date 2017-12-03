package com.example.s1552344.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guess_song);

        mEdit   = (EditText)findViewById(R.id.editText);
        Intent intent = getIntent();
        selectedSong = (Song)intent.getSerializableExtra("selectedSong");
    }

    public void switchActivity (View view){
        System.out.println("Song Guess " + mEdit.getText().toString() + " " + selectedSong.getTitle());

        if(selectedSong.getTitle().toLowerCase().equals(mEdit.getText().toString().toLowerCase())) {
            System.out.println("correct");

            SharedPreferences settings = getSharedPreferences("prefsFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("solved", true);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP );

            startActivity(intent);
            finish();

        }else{
            System.out.println("incorrect");
        }

    }

    public void back (View view){
        finish();
    }

}
