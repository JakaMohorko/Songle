package com.example.s1552344.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Activity where progress on achievements is parsed and displayed
 */

public class Achievements extends AppCompatActivity {

    private ArrayList<Achievement> achievsList = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize achievemtn descriptions and their progress to the default value
        setContentView(R.layout.activity_achievements);
        String[] achievs = {
                "Collect 100 placemarks",
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
        };

        String[] obtained = {
                "Not completed", "Not completed", "Not completed", "Not completed",
                "Not completed", "Not completed", "Not completed", "Not completed",
                "Not completed", "Not completed", "Not completed", "Not completed", "Not completed",
        };

        String[] progress = {
                "0/100", "0/500", "0/1000", "0/5000", "0/5", "0/10", "0/50", "0/1", "0/10", "0/20", "0/1", "0/1", "0/1"
        };

        //Parse the data.txt file and update achievement progress accordingly
        //some if statements are left empty for future purposes of adding additional achievements
        FileInputStream fis = null;
        try {
            fis = openFileInput("data.txt");

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String arg = line.split(" ")[0];
                int val = Integer.parseInt(line.split(" ")[1]);

                if (arg.equals("DistanceWalked")) {

                    if (val >= 5000) {
                        obtained[4] = "Completed";
                        progress[4] = "5/5";
                    } else {
                        progress[4] = val / 1000 + "/5";
                    }
                    if (val >= 10000) {
                        obtained[5] = "Completed";
                        progress[5] = "10/10";
                    } else {
                        progress[5] = val / 1000 + "/10";
                    }
                    if (val >= 50000) {
                        obtained[6] = "Completed";
                        progress[6] = "50/50";
                    } else {
                        progress[6] = val / 1000 + "/50";
                    }
                }
                if (arg.equals("TotalSolved")) {
                    if (val >= 1) {
                        obtained[7] = "Completed";
                        progress[7] = "1/1";
                    }
                    if (val >= 10) {
                        obtained[8] = "Completed";
                        progress[8] = "10/10";
                    } else {
                        progress[8] = val + "/10";
                    }
                    if (val >= 20) {
                        obtained[9] = "Completed";
                        progress[9] = "20/20";
                    } else {
                        progress[9] = val + "/20";
                    }
                }
                if (arg.equals("EasiestSolved")) {
                }
                if (arg.equals("EasySolved")) {
                }
                if (arg.equals("MediumSolved")) {
                }
                if (arg.equals("HardSolved")) {
                }
                if (arg.equals("HardestSolved")) {
                    if (val != 0) {
                        obtained[12] = "Completed";
                        progress[12] = "1/1";
                    }
                }
                if (arg.equals("FastestTimeEasiest")) {
                    if (val <= 300 && val != 0) {
                        obtained[10] = "Completed";
                        progress[10] = "1/1";
                    }
                    if (val <= 600 && val != 0) {
                        obtained[11] = "Completed";
                        progress[11] = "1/1";
                    }
                }
                if (arg.equals("FastestTimeEasy")) {
                    if (val <= 300 && val != 0) {
                        obtained[10] = "Completed";
                        progress[10] = "1/1";
                    }
                    if (val <= 600 && val != 0) {
                        obtained[11] = "Completed";
                        progress[11] = "1/1";
                    }
                }
                if (arg.equals("FastestTimeMedium")) {
                    if (val <= 300 && val != 0) {
                        obtained[10] = "Completed";
                        progress[10] = "1/1";
                    }
                    if (val <= 600 && val != 0) {
                        obtained[11] = "Completed";
                        progress[11] = "1/1";
                    }
                }
                if (arg.equals("FastestTimeHard")) {
                    if (val <= 300 && val != 0) {
                        obtained[10] = "Completed";
                        progress[10] = "1/1";
                    }
                    if (val <= 600 && val != 0) {
                        obtained[11] = "Completed";
                        progress[11] = "1/1";
                    }
                }
                if (arg.equals("FastestTimeHardest")) {
                    if (val <= 300 && val != 0) {
                        obtained[10] = "Completed";
                        progress[10] = "1/1";
                    }
                    if (val <= 600 && val != 0) {
                        obtained[11] = "Completed";
                        progress[11] = "1/1";
                    }

                }
                if (arg.equals("PlacemarksCollected")) {
                    if (val >= 100) {
                        obtained[0] = "Completed";
                        progress[0] = "100/100";
                    } else {
                        progress[0] = val + "/100";
                    }
                    if (val >= 500) {
                        obtained[1] = "Completed";
                        progress[1] = "500/500";
                    } else {
                        progress[1] = val + "/500";
                    }
                    if (val >= 1000) {
                        obtained[2] = "Completed";
                        progress[2] = "1000/1000";
                    } else {
                        progress[2] = val + "/1000";
                    }
                    if (val >= 2000) {
                        obtained[3] = "Completed";
                        progress[3] = "2000/2000";
                    } else {
                        progress[3] = val + "/2000";
                    }
                }
                if (arg.equals("AverageTimeEasiest")) {
                }
                if (arg.equals("AverageTimeEasy")) {
                }
                if (arg.equals("AverageTimeMedium")) {
                }
                if (arg.equals("AverageTimeHard")) {
                }
                if (arg.equals("AverageTimeHardest")) {
                }


            }
            fis.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Create achhievement objects and add them to a list for the ListView creation
        for (int x = 0; x < 13; x++) {
            getAchievsList().add(new Achievement(achievs[x], progress[x], obtained[x]));
        }


        //Feed achievements int a custom List View Adapter to display them on
        //the screen
        setmListView((ListView) findViewById(R.id.listViewAchievs));
        System.out.println("adapter " + getmListView());

        AchievementsListAdapter adapter = new AchievementsListAdapter(this, getAchievsList());
        getmListView().setAdapter(adapter);

    }

    public void back(View view) {
        finish();
    }


    //getters and setters
    public ArrayList<Achievement> getAchievsList() {
        return achievsList;
    }

    public void setAchievsList(ArrayList<Achievement> achievsList) {
        this.achievsList = achievsList;
    }

    public ListView getmListView() {
        return mListView;
    }

    public void setmListView(ListView mListView) {
        this.mListView = mListView;
    }
}
