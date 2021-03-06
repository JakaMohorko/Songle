package com.example.s1552344.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * Help display activity for SongList
 */
public class HelpSongList extends AppCompatActivity {

    private String help = "    - Here you can see the titles and artists of songs you've already completed, " +
            "along with the highest difficulty you've beaten them at and what your fastest time was.\n\n" +
            "    - Clicking on a song will enable you to listen to the song on Youtube. " +
            "You have to confirm your choice by pressing the Listen button when prompted.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song_list_help);


        //display help info stored in help
        TextView mytxt = (TextView) findViewById(R.id.textView10);
        mytxt.setText(getHelp());
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    //back button press function
    public void back(View view) {
        finish();
    }

    //getters and setters
    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }
}
