package com.example.s1552344.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Difficulty select acitivity. User can choose a difficulty.
 * Upon choosing, a song which hasn't yet been solved at the chosen difficulty
 * is chosen and passed to the next activity.
 */

public class DifficultySelect extends Activity {


    //debug tag
    private static final String TAG = "Difficulty Select";

    //variables obtained from previous activity
    private ArrayList<Song> songList;

    //variables to be passed onwards
    private String difficulty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_difficulty_select);
        Intent intent = getIntent();

        //obtain the song list from MainActivity or MapsActivity
        setSongList((ArrayList<Song>) intent.getSerializableExtra("songList"));
    }

    //Select difficulty and switch activities depending on which button is pressed
    public void easiest(View view) {
        setDifficulty("5");
        switchActivity(view);
    }

    public void easy(View view) {
        setDifficulty("4");
        switchActivity(view);
    }

    public void medium(View view) {
        setDifficulty("3");
        switchActivity(view);
    }

    public void hard(View view) {
        setDifficulty("2");
        switchActivity(view);
    }

    public void hardest(View view) {
        setDifficulty("1");
        switchActivity(view);
    }

    public void switchActivity(View view) {
        Intent intent = new Intent(this, LoadMap.class);
        String line;
        FileInputStream fis = null;
        try {

            //parse the file where songs already solved are stored according to difficulty
            //and save the numebers of songs already solved at the chosen difficulty into an array.
            fis = openFileInput("solvedbydifficulty.txt");
            InputStreamReader isr2 = new InputStreamReader(fis);
            BufferedReader bufferedReader2 = new BufferedReader(isr2);
            String[] alreadySolved = new String[0];

            while ((line = bufferedReader2.readLine()) != null) {
                String arg = line.split(" ")[0];
                System.out.println(line);
                if (getDifficulty().equals("5") && arg.equals("Easiest")) {
                    alreadySolved = line.split(" ");
                } else if (getDifficulty().equals("4") && arg.equals("Easy")) {
                    alreadySolved = line.split(" ");
                } else if (getDifficulty().equals("3") && arg.equals("Medium")) {
                    alreadySolved = line.split(" ");
                } else if (getDifficulty().equals("2") && arg.equals("Hard")) {
                    alreadySolved = line.split(" ");
                } else if (getDifficulty().equals("1") && arg.equals("Hardest")) {
                    alreadySolved = line.split(" ");
                }
            }
            isr2.close();
            bufferedReader2.close();
            fis.close();

            //if no songs are unsolved at the chosen difficulty, ask the user to pick
            //a different difficulty
            if (alreadySolved.length == getSongList().size() + 2) {
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

                builder.setTitle("No songs left")
                        .setMessage("All of the songs currently available have already been beaten at the selected difficulty level. Please pick a different difficulty.")
                        .setPositiveButton("return", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                return;
            }
            //if any song has already been solved at the chosen difficulty, select a random
            //unsolved song
            if (alreadySolved.length > 1) {
                boolean newSong = false;
                System.out.println("find new song");
                Random generator = new Random();
                String cur = "";
                //generate a random number from 1 to the size of the song list
                int x = generator.nextInt(getSongList().size() - 1);
                x++;
                while (true) {

                    for (int y = 1; y < alreadySolved.length; y++) {

                        //append a 0 to the chosen number if x<10. Song numbers are stored as 01,02,03...
                        if (x < 10) {
                            cur = "0" + Integer.toString(x);

                        } else {
                            cur = Integer.toString(x);
                        }
                        System.out.println("trying number " + cur + " " + alreadySolved[y]);
                        //keep incrementing x by one until an unsolved song is found
                        if (alreadySolved[y].equals(cur)) {
                            if (x == getSongList().size()) {
                                x = 1;
                            } else {
                                System.out.println("printing x " + x);
                                x++;
                            }
                            break;
                        }
                        if (y == alreadySolved.length - 1) {
                            newSong = true;
                        }
                    }
                    if (newSong) {
                        intent.putExtra("song", getSongList().get(x - 1));
                        break;
                    }
                }
            } else {
                //pick a random song if no songs have been solved yet at the chosen difficulty
                System.out.println("random");
                Random generator = new Random();
                int x = generator.nextInt(getSongList().size() - 1);
                intent.putExtra("song", getSongList().get(x));
            }

            //switch to LoadMap activity
            intent.putExtra("difficulty", getDifficulty());
            intent.putExtra("songList", getSongList());
            Log.d(TAG, "Difficulty " + getDifficulty());
            startActivity(intent);
            finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //button press actions
    public void switchHelp(View view) {
        Intent intent = new Intent(this, HelpDifficulty.class);

        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }


    //getters and setters
    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
