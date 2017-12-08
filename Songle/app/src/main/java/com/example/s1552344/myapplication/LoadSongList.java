package com.example.s1552344.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * initial activity which downloads the song list and
 * initializes necessary storage files if they do not yet exist
 */

public class LoadSongList extends Activity {

    //debug tag
    private final String TAG = "LoadSongList";
    //list data storage
    private String songXML = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_load_song_list);
        AsyncHttpClient client = new AsyncHttpClient();

        //Download the song list
        client.get("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.txt", new
                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        downloadFail();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        setSongXML(responseString);
                        switchActivity();
                    }
                }
        );
    }

    //when download finishes, switch to main and initialize data.txt,
    //solved.txt and solvedbydifficulty.txt if they don't exist yet
    public void switchActivity() {

        System.out.println("switching to main");
        File file = getBaseContext().getFileStreamPath("data.txt");

        //initialize data.txt with default values
        if (!file.exists()) {
            String filename = "data.txt";
            String string = "DistanceWalked 0\n" +
                    "PlacemarksCollected 0\n" +
                    "TotalSolved 0\n" +
                    "EasiestSolved 0\n" +
                    "EasySolved 0\n" +
                    "MediumSolved 0\n" +
                    "HardSolved 0\n" +
                    "HardestSolved 0\n" +
                    "FastestTimeEasiest 0\n" +
                    "FastestTimeEasy 0\n" +
                    "FastestTimeMedium 0\n" +
                    "FastestTimeHard 0\n" +
                    "FastestTimeHardest 0\n" +
                    "AverageTimeEasiest 0\n" +
                    "AverageTimeEasy 0\n" +
                    "AverageTimeMedium 0\n" +
                    "AverageTimeHard 0\n" +
                    "AverageTimeHardest 0\n";

            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("switching to main1");
        File file1 = getBaseContext().getFileStreamPath("solved.txt");

        //initialize solved.txt with default values
        if (!file1.exists()) {
            String filename1 = "solved.txt";
            String string1 = "Total songs found: 0";
            FileOutputStream outputStream1;

            try {
                outputStream1 = openFileOutput(filename1, Context.MODE_PRIVATE);
                outputStream1.write(string1.getBytes());
                outputStream1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File file2 = getBaseContext().getFileStreamPath("solvedbydifficulty.txt");

        //initialize solvedbydifficulty.txt with default values
        if (!file2.exists()) {
            String filename2 = "solvedbydifficulty.txt";
            String string2 = "Easiest \n" +
                    "Easy \n" +
                    "Medium \n" +
                    "Hard \n" +
                    "Hardest \n";

            FileOutputStream outputStream2;

            try {
                outputStream2 = openFileOutput(filename2, Context.MODE_PRIVATE);
                outputStream2.write(string2.getBytes());
                outputStream2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("switching to main2");
        Intent intent = new Intent(this, MainActivity.class);

        //switch to MainActivity
        intent.putExtra("songXML", getSongXML());
        Log.d(TAG, "Song XML loaded");
        startActivity(intent);
        finish();
    }

    //if download fails, prompt the user to either retry download or exit the app
    public void downloadFail() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Download failed!")
                .setMessage("Press Retry to try downloading the data anew, press Exit to close the application.")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    //getters and setters
    public String getSongXML() {
        return songXML;
    }

    public void setSongXML(String songXML) {
        this.songXML = songXML;
    }
}
