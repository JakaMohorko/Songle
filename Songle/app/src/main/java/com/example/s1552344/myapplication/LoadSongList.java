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

import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class LoadSongList extends AppCompatActivity {

    String songXML = "";
    private final String TAG = "LoadSongList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_song_list);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.txt", new
                TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        songXML = responseString;
                        switchActivity();
                    }
                }

        );
    }
    public void switchActivity (){
        Intent intent = new Intent(this, MainActivity.class);


        intent.putExtra("songXML", songXML);

        Log.d(TAG, "Song XML loaded");
        startActivity(intent);
    }

}
