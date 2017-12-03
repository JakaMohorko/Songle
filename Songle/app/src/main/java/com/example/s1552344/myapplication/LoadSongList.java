package com.example.s1552344.myapplication;

import android.app.Activity;
import android.content.Context;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class LoadSongList extends Activity {

    String songXML = "";
    private final String TAG = "LoadSongList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_load_song_list);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.txt", new
                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        downloadFail();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        songXML = responseString;
                        switchActivity();
                    }
                }

        );

    }
    public void switchActivity () {

        System.out.println("switching to main");
        File file = getBaseContext().getFileStreamPath("data.txt");

        if (!file.exists()) {
            String filename = "data.txt";
            String string = "DistanceWalked 0\n" +
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
                    "PlacemarksCollected 0\n" +
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

            if(!file1.exists()) {
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

        if(!file2.exists()) {
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

            intent.putExtra("songXML", songXML);
            Log.d(TAG, "Song XML loaded");
            startActivity(intent);
            finish();
        }

        public void downloadFail(){
            new MaterialDialog.Builder(this)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            recreate();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .canceledOnTouchOutside(true)
                    .cancelListener( new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .title("Download failed")
                    .content("Press Retry to try downloading the data anew, press Exit to close the application.")
                    .positiveText("Retry")
                    .negativeText("Exit")
                    .show();
        }
}
