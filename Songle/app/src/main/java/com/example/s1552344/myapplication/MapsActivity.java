package com.example.s1552344.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main gameplay activity.
 * Here the user can collect placemarks and finally guess which song is being played.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //debug tag
    private static final String TAG = "MapsActivity";
    //Map variables
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean cameraTracking = false;
    private boolean firstPan = false;

    //song data
    private ArrayList<String[]> songLyrics;
    private Song selectedSong;
    private ArrayList<String> collectedWords = new ArrayList<>();
    private ArrayList<Song> songList;

    //test vars
    private String songTitle;

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

    //navigation drawer
    private Drawer result;

    //movement data
    private double savedLatitude;
    private double savedLongitude;

    //Drawer items
    private SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName("Guess Song").withSelectable(false);
    private SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("View Words").withSelectable(false);
    private SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("New Song").withSelectable(false);
    private SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName("Main Menu").withSelectable(false);
    private SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("Help").withSelectable(false);
    private SecondaryDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(6).withName("Enable Camera Tracking");
    private SecondaryDrawerItem item7 = new SecondaryDrawerItem().withIdentifier(7).withName("Disable Camera Tracking").withSetSelected(true);

    public static boolean isLocationEnabled(Context context) {
        //checks whether location is enabled or not
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);

        //Create side navigation drawer
        setResult(new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        getItem1(),
                        new DividerDrawerItem(),
                        getItem2(),
                        new DividerDrawerItem(),
                        getItem3(),
                        new DividerDrawerItem(),
                        getItem4(),
                        new DividerDrawerItem(),
                        getItem5(),
                        new DividerDrawerItem(),
                        getItem6(),
                        getItem7(),
                        new DividerDrawerItem()
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
                            difficultyCheck();
                        }
                        if (position == 7) {
                            exitCheck();
                        }
                        if (position == 9) {
                            switchHelp();
                        }
                        if (position == 11) {
                            setCameraTracking(true);
                        }
                        if (position == 12) {
                            setCameraTracking(false);
                        }
                        return true;
                    }
                })
                .build());

        //set none of the buttons as selected
        getResult().setSelection(999);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // Long´running activities are performed asynchronously in order to
        // keep the user interface responsive
        mapFragment.getMapAsync(this);
        // Create an instance of GoogleAPIClient.
        if (getmGoogleApiClient() == null) {
            setmGoogleApiClient(new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build());
        }

        //log starting time of the play session
        setStartTime(System.currentTimeMillis());

        Intent intent = getIntent();
        //obtained all extras passed from previous activities
        setSelectedSong((Song) intent.getSerializableExtra("selectedSong"));
        setDifficulty(Integer.parseInt((String) intent.getSerializableExtra("difficulty")));
        setSongLyrics((ArrayList<String[]>) intent.getSerializableExtra("songLyrics"));
        setPlacemarkCoordinates((ArrayList<double[]>) intent.getSerializableExtra("placemarkCoordinates"));
        setPlacemarkNames((ArrayList<String>) intent.getSerializableExtra("placemarkNames"));
        setPlacemarkStyles((ArrayList<String>) intent.getSerializableExtra("placemarkStyles"));
        setMarkerIcons((HashMap<String, Bitmap>) intent.getSerializableExtra("placemarkIcons"));
        setSongList((ArrayList<Song>) intent.getSerializableExtra("songList"));
        setSongTitle(getSelectedSong().getTitle());

        Log.d(TAG, "Lyrics" + getSongLyrics());
        Log.d(TAG, "selected song" + getSelectedSong().getNumber());

        //ask the user to turn on location, if it is off and return to main menu
        final Context context = this;
        if (!isLocationEnabled(this)) {
            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("Location disabled")
                    .setMessage("Please turn Location on and start a new game.")
                    .setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switchMain();
                        }
                    })
                    .setNegativeButton("Location Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(myIntent);
                            finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(myIntent);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        //Make sure that the current state of the song being played is marked as unsolved
        //in shared preferences
        SharedPreferences settings = getSharedPreferences("prefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("solved", false);
        editor.commit();

        //add markers to the map
        addMarkers();

    }

    //adds markers at the coordinates obtained from MapDownload with correct styles,
    //if both the map has been created and markerIcons initialized
    public void addMarkers() {
        Log.d(TAG, "adding markers 1 " + getMarkerIcons());
        if (getmMap() != null && getMarkerIcons() != null) {
            Log.d(TAG, "adding markers 2");
            for (int x = 0; x < getPlacemarkNames().size(); x++) {

                Log.d(TAG, "current icon " + getMarkerIcons().get(getPlacemarkStyles().get(x)));

                getCurrentMarkers().add(getmMap().addMarker(new MarkerOptions().position(new LatLng(getPlacemarkCoordinates().get(x)[0], getPlacemarkCoordinates().get(x)[1]))
                        .title(getPlacemarkNames().get(x))
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerIcons().get(getPlacemarkStyles().get(x))))));
            }
        }
        System.out.println(getSelectedSong().getTitle());
    }

    //connect the google api client
    @Override
    protected void onStart() {
        super.onStart();
        getmGoogleApiClient().connect();
    }

    //on stop disconnect the api client
    @Override
    protected void onStop() {
        super.onStop();
        if (getmGoogleApiClient().isConnected()) {
            getmGoogleApiClient().disconnect();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        FileInputStream fis = null;
        setTimeSpent((System.currentTimeMillis() - getStartTime()) / 1000);
        Log.d(TAG, "editing data");
        try {

            //when the user returns to the main menu, or finishes a song, update all the data
            //files stored on the device
            fis = openFileInput("data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            String output = "";

            //check whether the song has been completed when on destroy was called.
            SharedPreferences settings = getSharedPreferences("prefsFile", 0);
            boolean solved = settings.getBoolean("solved", false);

            int difSolved = 0;

            //update statistic data with information collected during the play session.
            //update certain statistics only if the puzzle was solved.
            while ((line = bufferedReader.readLine()) != null) {
                String arg = line.split(" ")[0];
                int val = Integer.parseInt(line.split(" ")[1]);

                if (arg.equals("DistanceWalked")) {
                    System.out.println(getDistanceWalked());
                    val += getDistanceWalked();
                }
                if (arg.equals("TotalSolved") && solved) {
                    val++;

                }
                if (arg.equals("EasiestSolved") && getDifficulty() == 5 && solved) {
                    val++;
                    difSolved = val;
                }
                if (arg.equals("EasySolved") && getDifficulty() == 4 && solved) {
                    val++;
                    difSolved = val;
                }
                if (arg.equals("MediumSolved") && getDifficulty() == 3 && solved) {
                    val++;
                    difSolved = val;
                }
                if (arg.equals("HardSolved") && getDifficulty() == 2 && solved) {
                    val++;
                    difSolved = val;
                }
                if (arg.equals("HardestSolved") && getDifficulty() == 1 && solved) {
                    val++;
                    difSolved = val;
                }
                if (arg.equals("FastestTimeEasiest") && getDifficulty() == 5 && solved) {
                    if (getTimeSpent() < val || val == 0) {
                        val = (int) getTimeSpent();
                    }
                }
                if (arg.equals("FastestTimeEasy") && getDifficulty() == 4 && solved) {
                    if (getTimeSpent() < val || val == 0) {
                        val = (int) getTimeSpent();
                    }
                }
                if (arg.equals("FastestTimeMedium") && getDifficulty() == 3 && solved) {
                    if (getTimeSpent() < val || val == 0) {
                        val = (int) getTimeSpent();
                    }
                }
                if (arg.equals("FastestTimeHard") && getDifficulty() == 2 && solved) {
                    if (getTimeSpent() < val || val == 0) {
                        val = (int) getTimeSpent();
                    }
                }
                if (arg.equals("FastestTimeHardest") && getDifficulty() == 1 && solved) {
                    if (getTimeSpent() < val || val == 0) {
                        val = (int) getTimeSpent();
                    }
                }
                if (arg.equals("PlacemarksCollected")) {
                    val += getPlacemarksCollected();
                }
                if (arg.equals("AverageTimeEasiest") && getDifficulty() == 5 && solved) {
                    val = (val * (difSolved - 1) + (int) getTimeSpent()) / difSolved;
                }
                if (arg.equals("AverageTimeEasy") && getDifficulty() == 4 && solved) {
                    val = (val * (difSolved - 1) + (int) getTimeSpent()) / difSolved;
                }
                if (arg.equals("AverageTimeMedium") && getDifficulty() == 3 && solved) {
                    val = (val * (difSolved - 1) + (int) getTimeSpent()) / difSolved;
                }
                if (arg.equals("AverageTimeHard") && getDifficulty() == 2 && solved) {
                    val = (val * (difSolved - 1) + (int) getTimeSpent()) / difSolved;
                }
                if (arg.equals("AverageTimeHardest") && getDifficulty() == 1 && solved) {
                    val = (val * (difSolved - 1) + (int) getTimeSpent()) / difSolved;
                }


                output = output + arg + " " + val + "\n";

            }

            //write the changed data to tata.txt
            System.out.println(output);
            String filename = "data.txt";

            FileOutputStream outputStream;

            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(output.getBytes());
            outputStream.close();

            //read and update the solved song data file if the currently played song was completed
            boolean alreadySolved = false;
            output = "";
            fis = openFileInput("solved.txt");
            InputStreamReader isr1 = new InputStreamReader(fis);
            BufferedReader bufferedReader1 = new BufferedReader(isr1);

            while ((line = bufferedReader1.readLine()) != null) {
                System.out.println("current line: " + line);

                //if the currently played song has been solved, update the data on it
                if (("Title: " + getSelectedSong().getTitle()).equals(line)) {
                    System.out.println("Song already in list" + getSelectedSong().getTitle());
                    alreadySolved = true;
                    output = output + line + "\n";

                    //read and adjust Best Time line
                    line = bufferedReader1.readLine();
                    int val = Integer.parseInt(line.split(" ")[2]);
                    if (getTimeSpent() < val) {
                        output = output + "Best time: " + getTimeSpent() + "\n";
                    } else {
                        output = output + line + "\n";
                    }

                    //read and write the link line
                    line = bufferedReader1.readLine();
                    output = output + line + "\n";

                    //read and adjust the Highest difficulty line
                    line = bufferedReader1.readLine();
                    val = Integer.parseInt(line.split(" ")[2]);
                    if (val > getDifficulty()) {
                        output = output + "Highest difficulty: " + getDifficulty() + "\n";
                    } else {
                        output = output + line + "\n";

                    }
                } else if (line.split(" ")[0].equals("Total") && !alreadySolved) {
                    //if the song hasn't been completed yet and we're at the last line,
                    //add the song data to the file
                    output = output + "Artist: " + getSelectedSong().getArtist() + "\n";
                    output = output + "Title: " + getSelectedSong().getTitle() + "\n";
                    output = output + "Best time: " + getTimeSpent() + "\n";
                    output = output + "Link: " + getSelectedSong().getLink() + "\n";
                    output = output + "Highest difficulty: " + getDifficulty() + "\n\n";
                    output = output + "Total songs found: " + (Integer.parseInt(line.split(" ")[3]) + 1) + "\n";
                    System.out.println(output);
                } else if (!line.equals("")) {
                    output = output + line + "\n";
                }

                if (line.split(" ")[0].equals("Highest")) {
                    output = output + "\n";
                }


            }
            if (solved == true) {
                //if the currently played song was completed, update the solved song data file
                outputStream = openFileOutput("solved.txt", Context.MODE_PRIVATE);

                System.out.println("song list:\n" + output);

                outputStream.write(output.getBytes());
                outputStream.close();
            }

            //read and update the already solved songs listed by difficulty data file,
            //if the song currently being played has been completed.
            output = "";
            fis = openFileInput("solvedbydifficulty.txt");
            InputStreamReader isr2 = new InputStreamReader(fis);
            BufferedReader bufferedReader2 = new BufferedReader(isr2);
            alreadySolved = false;

            if (solved == true) {
                while ((line = bufferedReader2.readLine()) != null) {
                    String arg = line.split(" ")[0];
                    String[] split = line.split(" ");
                    System.out.println(line);
                    //for each difficulty, check the difficulty of the song currently being played.
                    //if it matches, check if the song was already completed at that difficulty.
                    //if not, add its number to that difficulty category
                    if (getDifficulty() == 5 && arg.equals("Easiest")) {
                        for (int x = 0; x < split.length; x++) {
                            if (split[x].equals(getSelectedSong().getNumber().toString())) {
                                alreadySolved = true;
                                break;
                            }
                        }
                        if (!alreadySolved) {
                            output = output + line + " " + getSelectedSong().getNumber() + "\n";
                        }
                    } else if (getDifficulty() == 4 && arg.equals("Easy")) {
                        for (int x = 0; x < split.length; x++) {
                            if (split[x].equals(getSelectedSong().getNumber().toString())) {
                                alreadySolved = true;
                                break;
                            }
                        }
                        if (!alreadySolved) {
                            output = output + line + " " + getSelectedSong().getNumber() + "\n";
                        }
                    } else if (getDifficulty() == 3 && arg.equals("Medium")) {
                        for (int x = 0; x < split.length; x++) {
                            if (split[x].equals(getSelectedSong().getNumber().toString())) {
                                alreadySolved = true;
                                break;
                            }
                        }
                        if (!alreadySolved) {
                            output = output + line + " " + getSelectedSong().getNumber() + "\n";
                        }
                    } else if (getDifficulty() == 2 && arg.equals("Hard")) {
                        for (int x = 0; x < split.length; x++) {
                            if (split[x].equals(getSelectedSong().getNumber().toString())) {
                                alreadySolved = true;
                                break;
                            }
                        }
                        if (!alreadySolved) {
                            output = output + line + " " + getSelectedSong().getNumber() + "\n";
                        }
                    } else if (getDifficulty() == 1 && arg.equals("Hardest")) {
                        System.out.println("hardest test");
                        for (int x = 0; x < split.length; x++) {
                            if (split[x].equals(getSelectedSong().getNumber().toString())) {
                                alreadySolved = true;
                                break;
                            }
                        }
                        if (!alreadySolved) {
                            output = output + line + " " + getSelectedSong().getNumber() + "\n";
                        }
                    } else {
                        output = output + line + "\n";
                    }
                }

                if (!alreadySolved) {
                    //if the song currently being played hasn't been solved yet, udpate solvedbydifficulty.txt
                    outputStream = openFileOutput("solvedbydifficulty.txt", Context.MODE_PRIVATE);

                    System.out.println("solved numbers:\n" + output);

                    outputStream.write(output.getBytes());
                    outputStream.close();
                }

            }
            //set the solved variable in SharedPreferences back to false for the next song.
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("solved", false);
            editor.commit();


            fis.close();
            bufferedReader.close();
            bufferedReader1.close();
            bufferedReader2.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //on map ready, enable your location and location button and add markers to the map
        setmMap(googleMap);

        try {
            getmMap().setMyLocationEnabled(true);
        } catch (SecurityException se) {
            System.out.println("Security exception thrown[onMapReady]");

        }
        getmMap().getUiSettings().setMyLocationButtonEnabled(true);
        addMarkers();


    }

    protected void createLocationRequest() {
        // Set the parameters for the location request
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000); // preferably every 3 seconds
        mLocationRequest.setFastestInterval(1000); // at most every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Can we access the user’s current location?
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(getmGoogleApiClient(), mLocationRequest, this);
        }
    }

    @Override
    public void onBackPressed() {
        //check if the user wants to return to the main menu
        exitCheck();
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
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    getPERMISSIONS_REQUEST_ACCESS_FINE_LOCATION());

        }
    }


    @Override
    public void onLocationChanged(Location current) {

        //on the first change of location, always pan to the user and save the current LatLon
        if (!isFirstPan()) {
            setSavedLatitude(current.getLatitude());
            setSavedLongitude(current.getLongitude());
            LatLng latLng = new LatLng(getSavedLatitude(), getSavedLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            getmMap().animateCamera(cameraUpdate);
            setFirstPan(true);
        }

        System.out.println(
                "[onLocationChanged] Lat / long now (" +
                        String.valueOf(current.getLatitude()) + "," +
                        String.valueOf(current.getLongitude()) + ")");

        //calculate the distance moved since last OnLocationChanged call
        //using the haversine formula

        final int R = 6371; // Radius of the earth

        double lat1 = current.getLatitude();
        double lat2 = getSavedLatitude();
        double lon1 = current.getLongitude();
        double lon2 = getSavedLongitude();

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);
        distance = Math.sqrt(distance);
        setDistanceWalked(getDistanceWalked() + distance);

        setSavedLatitude(current.getLatitude());
        setSavedLongitude(current.getLongitude());

        for (int x = 0; x < getPlacemarkCoordinates().size(); x++) {

            //Calculate the distance from the current location to all the placemarks
            //that have not yet been collected
            Log.d(TAG, "current placemark  " + getPlacemarkCoordinates().get(x)[0] + " " + getPlacemarkCoordinates().get(x)[1]);

            lat1 = current.getLatitude();
            lat2 = getPlacemarkCoordinates().get(x)[0];
            lon1 = current.getLongitude();
            lon2 = getPlacemarkCoordinates().get(x)[1];

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
            if (distance < 25) {
                //if the placemark currently being analyzed is within 25 meters of the user,
                //save the word associated with it into collected words and remove the marker from the map
                //and other storage items where the marker is referenced.
                String[] tempNames = getPlacemarkNames().get(x).split(":");
                int first = Integer.parseInt(tempNames[0]);
                int second = Integer.parseInt(tempNames[1]);
                Log.d(TAG, "first and second " + first + " " + second);
                Log.d(TAG, "found word " + getSongLyrics().get(first - 1)[second - 1]);

                setPlacemarksCollected(getPlacemarksCollected() + 1);
                Log.d(TAG, "number of placemarks " + getPlacemarksCollected());
                getCollectedWords().add(getSongLyrics().get(first - 1)[second - 1]);

                getPlacemarkCoordinates().remove(x);
                getPlacemarkNames().remove(x);
                getPlacemarkStyles().remove(x);
                getCurrentMarkers().get(x).remove();
                getCurrentMarkers().remove(x);
                x--;
            }


        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (isCameraTracking()) {
            //If the user enabled camera tracking, pan to their location
            //every time OnLocationChanged is called
            LatLng latLng = new LatLng(current.getLatitude(), current.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            getmMap().animateCamera(cameraUpdate);
        }


    }


    @Override
    public void onConnectionSuspended(int flag) {
        System.out.println(" >>>> onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //alert the user of the fact that the connection failed and prompt them
        //to start a new game
        System.out.println(" >>>> onConnectionFailed");
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Connection to maps failed!")
                .setMessage("Please start a new game to retry.")
                .setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switchMain();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        switchMain();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void exitCheck() {
        //opens a dialog asking the user whether or not they really want to
        //return to the Main Menu, as their gameplay progress will not be saved.
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Return to Main Menu?")
                .setMessage("Gameplay progress will not be saved! Would you like to return to the Main Menu?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switchMain();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void switchMain() {
        //switches to the MainActivity

        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    public void switchWords() {
        //switches to WordsActivity
        Intent intent = new Intent(this, WordsActivity.class);

        intent.putExtra("collectedWords", getCollectedWords());
        startActivity(intent);

    }

    public void difficultyCheck() {
        //When New Game is pressed asks the user if they really want to return
        //to the difficulty select activity
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Return to Difficulty Select?")
                .setMessage("Gameplay progress will not be saved! Would you like to return to the Difficulty Selection Menu?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switchDiffSelect();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void switchDiffSelect() {
        //switches to difficulty select
        Intent intent = new Intent(this, DifficultySelect.class);

        intent.putExtra("songList", getSongList());

        startActivity(intent);
        finish();
    }

    public void switchGuess() {
        //switches to SongGuess activity
        Intent intent = new Intent(this, GuessSong.class);

        intent.putExtra("selectedSong", getSelectedSong());
        intent.putExtra("distance", getDistanceWalked());
        intent.putExtra("time", getStartTime());
        intent.putExtra("placemarks", getPlacemarksCollected());

        startActivity(intent);
    }

    public void switchHelp() {
        //switches to the HelpMaps activity
        Intent intent = new Intent(this, HelpMaps.class);

        startActivity(intent);
    }

    public void toggleDrawer(View view) {
        //On hamburger icon press, open the side navigation drawer
        getResult().openDrawer();
    }

    public void guessSong(View view) {
        switchGuess();
    }

    //Getters and Setters

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public int getPERMISSIONS_REQUEST_ACCESS_FINE_LOCATION() {
        return PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
    }

    public boolean isCameraTracking() {
        return cameraTracking;
    }

    public void setCameraTracking(boolean cameraTracking) {
        this.cameraTracking = cameraTracking;
    }

    public boolean isFirstPan() {
        return firstPan;
    }

    public void setFirstPan(boolean firstPan) {
        this.firstPan = firstPan;
    }

    public ArrayList<String[]> getSongLyrics() {
        return songLyrics;
    }

    public void setSongLyrics(ArrayList<String[]> songLyrics) {
        this.songLyrics = songLyrics;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public ArrayList<String> getCollectedWords() {
        return collectedWords;
    }

    public void setCollectedWords(ArrayList<String> collectedWords) {
        this.collectedWords = collectedWords;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public void setDistanceWalked(double distanceWalked) {
        this.distanceWalked = distanceWalked;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getPlacemarksCollected() {
        return placemarksCollected;
    }

    public void setPlacemarksCollected(int placemarksCollected) {
        this.placemarksCollected = placemarksCollected;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public ArrayList<double[]> getPlacemarkCoordinates() {
        return placemarkCoordinates;
    }

    public void setPlacemarkCoordinates(ArrayList<double[]> placemarkCoordinates) {
        this.placemarkCoordinates = placemarkCoordinates;
    }

    public ArrayList<String> getPlacemarkNames() {
        return placemarkNames;
    }

    public void setPlacemarkNames(ArrayList<String> placemarkNames) {
        this.placemarkNames = placemarkNames;
    }

    public ArrayList<String> getPlacemarkStyles() {
        return placemarkStyles;
    }

    public void setPlacemarkStyles(ArrayList<String> placemarkStyles) {
        this.placemarkStyles = placemarkStyles;
    }

    public ArrayList<Marker> getCurrentMarkers() {
        return currentMarkers;
    }

    public void setCurrentMarkers(ArrayList<Marker> currentMarkers) {
        this.currentMarkers = currentMarkers;
    }

    public HashMap<String, Bitmap> getMarkerIcons() {
        return markerIcons;
    }

    public void setMarkerIcons(HashMap<String, Bitmap> markerIcons) {
        this.markerIcons = markerIcons;
    }

    public Drawer getResult() {
        return result;
    }

    public void setResult(Drawer result) {
        this.result = result;
    }

    public double getSavedLatitude() {
        return savedLatitude;
    }

    public void setSavedLatitude(double savedLatitude) {
        this.savedLatitude = savedLatitude;
    }

    public double getSavedLongitude() {
        return savedLongitude;
    }

    public void setSavedLongitude(double savedLongitude) {
        this.savedLongitude = savedLongitude;
    }

    public SecondaryDrawerItem getItem1() {
        return item1;
    }

    public void setItem1(SecondaryDrawerItem item1) {
        this.item1 = item1;
    }

    public SecondaryDrawerItem getItem2() {
        return item2;
    }

    public void setItem2(SecondaryDrawerItem item2) {
        this.item2 = item2;
    }

    public SecondaryDrawerItem getItem3() {
        return item3;
    }

    public void setItem3(SecondaryDrawerItem item3) {
        this.item3 = item3;
    }

    public SecondaryDrawerItem getItem4() {
        return item4;
    }

    public void setItem4(SecondaryDrawerItem item4) {
        this.item4 = item4;
    }

    public SecondaryDrawerItem getItem5() {
        return item5;
    }

    public void setItem5(SecondaryDrawerItem item5) {
        this.item5 = item5;
    }

    public SecondaryDrawerItem getItem6() {
        return item6;
    }

    public void setItem6(SecondaryDrawerItem item6) {
        this.item6 = item6;
    }

    public SecondaryDrawerItem getItem7() {
        return item7;
    }

    public void setItem7(SecondaryDrawerItem item7) {
        this.item7 = item7;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
}
