package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

public class HelpMain extends AppCompatActivity {

    String help = "    - Select Play to start a new game of a difficulty of choice.\n\n"+
            "    - Select Song List to view the list of songs you've already encountered and beaten.\n\n"+
            "    - The achievements window will show you your progress towards the achievements available in the game.\n\n"+
            "    - Select Statistics to view data about your gameplay sessions.\n\n"+
            "    - You can quit the game by pressing the Exit button.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_main);



        TextView mytxt=(TextView ) findViewById(R.id.textView4);
        mytxt.setText(help);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    public void back (View view){
        finish();
    }


}
