package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.txt", new

                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        updateSongs(responseString);
                    }
                }
        );
    }

    public void updateSongs(String SongList) {
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

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
        Intent intent = new Intent(this, MapsActivity.class);

        Random generator = new Random();
        int x = generator.nextInt(getSongList().size()-1);
        Log.d(TAG, "song number " + getSongList().get(x).getNumber() + " " + x);
        intent.putExtra("song", getSongList().get(x));
        startActivity(intent);
    }

}