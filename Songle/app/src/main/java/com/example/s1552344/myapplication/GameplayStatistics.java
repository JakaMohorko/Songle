package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameplayStatistics extends AppCompatActivity {

    String statistics = "placeholder";
    String words = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_words);

        FileInputStream fis = null;
        String output = "";
        try {
            fis = openFileInput("data.txt");

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String arg = line.split(" ")[0];
                int val = Integer.parseInt(line.split(" ")[1]);

                if(arg.equals("DistanceWalked")){
                }
                if(arg.equals("TotalSolved")){
                }
                if(arg.equals("EasiestSolved")){
                }
                if(arg.equals("EasySolved")){
                }
                if(arg.equals("MediumSolved")){
                }
                if(arg.equals("HardSolved")){
                }
                if(arg.equals("HardestSolved")){
                }
                if(arg.equals("FastestTimeEasiest")){
                }
                if(arg.equals("FastestTimeEasy")){
                }
                if(arg.equals("FastestTimeMedium")){
                }
                if(arg.equals("FastestTimeHard")){
                }
                if(arg.equals("FastestTimeHardest")){
                }
                if(arg.equals("PlacemarksCollected")){
                }
                if(arg.equals("AverageTimeEasiest")){
                }
                if(arg.equals("AverageTimeEasy")){
                }
                if(arg.equals("AverageTimeMedium")){
                }
                if(arg.equals("AverageTimeHard")){
                }
                if(arg.equals("AverageTimeHardest")){
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        TextView mytxt=(TextView ) findViewById(R.id.textView);
        mytxt.setText(statistics);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    public void back (View view){
        finish();
    }


}
