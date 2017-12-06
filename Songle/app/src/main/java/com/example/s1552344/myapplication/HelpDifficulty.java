package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

public class HelpDifficulty extends AppCompatActivity {

    String help = "    Easiest: All of the lyrics of a song can be collected and are classified with markers 1, 2, 3 and 4.\n\n"+
            "    Easy: All of the lyrics of a song can be collected and are classified with markers 2, 3 and 4\n\n"+
            "    Normal: 75% of the lyrics of a song can be collected and are classified with markers 2, 3 and 4\n\n"+
            "    Hard: 50% of the lyrics of a song can be collected and are classified with markers 3 and 4\n\n"+
            "    Hardest: 25% of the lyrics of a song can be collected and are classified only with markers 5\n\n"+
            "1 - Red marker with star: very uncommon word\n" +
            "2 - Orange marker with diamond: uncommon word\n"+
            "3 - Yellow marker with circle: common word\n" +
            "4 - Yellow marker: very common word\n"+
            "5 - White marker: unclassified word\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_difficulty);



        TextView mytxt=(TextView ) findViewById(R.id.textView3);
        mytxt.setText(help);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    public void back (View view){
        finish();
    }


}
