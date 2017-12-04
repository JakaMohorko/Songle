package com.example.s1552344.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class DifficultySelect extends Activity {

    Song selectedSong;
    private static final String TAG = "Difficulty Select";
    private String difficulty = "";
    ArrayList<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_difficulty_select);
        Intent intent = getIntent();

        songList = (ArrayList<Song>) intent.getSerializableExtra("songList");
    }

    public void easiest(View view){
        difficulty = "5";
        switchActivity(view);
    }
    public void easy(View view){
        difficulty = "4";
        switchActivity(view);
    }
    public void medium(View view){
        difficulty = "3";
        switchActivity(view);
    }
    public void hard(View view){
        difficulty = "2";
        switchActivity(view);
    }
    public void hardest(View view){
        difficulty = "1";
        switchActivity(view);
    }

    public void switchActivity (View view) {
        Intent intent = new Intent(this, LoadMap.class);
        String line;
        FileInputStream fis = null;
        try {
            fis = openFileInput("solvedbydifficulty.txt");
            InputStreamReader isr2 = new InputStreamReader(fis);
            BufferedReader bufferedReader2 = new BufferedReader(isr2);
            String[] alreadySolved = new String[0];

            while ((line = bufferedReader2.readLine()) != null) {
                String arg = line.split(" ")[0];
                String[] split = line.split(" ");
                System.out.println(line);
                if (difficulty.equals("5") && arg.equals("Easiest")) {
                    alreadySolved = line.split(" ");
                    System.out.println("easiest check");
                } else if (difficulty.equals("4") && arg.equals("Easy")) {
                    alreadySolved = line.split(" ");
                } else if (difficulty.equals("3") && arg.equals("Medium")) {
                    alreadySolved = line.split(" ");
                } else if (difficulty.equals("2") && arg.equals("Hard")) {
                    alreadySolved = line.split(" ");
                } else if (difficulty.equals("1") && arg.equals("Hardest")) {
                    alreadySolved = line.split(" ");
                }
            }
            isr2.close();
            bufferedReader2.close();
            fis.close();

            if(alreadySolved.length == songList.size()+2){
                new MaterialDialog.Builder(this)

                        .title("No songs left")
                        .content("All of the songs currently available have already been beaten at the selected difficulty level. Please pick a different difficulty.")
                        .positiveText("Return")
                        .show();
                return;
            }
            if(alreadySolved.length>1){
                boolean newSong = false;
                System.out.println("find new song");
                Random generator = new Random();
                String cur = "";
                int x = generator.nextInt(songList.size() - 1);
                if(x==0){
                    x++;
                }
                while(true) {

                    for(int y = 1; y < alreadySolved.length;y++){

                        if(x<10){
                            cur = "0" + Integer.toString(x);

                        }else{
                            cur = Integer.toString(x);
                        }
                        System.out.println("trying number " + cur + " " + alreadySolved[y]);
                        if (alreadySolved[y].equals(cur)){
                            if(x == songList.size()){
                                x=1;
                            }else {
                                System.out.println("printing x " + x);
                                x++;
                            }
                            break;
                        }
                        if(y == alreadySolved.length-1){
                            newSong=true;
                        }
                    }
                    if(newSong){
                        intent.putExtra("song", songList.get(x-1));
                        break;
                    }
                }
            }else {
                System.out.println("random");
                Random generator = new Random();
                int x = generator.nextInt(songList.size() - 1);
                intent.putExtra("song", songList.get(x));
            }
            intent.putExtra("difficulty", difficulty);
            intent.putExtra("songList", songList);
            Log.d(TAG, "Difficulty " + difficulty);
            startActivity(intent);
            finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void switchHelp (View view){
        Intent intent = new Intent(this, HelpDifficulty.class);

        startActivity(intent);
    }

    public void back (View view){
        finish();
    }



}
