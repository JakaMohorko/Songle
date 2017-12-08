package com.example.s1552344.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cz.msebera.android.httpclient.Header;

public class LoadMap extends Activity {

    //debug tag
    private static final String TAG = "Load Map";

    //variables to be parsed
    private String kmlMap = "";
    private String songLyrics = "";

    //storage variables to be sent to MapsActivity
    private Song selectedSong;
    private ArrayList<double[]> coordinates = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> styles = new ArrayList<>();
    private ConcurrentHashMap<String, Bitmap> icons = new ConcurrentHashMap<>();
    private ArrayList<String[]> parsedLyrics = new ArrayList<>();
    private ArrayList<Song> songList;
    private String difficulty;

    //check if dialog has been created
    private volatile boolean dialogCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_load_map);
        Intent intent = getIntent();
        AsyncHttpClient client = new AsyncHttpClient();

        //get data from DifficultySelect
        setSelectedSong((Song) (intent.getSerializableExtra("song")));
        setSongList((ArrayList<Song>) intent.getSerializableExtra("songList"));
        setDifficulty((String) intent.getSerializableExtra("difficulty"));

        //download map info
        String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + getSelectedSong().getNumber() + "/map" +
                intent.getSerializableExtra("difficulty") + ".kml";
        Log.d(TAG, url);
        client.get(url, new
                TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        setKmlMap(responseString);
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(TAG, "DL failure");
                        if (!isDialogCreated()) {
                            setDialogCreated(true);
                            downloadFail();
                        }
                    }
                }
        );
        //download lyric info
        url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + getSelectedSong().getNumber() + "/words.txt";
        System.out.println(url);
        client.get(url, new
                TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d(TAG, "DL success");
                        setSongLyrics(responseString);
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(TAG, "DL failure");
                        if (!isDialogCreated()) {
                            //if download fails, if no dialog has been displayed yet, call downloadFail
                            setDialogCreated(true);
                            downloadFail();
                        }
                    }
                }
        );
        //download icon for very interesting words
        url = "http://maps.google.com/mapfiles/kml/paddle/red-stars.png";
        client.get(url, new
                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        getIcons().put("veryinteresting", BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (!isDialogCreated()) {
                            //if download fails, if no dialog has been displayed yet, call downloadFail
                            setDialogCreated(true);
                            downloadFail();
                        }
                    }
                }
        );
        //download icon for interesting words
        url = "http://maps.google.com/mapfiles/kml/paddle/orange-diamond.png";
        client.get(url, new
                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        getIcons().put("interesting", BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (!isDialogCreated()) {
                            //if download fails, if no dialog has been displayed yet, call downloadFail
                            setDialogCreated(true);
                            downloadFail();
                        }
                    }
                }
        );
        //download icon for not boring words
        url = "http://maps.google.com/mapfiles/kml/paddle/ylw-circle.png";
        client.get(url, new
                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        getIcons().put("notboring", BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (!isDialogCreated()) {
                            //if download fails, if no dialog has been displayed yet, call downloadFail
                            setDialogCreated(true);
                            downloadFail();
                        }
                    }
                }
        );
        //download icon for boring words
        url = "http://maps.google.com/mapfiles/kml/paddle/ylw-blank.png";
        client.get(url, new
                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        getIcons().put("boring", BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (!isDialogCreated()) {
                            //if download fails, if no dialog has been displayed yet, call downloadFail
                            setDialogCreated(true);
                            downloadFail();
                        }
                    }
                }
        );
        //download icon for unclassified words
        url = "http://maps.google.com/mapfiles/kml/paddle/wht-blank.png";
        client.get(url, new
                AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        getIcons().put("unclassified", BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));
                        switchActivity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (!isDialogCreated()) {
                            //if download fails, if no dialog has been displayed yet, call downloadFail
                            setDialogCreated(true);
                            downloadFail();
                        }
                    }
                }
        );

    }

    public void switchActivity() {
        Log.d(TAG, "no of icons: " + getIcons().size());
        if (getKmlMap() != "" && getSongLyrics() != "" && getIcons().size() == 5) {

            //parse the coordinates of the placemarks downloaded and save them
            getCoords();
            //parse the lyrics of the song downloaded and save them
            parseLyrics();

            Intent intent = new Intent(this, MapsActivity.class);

            for (int x = 0; x < getCoordinates().size(); x++) {
                Log.d(TAG, "coordinate check " + getCoordinates().get(x)[0] + " " + getCoordinates().get(x)[1]);
            }

            Log.d(TAG, "map load");
            Log.d(TAG, "icon list: " + getIcons());
            Log.d(TAG, "style list: " + getStyles());
            Log.d(TAG, "name list: " + getNames());
            Log.d(TAG, "coordinate list: " + getCoordinates());

            //send data to MapsActivity
            intent.putExtra("placemarkIcons", getIcons());
            intent.putExtra("placemarkStyles", getStyles());
            intent.putExtra("placemarkNames", getNames());
            intent.putExtra("placemarkCoordinates", getCoordinates());
            intent.putExtra("selectedSong", getSelectedSong());
            intent.putExtra("songLyrics", getParsedLyrics());
            intent.putExtra("songList", getSongList());
            intent.putExtra("difficulty", getDifficulty());

            Log.d(TAG, "Map loaded");
            startActivity(intent);
            finish();
        }
    }

    //parses the lyrics and stores separate words in a list
    public void parseLyrics() {
        String lines[] = getSongLyrics().split("\\r?\\n");
        for (int x = 0; x < lines.length; x++) {
            if (lines[x].length() > 7) {
                getParsedLyrics().add((lines[x].substring(7)).split(" "));
            } else {
                getParsedLyrics().add(lines[x].split(" "));
            }
        }

    }

    //parses the map data and stores the coordinates of markers to be placed on the map
    //in addition to the styles of the markers
    public void getCoords() {
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(getKmlMap()));
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
                    Log.d(TAG, "Text " + xpp.getText());
                    if (currentTag.equals("name")) {
                        getNames().add(new String(xpp.getText()));
                    }
                    if (currentTag.equals("coordinates")) {
                        List<String> tempCoordinates = Arrays.asList(xpp.getText().split(","));
                        getCoordinates().add(new double[]{Double.parseDouble(tempCoordinates.get(1)), Double.parseDouble(tempCoordinates.get(0))});
                    }
                    if (currentTag.equals("description")) {
                        getStyles().add(new String(xpp.getText()));
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

    //display a dialog asking the user to retry download or return to main menu if download fails
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //getters and setters
    public String getKmlMap() {
        return kmlMap;
    }

    public void setKmlMap(String kmlMap) {
        this.kmlMap = kmlMap;
    }

    public String getSongLyrics() {
        return songLyrics;
    }

    public void setSongLyrics(String songLyrics) {
        this.songLyrics = songLyrics;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public ArrayList<double[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<double[]> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getStyles() {
        return styles;
    }

    public void setStyles(ArrayList<String> styles) {
        this.styles = styles;
    }

    public ConcurrentHashMap<String, Bitmap> getIcons() {
        return icons;
    }

    public void setIcons(ConcurrentHashMap<String, Bitmap> icons) {
        this.icons = icons;
    }

    public ArrayList<String[]> getParsedLyrics() {
        return parsedLyrics;
    }

    public void setParsedLyrics(ArrayList<String[]> parsedLyrics) {
        this.parsedLyrics = parsedLyrics;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isDialogCreated() {
        return dialogCreated;
    }

    public void setDialogCreated(boolean dialogCreated) {
        this.dialogCreated = dialogCreated;
    }
}
