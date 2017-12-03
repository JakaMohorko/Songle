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

public class Achievements extends AppCompatActivity {

    String achievements = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_words);
        String[][] achievs = {
                {   "Collect 100 placemarks",
                    "Collect 500 placemarks",
                    "Collect 1000 placemarks",
                    "Collect 2000 placemarks",
                    "Walk 5km while playing",
                    "Walk 10km while playing",
                    "Walk 50km while playing",
                    "Complete 1 song puzzle",
                    "Complete 10 song puzzles",
                    "Complete 20 song puzzles",
                    "Complete a song puzzle in under 5 minutes",
                    "Complete a song puzzle in under 10 minutes",
                    "Complete a \"hardest\" song puzzle"
                },
                {
                    "False","False","False","False","False","False","False","False","False","False","False","False","False",
                }
        };
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
                    if(val>=5000){
                        achievs[1][4] = "True";
                    }
                    if(val>=10000){
                        achievs[1][5] = "True";
                    }
                    if(val>=50000){
                        achievs[1][6] = "True";
                    }
                }
                if(arg.equals("TotalSolved")){
                    if(val>=1){
                        achievs[1][7] = "True";
                    }
                    if(val>=10){
                        achievs[1][8] = "True";
                    }
                    if(val>=20){
                        achievs[1][9] = "True";
                    }
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
                    if(val!=0){
                        achievs[1][12] = "True";
                    }
                }
                if(arg.equals("FastestTimeEasiest")){
                    if(val<=300 && val!=0){
                        achievs[1][10] ="True";
                    }
                    if(val<=600 && val != 0){
                        achievs[1][11] = "True";
                    }
                }
                if(arg.equals("FastestTimeEasy")){
                    if(val<=300 && val!=0){
                        achievs[1][10] ="True";
                    }
                    if(val<=600 && val != 0){
                        achievs[1][11] = "True";
                    }
                }
                if(arg.equals("FastestTimeMedium")){
                    if(val<=300 && val!=0){
                        achievs[1][10] ="True";
                    }
                    if(val<=600 && val != 0){
                        achievs[1][11] = "True";
                    }
                }
                if(arg.equals("FastestTimeHard")){
                    if(val<=300 && val!=0){
                        achievs[1][10] ="True";
                    }
                    if(val<=600 && val != 0){
                        achievs[1][11] = "True";
                    }
                }
                if(arg.equals("FastestTimeHardest")){
                    if(val<=300 && val!=0){
                        achievs[1][10] ="True";
                    }
                    if(val<=600 && val != 0){
                        achievs[1][11] = "True";
                    }

                }
                if(arg.equals("PlacemarksCollected")){
                    if(val>=100){
                        achievs[1][0] = "True";
                    }
                    if(val >= 500){
                        achievs[1][1] = "True";
                    }
                    if(val >= 1000){
                        achievs[1][2] = "True";
                    }
                    if(val >= 2000){
                        achievs[1][3] = "True";
                    }
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


                output = output + arg + " " + val + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int x = 0; x < 13; x++){
            achievements = achievements + achievs[0][x] + ":   " + achievs[1][x] + "\n\n";
        }



        TextView mytxt=(TextView ) findViewById(R.id.textView);
        mytxt.setText(achievements);
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    public void back (View view){
        finish();
    }


}
