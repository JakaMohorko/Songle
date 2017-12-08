package com.example.s1552344.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Activity displayed when user presses View Words during gameplay.
 * Collected words are parsed and listed here
 */

public class WordsActivity extends AppCompatActivity {

    //data on the collected words obtained from the previous activity
    private ArrayList<String> collectedWords;

    //output string container
    private String words = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_words);


        Intent intent = getIntent();
        setCollectedWords((ArrayList<String>) intent.getSerializableExtra("collectedWords"));

        //craft string to be displayed to the user
        for (int x = 0; x < getCollectedWords().size(); x++) {
            setWords(getWords() + "      " + (x + 1) + " | " + getCollectedWords().get(x) + "\n");
        }

        //set text view contents
        TextView mytxt = (TextView) findViewById(R.id.textView);
        mytxt.setText(getWords());
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    //return to the MapsActivity
    public void back(View view) {
        finish();
    }


    //getters and setters
    public ArrayList<String> getCollectedWords() {
        return collectedWords;
    }

    public void setCollectedWords(ArrayList<String> collectedWords) {
        this.collectedWords = collectedWords;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
