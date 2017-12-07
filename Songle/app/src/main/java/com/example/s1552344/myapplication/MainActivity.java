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

/**
 * Main menu activity of the app.
 * The user can start a new game from this activity, as well
 * as access various information activities (statistics, achievements...)
 */

public class MainActivity extends Activity {

    //debug tag
    private static final String TAG = "Song list parse";

    //list of songs
    private static ArrayList<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        String songXML = (String) intent.getSerializableExtra("songXML");

        //generate an array list of songs containing their data (artist, link, number, title)
        updateSongs(songXML);
    }


    //parse the SongList string and separate the data to be added
    //into the songList array list
    public void updateSongs(String SongList) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            songList.clear();
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
                        songList.add(new Song());
                        songCounter++;
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d(TAG, "End tag " + xpp.getName());
                    currentTag = "";
                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d(TAG, "Text " + xpp.getText()); // here you get the text from xml
                    if (currentTag.equals("Number")) {
                        //Log.d(TAG, "Number " + xpp.getText());
                        songList.get(songCounter).setNumber(xpp.getText());
                        Log.d(TAG, "Number is " + songList.get(songCounter).getNumber());
                    }
                    if (currentTag.equals("Artist")) {
                        songList.get(songCounter).setArtist(xpp.getText());
                    }
                    if (currentTag.equals("Title")) {
                        songList.get(songCounter).setTitle(xpp.getText());
                    }
                    if (currentTag.equals("Link")) {
                        songList.get(songCounter).setLink(xpp.getText());
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

    //switch to the DifficultySelect activity, passing along the song list
    public void switchActivity (View view){
        Intent intent = new Intent(this, DifficultySelect.class);
        for(int x = 0; x < songList.size(); x++){
            System.out.println("song number " + songList.get(x).getNumber());
        }

        intent.putExtra("songList", songList);

        startActivity(intent);
    }

    //on button press  functions

    //switch to the GameplayStatistics activity
    public void switchStatistics (View view){
        Intent intent = new Intent(this, GameplayStatistics.class);

        startActivity(intent);
    }
    //switch to the SongList activity
    public void switchSongs (View view){
        Intent intent = new Intent(this, SongList.class);

        startActivity(intent);
    }
    //switch to the Achievements activity
    public void switchAchievements (View view){
        Intent intent = new Intent(this, Achievements.class);

        startActivity(intent);
    }
    //quit the app
    public void exit (View view){
        finish();
    }
    //switch to the Help Main activity
    public void switchHelp (View view){
        Intent intent = new Intent(this, HelpMain.class);

        startActivity(intent);

    }

}
