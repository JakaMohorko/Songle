package com.example.s1552344.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SongList extends AppCompatActivity {

    private ArrayList<String> songs = new ArrayList<>();
    private ArrayList<String> links = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song_list);

        FileInputStream fis = null;
        String output = "";
        try {
            fis = openFileInput("solved.txt");

            int x = 1;
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if(line.split(" ")[0].equals("Best")){
                    int time = Integer.parseInt(line.split(" ")[2]);
                    output = output + "Best time: " + time/60 + " min " + (time-((time/60)*60))+ " s"+ "\n";
                }else if(line.split(" ")[0].equals("Highest")) {
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
                    songs.add(output);
                    output = "";
                }else if(line.split(" ")[0].equals("Link:")){
                    links.add(line.split(" ")[1]);
                    System.out.println("added link: " + line.split(" ")[1]);
                }
                else{
                    output = output + line + "\n";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mListView = (ListView) findViewById(R.id.listViewSongs);
        System.out.println("adapter " + mListView);

        SongListAdapter adapter = new SongListAdapter(this, songs);
        mListView.setAdapter(adapter);

        final Context context = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                System.out.println(position);
                System.out.println(links.get(position).split("/")[3]);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Listen on Youtube")
                        .setMessage("Press Listen to open a video of the chosen song on Youtube, press Return to return to the Song List")
                        .setPositiveButton("Listen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String id = links.get(position).split("/")[3];
                                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(links.get(position)));
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
    public void back (View view){
        finish();
    }

    public void switchHelp(View view){
        Intent intent = new Intent(this, HelpSongList.class);

        startActivity(intent);
    }

}
