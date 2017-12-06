package com.example.s1552344.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cz.msebera.android.httpclient.Header;

public class LoadMap extends Activity {


    private static final String TAG = "Load Map";
    String kmlMap = "";
    Song selectedSong;
    String songLyrics = "";
    ArrayList<double[]> coordinates = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> styles = new ArrayList<>();
    ConcurrentHashMap<String, Bitmap> icons = new ConcurrentHashMap<>();
    ArrayList<String[]> parsedLyrics = new ArrayList<>();
    ArrayList<Song> songList;
    String difficulty;
    volatile boolean dialogCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_load_map);
        Intent intent = getIntent();
        AsyncHttpClient client = new AsyncHttpClient();
        selectedSong = (Song)(intent.getSerializableExtra("song"));
        songList = (ArrayList<Song>) intent.getSerializableExtra("songList");
        difficulty = (String) intent.getSerializableExtra("difficulty");
        String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + selectedSong.getNumber() + "/map" +
                intent.getSerializableExtra("difficulty") + ".kml";
        Log.d(TAG, url);

        client.get(url, new

                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(TAG, "DL failure");
                        if(!dialogCreated) {
                            dialogCreated=true;
                            downloadFail();
                        }
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
        System.out.println(url);
        client.get(url, new

                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(TAG, "DL failure");
                        if(!dialogCreated) {
                            dialogCreated=true;
                            downloadFail();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d(TAG, "DL success" );
                        songLyrics = responseString;
                            switchActivity();

                    }
                }
        );

        url = "http://maps.google.com/mapfiles/kml/paddle/red-stars.png";
        client.get(url, new

                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        icons.put("veryinteresting", BitmapFactory.decodeByteArray(responseBody,0,responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(!dialogCreated) {
                            dialogCreated=true;
                            downloadFail();
                        }
                    }
                }
        );

        url = "http://maps.google.com/mapfiles/kml/paddle/orange-diamond.png";
        client.get(url, new

                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        icons.put("interesting", BitmapFactory.decodeByteArray(responseBody,0,responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(!dialogCreated) {
                            dialogCreated=true;
                            downloadFail();
                        }

                    }
                }
        );
        url = "http://maps.google.com/mapfiles/kml/paddle/ylw-circle.png";
        client.get(url, new

                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        icons.put("notboring", BitmapFactory.decodeByteArray(responseBody,0,responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        if(!dialogCreated) {
                            dialogCreated=true;
                            downloadFail();
                        }
                    }
                }
        );
        url = "http://maps.google.com/mapfiles/kml/paddle/ylw-blank.png";
        client.get(url, new

                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        icons.put("boring", BitmapFactory.decodeByteArray(responseBody,0,responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(!dialogCreated) {
                            dialogCreated=true;
                            downloadFail();
                        }
                    }
                }
        );
        url = "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png";
        client.get(url, new

                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        icons.put("unclassified", BitmapFactory.decodeByteArray(responseBody,0,responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(!dialogCreated) {
                            dialogCreated=true;
                            downloadFail();
                        }
                    }
                }
        );

    }

    public void switchActivity() {
        Log.d(TAG, "no of icons: " + icons.size());
        if(kmlMap != "" && songLyrics != "" && icons.size()==5) {

            getCoordinates();
            parseLyrics();
            Intent intent = new Intent(this, MapsActivity.class);

            for (int x = 0; x < coordinates.size(); x++) {
                Log.d(TAG, "coordinate check " + coordinates.get(x)[0] + " " + coordinates.get(x)[1]);
            }

            Log.d(TAG, "map load");
            Log.d(TAG, "icon list: " + icons);
            Log.d(TAG, "style list: " + styles);
            Log.d(TAG, "name list: " + names);
            Log.d(TAG, "coordinate list: " + coordinates);

            intent.putExtra("placemarkIcons", icons);
            intent.putExtra("placemarkStyles", styles);
            intent.putExtra("placemarkNames", names);
            intent.putExtra("placemarkCoordinates", coordinates);
            intent.putExtra("selectedSong", selectedSong);
            intent.putExtra("songLyrics", parsedLyrics);
            intent.putExtra("songList",  songList);
            intent.putExtra("difficulty", difficulty);
            Log.d(TAG, "Map loaded");
            startActivity(intent);
            finish();
        }
    }

    public void parseLyrics(){
        String lines[] = songLyrics.split("\\r?\\n");
        for (int x = 0; x < lines.length;x++){
            if(lines[x].length()>7) {
                parsedLyrics.add((lines[x].substring(7)).split(" "));
            }else{
                parsedLyrics.add(lines[x].split(" "));
            }
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
                    if(currentTag.equals("name")){
                        names.add(new String(xpp.getText()));
                    }
                    if (currentTag.equals("coordinates")) {
                        List<String> tempCoordinates = Arrays.asList(xpp.getText().split(","));
                        coordinates.add(new double[]{Double.parseDouble(tempCoordinates.get(1)), Double.parseDouble(tempCoordinates.get(0)) });
                    }
                    if (currentTag.equals("description")){
                        styles.add(new String(xpp.getText()));
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
    public void downloadFail() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
