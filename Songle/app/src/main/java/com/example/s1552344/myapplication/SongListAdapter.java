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
 * Created by s1552344 on 06/12/17.
 */

public class SongListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mSongs;


    public SongListAdapter(Context context, ArrayList<String> songs) {
        mContext = context;
        mSongs = songs;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //1
    @Override
    public int getCount() {
        return mSongs.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mSongs.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_view_settings1, parent, false);

        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.list_title);


        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.list_detail);

        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.list_thumbnail);

        String song = (String) getItem(position);

        titleTextView.setText(song);
        detailTextView.setText(Integer.toString(position+1));


        thumbnailImageView.setImageResource(R.drawable.puzzle);


        return rowView;
    }
}
