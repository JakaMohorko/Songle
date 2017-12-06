package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsActivity extends AppCompatActivity {

    ArrayList<String> collectedWords;
    String words = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_words);


        Intent intent = getIntent();
        collectedWords = (ArrayList<String>) intent.getSerializableExtra("collectedWords");

        for(int x = 0; x < collectedWords.size(); x++){
            words = words + "      " + (x+1) + " | " + collectedWords.get(x)+ "\n";
        }

        TextView mytxt=(TextView ) findViewById(R.id.textView);
        mytxt.setText(words);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    public void back (View view){
        finish();
    }


}
