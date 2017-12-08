package com.example.s1552344.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * list adapter implemented by guidelines found at:
 * https://www.raywenderlich.com/124438/android-listview-tutorial
 * Custom list view adapter for the SongList activity
 */

public class SongListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mSongs;


    public SongListAdapter(Context context, ArrayList<String> songs) {
        setmContext(context);
        setmSongs(songs);
        setmInflater((LayoutInflater) getmContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public int getCount() {
        return getmSongs().size();
    }


    @Override
    public Object getItem(int position) {
        return getmSongs().get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = getmInflater().inflate(R.layout.list_view_settings1, parent, false);

        //set TextView title
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.list_title);

        //set detail element
        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.list_detail);

        //set thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.list_thumbnail);

        //get song data from the song array list
        String song = (String) getItem(position);

        //set the title to the song data
        titleTextView.setText(song);

        //set the detail to the row number
        detailTextView.setText(Integer.toString(position + 1));

        //set the image to the puzzle icon
        thumbnailImageView.setImageResource(R.drawable.puzzle);


        return rowView;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public ArrayList<String> getmSongs() {
        return mSongs;
    }

    public void setmSongs(ArrayList<String> mSongs) {
        this.mSongs = mSongs;
    }
}
