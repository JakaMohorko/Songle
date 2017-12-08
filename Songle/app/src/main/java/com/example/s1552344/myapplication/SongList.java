package com.example.s1552344.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Activity that displays the list of already completed songs
 * in addition to some data on what the highest difficulty they were completed at
 * and the users best completion time for the specific song.
 */

public class SongList extends AppCompatActivity {

    //data containers to be displayed in the list view
    private ArrayList<String> songs = new ArrayList<>();
    private ArrayList<String> links = new ArrayList<>();

    //my list view
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song_list);

        //Parse the solved.txt file which contains data on which songs have already been
        //solved and what their highest difficulty and best time is
        FileInputStream fis = null;
        String output = "";
        try {
            fis = openFileInput("solved.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if (line.split(" ")[0].equals("Best")) {
                    int time = Integer.parseInt(line.split(" ")[2]);
                    output = output + "Best time: " + time / 60 + " min " + (time - ((time / 60) * 60)) + " s" + "\n";
                } else if (line.split(" ")[0].equals("Highest")) {
                    int difficulty = Integer.parseInt(line.split((" "))[2]);
                    if (difficulty == 1) {
                        output = output + "Highest difficulty: Hardest" + "\n\n";
                    }
                    if (difficulty == 2) {
                        output = output + "Highest difficulty: Hard" + "\n\n";
                    }
                    if (difficulty == 3) {
                        output = output + "Highest difficulty: Medium" + "\n\n";
                    }
                    if (difficulty == 4) {
                        output = output + "Highest difficulty: Easy" + "\n\n";
                    }
                    if (difficulty == 5) {
                        output = output + "Highest difficulty: Easiest" + "\n\n";
                    }
                    getSongs().add(output);
                    output = "";
                } else if (line.split(" ")[0].equals("Link:")) {
                    getLinks().add(line.split(" ")[1]);
                    System.out.println("added link: " + line.split(" ")[1]);
                } else {
                    output = output + line + "\n";
                }
            }
            fis.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //display the list view containing the song data using a custom adapter
        setmListView((ListView) findViewById(R.id.listViewSongs));
        System.out.println("adapter " + getmListView());

        SongListAdapter adapter = new SongListAdapter(this, getSongs());
        getmListView().setAdapter(adapter);

        //on press of a song in the list, open a dialog asking the user whether they'd
        //like to listen to the song on youtube. If so, either open up the youtube app if it's installed,
        //or open the link in a browser.
        final Context context = this;
        getmListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                System.out.println(position);
                System.out.println(getLinks().get(position).split("/")[3]);
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Listen on Youtube")
                        .setMessage("Press Listen to open a video of the chosen song on Youtube, press Return to return to the Song List")
                        .setPositiveButton("Listen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String id = getLinks().get(position).split("/")[3];
                                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(getLinks().get(position)));
                                try {
                                    startActivity(appIntent);
                                } catch (ActivityNotFoundException ex) {
                                    startActivity(webIntent);
                                }
                            }
                        })
                        .setNegativeButton("return", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }

        });
    }

    //back button function
    public void back(View view) {
        finish();
    }

    //help button function
    public void switchHelp(View view) {
        Intent intent = new Intent(this, HelpSongList.class);

        startActivity(intent);
    }

    //Getters and Setters
    public ArrayList<String> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public ListView getmListView() {
        return mListView;
    }

    public void setmListView(ListView mListView) {
        this.mListView = mListView;
    }
}
