package com.example.s1552344.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * Help display activity for MapsActivity
 */
public class HelpMaps extends AppCompatActivity {

    private String help = "   - Move within 25 meters of a marker to \"collect\" it.\n\n" +
            "    - You can view the words you've collected through the side menu button View Words.\n\n" +
            "    - Press Guess Song to enter the song guessing menu. There you have to enter the title of the Song of which the" +
            "lyrics are scattered around the map.\n\n" +
            "    - If you are unable to complete a song, press the New Song button to return to the difficulty select menu.\n\n" +
            "    - All of the markers are located around George Square, Edinburgh.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_help);


        //display help info stored in help
        TextView mytxt = (TextView) findViewById(R.id.textView5);
        mytxt.setText(getHelp());
        mytxt.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    //back button press function
    public void back(View view) {
        finish();
    }

    //getters and setters
    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }
}
