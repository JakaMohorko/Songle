package com.example.s1552344.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    private static final String TAG = "Song list parse";

    //list of songs
    private static ArrayList<Song> SongList = new ArrayList<>();

    //Getters and Setters
    public static ArrayList<Song> getSongList() {
        return SongList;
    }

    public static void setSongList(ArrayList<Song> songList) {
        SongList = songList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        String songXML = (String) intent.getSerializableExtra("songXML");
        updateSongs(songXML);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    public void updateSongs(String SongList) {
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            getSongList().clear();
            xpp.setInput(new StringReader(SongList)); // pass input whatever xml you have
            int eventType = xpp.getEventType();
            String currentTag = "";
            int songCounter = -1;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(TAG, "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    Log.d(TAG, "Start tag " + xpp.getName());
                    currentTag = xpp.getName();
                    if (xpp.getName().equals("Song")) {
                        getSongList().add(new Song());
                        songCounter++;
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d(TAG, "End tag " + xpp.getName());
                    currentTag = "";
                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d(TAG, "Text " + xpp.getText()); // here you get the text from xml
                    if (currentTag.equals("Number")) {
                        //Log.d(TAG, "Number " + xpp.getText());
                        getSongList().get(songCounter).setNumber(xpp.getText());
                        Log.d(TAG, "Number is " + getSongList().get(songCounter).getNumber());
                    }
                    if (currentTag.equals("Artist")) {
                        getSongList().get(songCounter).setArtist(xpp.getText());
                    }
                    if (currentTag.equals("Title")) {
                        getSongList().get(songCounter).setTitle(xpp.getText());
                    }
                    if (currentTag.equals("Link")) {
                        getSongList().get(songCounter).setLink(xpp.getText());
                    }
                }
                eventType = xpp.next();
            }
            Log.d(TAG, "End document");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void switchActivity (View view){
        Intent intent = new Intent(this, DifficultySelect.class);
        for(int x = 0; x < getSongList().size(); x++){
            System.out.println("song number " + getSongList().get(x).getNumber());
        }


        intent.putExtra("songList", getSongList());

        startActivity(intent);
    }



    public void switchStatistics (View view){
        Intent intent = new Intent(this, GameplayStatistics.class);

        startActivity(intent);
    }
    public void switchSongs (View view){
        Intent intent = new Intent(this, SongList.class);

        FileInputStream fis = null;
        String output = "";
        try {
            fis = openFileInput("solved.txt");

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                output = output + line + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        intent.putExtra("songs", output);
        startActivity(intent);
    }
    public void switchAchievements (View view){
        Intent intent = new Intent(this, Achievements.class);

        startActivity(intent);
    }
    public void exit (View view){
        finish();

    }

    public void switchHelp (View view){
        Intent intent = new Intent(this, HelpMain.class);

        startActivity(intent);

    }

}
