package com.example.s1552344.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Map variables
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    private static final String TAG = "MapsActivity";

    //song data
    private ArrayList<String[]> songLyrics;
    private Song selectedSong;
    private ArrayList<String> collectedWords = new ArrayList<>();
    private ArrayList<Song> songList;

    //achievement and statistics tracking
    private int difficulty = 0;
    private double distanceWalked = 0;
    private long timeSpent = 0;
    private int placemarksCollected = 0;
    private long startTime = 0;

    //placemark data
    private ArrayList<double[]> placemarkCoordinates;
    private ArrayList<String> placemarkNames;
    private ArrayList<String> placemarkStyles;
    private ArrayList<Marker> currentMarkers = new ArrayList<>();
    private HashMap<String, Bitmap> markerIcons;

    //movement data
    private double savedLatitude;
    private double savedLongitude;

    //Drawer items
    SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName("Guess Song").withSelectable(false);
    SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("View Words").withSelectable(false);
    SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("New Song").withSelectable(false);
    SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName("Main Menu").withSelectable(false);
    SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("Help").withSelectable(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        new DividerDrawerItem(),
                        item4,
                        new DividerDrawerItem(),
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d(TAG, "clicked button " + position);
                        if (position == 1) {
                            switchGuess();
                        }
                        if (position == 3) {
                            switchWords();
                        }
                        if (position == 5) {
                            switchDiffSelect();
                        }
                        if (position == 7) {
                            switchMain();
                        }
                        if (position == 9) {
                            switchHelp();
                        }
                        return true;
                    }
                })
                .build();
        result.setSelection(999);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // Long´running activities are performed asynchronously in order to
        // keep the user interface responsive
        mapFragment.getMapAsync(this);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startTime = System.currentTimeMillis();

        Intent intent = getIntent();
        difficulty = Integer.parseInt((String) intent.getSerializableExtra("difficulty"));
        selectedSong = (Song)intent.getSerializableExtra("selectedSong");
        Log.d(TAG, "selected song" + selectedSong.getNumber());
        songLyrics = (ArrayList<String[]>)intent.getSerializableExtra("songLyrics");
        Log.d(TAG, "Lyrics" + songLyrics);
        placemarkCoordinates = (ArrayList<double[]>)intent.getSerializableExtra("placemarkCoordinates");
        placemarkNames = (ArrayList<String>)intent.getSerializableExtra("placemarkNames");
        placemarkStyles = (ArrayList<String>) intent.getSerializableExtra("placemarkStyles");
        markerIcons = (HashMap<String,Bitmap>) intent.getSerializableExtra("placemarkIcons");
        songList = (ArrayList<Song>) intent.getSerializableExtra("songList");
        addMarkers();

        for(int x= 0; x < songLyrics.size();x++){
            for(int y = 0; y < songLyrics.get(x).length; y++){
                System.out.print(songLyrics.get(x)[y] + " " + "word" + " ");
            }
            System.out.println();
        }
    }

    public void addMarkers() {
        Log.d(TAG, "adding markers 1 " + markerIcons);
        if (mMap != null && markerIcons != null) {
            Log.d(TAG, "adding markers 2");
            for(int x = 0; x < placemarkNames.size(); x++){

                Log.d(TAG, "current icon " + markerIcons.get(placemarkStyles.get(x)));

                currentMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(placemarkCoordinates.get(x)[0],placemarkCoordinates.get(x)[1]))
                        .title(placemarkNames.get(x))
                        .icon(BitmapDescriptorFactory.fromBitmap(markerIcons.get(placemarkStyles.get(x))))));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        FileInputStream fis = null;
        timeSpent = (System.currentTimeMillis()-startTime)/1000;
        Log.d(TAG, "editing data");
        try {
            fis = openFileInput("data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            String output = "";
            SharedPreferences settings = getSharedPreferences("prefsFile", 0);
            boolean solved = settings.getBoolean("solved", false);
            int difSolved = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String arg = line.split(" ")[0];
                int val = Integer.parseInt(line.split(" ")[1]);

                if(arg.equals("DistanceWalked")){
                    System.out.println(distanceWalked);
                    val+=distanceWalked;
                }
                if(arg.equals("TotalSolved") && solved){
                    val++;

                }
                if(arg.equals("EasiestSolved")&& difficulty==5 && solved){
                    val++;
                    difSolved=val;
                }
                if(arg.equals("EasySolved") && difficulty==4 && solved){
                    val++;
                    difSolved=val;
                }
                if(arg.equals("MediumSolved") && difficulty==3 && solved){
                    val++;
                    difSolved=val;
                }
                if(arg.equals("HardSolved") && difficulty==2 && solved){
                    val++;
                    difSolved=val;
                }
                if(arg.equals("HardestSolved")&& difficulty==1 && solved){
                    val++;
                    difSolved=val;
                }
                if(arg.equals("FastestTimeEasiest")&& difficulty==5 && solved){
                    if(timeSpent < val || val == 0){
                        val = (int)timeSpent;
                    }
                }
                if(arg.equals("FastestTimeEasy")&& difficulty==4 && solved){
                    if(timeSpent < val || val == 0){
                        val = (int)timeSpent;
                    }
                }
                if(arg.equals("FastestTimeMedium") && difficulty==3 && solved){
                    if(timeSpent < val || val == 0){
                        val = (int)timeSpent;
                    }
                }
                if(arg.equals("FastestTimeHard")&& difficulty==2 && solved){
                    if(timeSpent < val || val == 0){
                        val = (int)timeSpent;
                    }
                }
                if(arg.equals("FastestTimeHardest") && difficulty==1 && solved){
                    if(timeSpent < val || val == 0){
                        val = (int)timeSpent;
                    }
                }
                if(arg.equals("PlacemarksCollected")){
                    val += placemarksCollected;
                }
                if(arg.equals("AverageTimeEasiest")&& difficulty==5 && solved){
                    val = (val*(difSolved-1) + (int)timeSpent)/difSolved;
                }
                if(arg.equals("AverageTimeEasy")&& difficulty==4 && solved){
                    val = (val*(difSolved-1) + (int)timeSpent)/difSolved;
                }
                if(arg.equals("AverageTimeMedium")&& difficulty==3 && solved){
                    val = (val*(difSolved-1) + (int)timeSpent)/difSolved;
                }
                if(arg.equals("AverageTimeHard")&& difficulty==2 && solved){
                    val = (val*(difSolved-1) + (int)timeSpent)/difSolved;
                }
                if(arg.equals("AverageTimeHardest")&& difficulty==1 && solved){
                    val = (val*(difSolved-1) + (int)timeSpent)/difSolved;
                }


                output = output + arg + " " + val + "\n";

            }

            System.out.println(output);
            String filename = "data.txt";

            FileOutputStream outputStream;

            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(output.getBytes());
            outputStream.close();

            boolean alreadySolved = false;
            output = "";
            fis = openFileInput("solved.txt");
            InputStreamReader isr1 = new InputStreamReader(fis);
            BufferedReader bufferedReader1 = new BufferedReader(isr1);

            while ((line = bufferedReader1.readLine()) != null) {
                System.out.println("current line: " + line);
                if(("Title: " + selectedSong.getTitle()).equals(line)){
                    alreadySolved=true;
                    break;
                }else if(line.split(" ")[0].equals("Total")){
                    output = output + "Artist: " + selectedSong.getArtist()+ "\n";
                    output = output + "Title: " + selectedSong.getTitle() + "\n";
                    output = output + "Link: " + selectedSong.getLink() + "\n\n";
                    output = output + "Total songs found: " + (Integer.parseInt(line.split(" ")[3])+1);
                    System.out.println(output);
                }else if(!line.equals("")){
                    output = output + line + "\n";
                }

                if(line.split(" ")[0].equals("Link:")){
                    output = output + "\n";
                }


            }
            if(!alreadySolved && solved==true){

                outputStream = openFileOutput("solved.txt", Context.MODE_PRIVATE);

                System.out.println("song list:\n" + output);

                outputStream.write(output.getBytes());
                outputStream.close();
            }

            output = "";
            fis = openFileInput("solvedbydifficulty.txt");
            InputStreamReader isr2 = new InputStreamReader(fis);
            BufferedReader bufferedReader2 = new BufferedReader(isr2);
            alreadySolved = false;

            if(solved==true) {
                while ((line = bufferedReader2.readLine()) != null) {
                    String arg = line.split(" ")[0];
                    String[] split = line.split(" ");
                    System.out.println(line);
                    if(difficulty == 5 && arg.equals("Easiest")){
                        for(int x = 0; x< split.length; x++){
                            if(split[x].equals(selectedSong.getNumber().toString())){
                                alreadySolved=true;
                                break;
                            }
                        }
                        if(!alreadySolved) {
                            output = output + line + " " + selectedSong.getNumber() + "\n";
                        }
                    }else if(difficulty == 4 && arg.equals("Easy")){
                        for(int x = 0; x< split.length; x++){
                            if(split[x].equals(selectedSong.getNumber().toString())){
                                alreadySolved=true;
                                break;
                            }
                        }
                        if(!alreadySolved) {
                            output = output + line + " " + selectedSong.getNumber() + "\n";
                        }
                    }else if(difficulty == 3 && arg.equals("Medium")){
                        for(int x = 0; x< split.length; x++){
                            if(split[x].equals(selectedSong.getNumber().toString())){
                                alreadySolved=true;
                                break;
                            }
                        }
                        if(!alreadySolved) {
                            output = output +  line + " " + selectedSong.getNumber() + "\n";
                        }
                    }else if(difficulty == 2 && arg.equals("Hard")){
                        for(int x = 0; x< split.length; x++){
                            if(split[x].equals(selectedSong.getNumber().toString())){
                                alreadySolved=true;
                                break;
                            }
                        }
                        if(!alreadySolved) {
                            output = output + line + " " + selectedSong.getNumber() + "\n";
                        }
                    }else if(difficulty == 1 && arg.equals("Hardest")){
                        System.out.println("hardest test");
                        for(int x = 0; x< split.length; x++) {
                            if (split[x].equals(selectedSong.getNumber().toString())) {
                                alreadySolved = true;
                                break;
                            }
                        }
                        if(!alreadySolved) {
                            output = output + line + " " + selectedSong.getNumber() + "\n";
                        }
                    }else{
                        output = output + line + "\n";
                    }

                }
                if(!alreadySolved) {
                    outputStream = openFileOutput("solvedbydifficulty.txt", Context.MODE_PRIVATE);

                    System.out.println("solved numbers:\n" + output);

                    outputStream.write(output.getBytes());
                    outputStream.close();
                }

            }
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("solved", false);
            editor.commit();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {
            System.out.println("Security exception thrown[onMapReady]");
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        addMarkers();
    }


    protected void createLocationRequest() {
        // Set the parameters for the location request
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // preferably every 5 seconds
        mLocationRequest.setFastestInterval(1000); // at most every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Can we access the user’s current location?
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            createLocationRequest();
        } catch (java.lang.IllegalStateException ise) {
            System.out.println("IllegalStateException thrown [onConnected]");
        }
        // Can we access the user’s current location?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            savedLatitude = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude();
            savedLongitude = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(Location current) {
        System.out.println(
                "[onLocationChanged] Lat / long now (" +
                        String.valueOf(current.getLatitude()) + "," +
                        String.valueOf(current.getLongitude()) + ")");
        final int R = 6371; // Radius of the earth

        double lat1 = current.getLatitude();
        double lat2 = savedLatitude;
        double lon1 = current.getLongitude();
        double lon2 = savedLongitude;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);
        distance = Math.sqrt(distance);
        distanceWalked+=distance;

        for (int x = 0; x < placemarkCoordinates.size(); x++) {
            Log.d(TAG, "current placemark  " + placemarkCoordinates.get(x)[0] + " " + placemarkCoordinates.get(x)[1]);

            lat1 = current.getLatitude();
            lat2 = placemarkCoordinates.get(x)[0];
            lon1 = current.getLongitude();
            lon2 = placemarkCoordinates.get(x)[1];

            latDistance = Math.toRadians(lat2 - lat1);
            lonDistance = Math.toRadians(lon2 - lon1);
            a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            distance = R * c * 1000; // convert to meters

            distance = Math.pow(distance, 2);
            distance = Math.sqrt(distance);
            Log.d(TAG, "distance " + distance);
            if(distance < 1500){
                String[] tempNames = placemarkNames.get(x).split(":");
                int first = Integer.parseInt(tempNames[0]);
                int second = Integer.parseInt(tempNames[1]);
                Log.d(TAG, "first and second " + first + " " + second);
                Log.d(TAG, "found word " + songLyrics.get(first-1)[second-1]);

                placemarksCollected++;
                Log.d(TAG, "number of placemarks " + placemarksCollected);
                collectedWords.add(songLyrics.get(first-1)[second-1]);

                placemarkCoordinates.remove(x);
                placemarkNames.remove(x);
                placemarkStyles.remove(x);
                currentMarkers.get(x).remove();
                currentMarkers.remove(x);
                x--;
            }


        }

    }

    @Override
    public void onConnectionSuspended(int flag) {
        System.out.println(" >>>> onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        System.out.println(" >>>> onConnectionFailed");
    }

    public void switchMain (){
        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    public void switchWords(){
        Intent intent = new Intent(this, WordsActivity.class);

        intent.putExtra("collectedWords", collectedWords);
        startActivity(intent);

    }

    public void switchDiffSelect(){
        Intent intent = new Intent (this, DifficultySelect.class);

        intent.putExtra("songList", songList);

        startActivity(intent);
        finish();
    }

    public void switchGuess(){
        Intent intent = new Intent (this, GuessSong.class);

        intent.putExtra("selectedSong", selectedSong);
        startActivity(intent);
    }
    public void switchHelp(){
        Intent intent = new Intent (this, MapsHelp.class);

        startActivity(intent);
    }
}
