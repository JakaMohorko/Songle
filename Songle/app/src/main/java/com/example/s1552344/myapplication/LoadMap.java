package com.example.s1552344.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LoadMap extends AppCompatActivity {


    private static final String TAG = "Load Map";
    String kmlMap = "";
    Song selectedSong;
    String songLyrics = "";
    ArrayList<double[]> coordinates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_map);
        Intent intent = getIntent();
        AsyncHttpClient client = new AsyncHttpClient();
        selectedSong = (Song)(intent.getSerializableExtra("song"));
        String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + selectedSong.getNumber() + "/map" +
                intent.getSerializableExtra("difficulty") + ".kml";
        Log.d(TAG, url);

        client.get(url, new

                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(TAG, "DL failure");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                        kmlMap = responseString;
                        Log.d(TAG, "DL success");
                        switchActivity();

                    }
                }
        );

        url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + selectedSong.getNumber() + "/words.txt";
        client.get(url, new

                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(TAG, "DL failure");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d(TAG, "DL success");
                        songLyrics = responseString;
                            switchActivity();

                    }
                }
        );
    }

    public void switchActivity() {
        if(kmlMap != "" && songLyrics != "") {

            getCoordinates();

            Intent intent = new Intent(this, MapsActivity.class);

            for (int x = 0; x < coordinates.size(); x++) {
                Log.d(TAG, "coordinate check " + coordinates.get(x)[0] + " " + coordinates.get(x)[1]);
            }

            Log.d(TAG, "map load");
            intent.putExtra("placemarkCoordinates", coordinates);
            intent.putExtra("kmlMap", kmlMap);
            intent.putExtra("selectedSong", selectedSong);
            intent.putExtra("songLyrics", songLyrics);
            Log.d(TAG, "Map loaded");
            startActivity(intent);
        }
    }

    public void getCoordinates(){
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(kmlMap)); // pass input whatever xml you have
            int eventType = xpp.getEventType();
            String currentTag = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(TAG, "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    Log.d(TAG, "Start tag " + xpp.getName());
                    currentTag = xpp.getName();
                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d(TAG, "End tag " + xpp.getName());
                    currentTag = "";
                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d(TAG, "Text " + xpp.getText()); // here you get the text from xml
                    if (currentTag.equals("coordinates")) {
                        List<String> tempCoordinates = Arrays.asList(xpp.getText().split(","));
                        coordinates.add(new double[]{Double.parseDouble(tempCoordinates.get(1)), Double.parseDouble(tempCoordinates.get(0)) });
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
}
