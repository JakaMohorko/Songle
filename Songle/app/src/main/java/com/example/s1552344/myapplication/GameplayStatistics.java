package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameplayStatistics extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gameplay_statistics);

        FileInputStream fis = null;
        ArrayList<Statistic> statistics = new ArrayList<>();
        try {
            fis = openFileInput("data.txt");

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String arg = line.split(" ")[0];
                int val = Integer.parseInt(line.split(" ")[1]);

                if(arg.equals("DistanceWalked")){
                    statistics.add(new Statistic("Distance walked:", val));
                }
                if(arg.equals("TotalSolved")){
                    statistics.add(new Statistic("Number of puzzles solved:", val));
                }
                if(arg.equals("EasiestSolved")){
                    statistics.add(new Statistic("Number of Easiest difficulty puzzles solved:", val));
                }
                if(arg.equals("EasySolved")){
                    statistics.add(new Statistic("Number of Easy difficulty  puzzles solved:", val));
                }
                if(arg.equals("MediumSolved")){
                    statistics.add(new Statistic("Number of Medium difficulty puzzles solved:", val));
                }
                if(arg.equals("HardSolved")){
                    statistics.add(new Statistic("Number of Hard difficulty puzzles solved:", val));
                }
                if(arg.equals("HardestSolved")){
                    statistics.add(new Statistic("Number of Hardest difficulty puzzles solved:", val));
                }
                if(arg.equals("FastestTimeEasiest")){
                    statistics.add(new Statistic("Fastest time to solve a Easiest difficulty puzzle:", val));
                }
                if(arg.equals("FastestTimeEasy")){
                    statistics.add(new Statistic("Fastest time to solve a Easy difficulty puzzle:", val));
                }
                if(arg.equals("FastestTimeMedium")){
                    statistics.add(new Statistic("Fastest time to solve a Medium difficulty puzzle:", val));
                }
                if(arg.equals("FastestTimeHard")){
                    statistics.add(new Statistic("Fastest time to solve a Hard difficulty puzzle:", val));
                }
                if(arg.equals("FastestTimeHardest")){
                    statistics.add(new Statistic("Fastest time to solve a Hardest difficulty puzzle:", val));
                }
                if(arg.equals("PlacemarksCollected")){
                    statistics.add(new Statistic("Placemarks collected:", val));
                }
                if(arg.equals("AverageTimeEasiest")){
                    statistics.add(new Statistic("Average time to solve a Easiest difficulty puzzle:", val));
                }
                if(arg.equals("AverageTimeEasy")){
                    statistics.add(new Statistic("Average time to solve a Easy difficulty puzzle:", val));
                }
                if(arg.equals("AverageTimeMedium")){
                    statistics.add(new Statistic("Average time to solve a Medium difficulty puzzle:", val));
                }
                if(arg.equals("AverageTimeHard")){
                    statistics.add(new Statistic("Average time to solve a Hard difficulty puzzle:", val));
                }
                if(arg.equals("AverageTimeHardest")){
                    statistics.add(new Statistic("Average time to solve a Hardest difficulty puzzle:", val));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mListView = (ListView) findViewById(R.id.listViewStats);
        System.out.println("adapter " + mListView);

        StatisticsAdapter adapter = new StatisticsAdapter(this, statistics);
        mListView.setAdapter(adapter);

    }
    public void back (View view){
        finish();
    }


}
