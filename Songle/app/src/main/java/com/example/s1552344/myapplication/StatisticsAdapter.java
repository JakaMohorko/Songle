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

public class StatisticsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Statistic> mStatistics;

    public StatisticsAdapter(Context context, ArrayList<Statistic> statistics) {
        mContext = context;
        mStatistics = statistics;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mStatistics.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mStatistics.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_view_settings2, parent, false);

        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.list_title);

// Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.list_subtitle);

// Get detail element
        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.list_detail);

// Get thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.list_thumbnail);

        // 1
        Statistic statistic = (Statistic) getItem(position);

// 2
        titleTextView.setText(statistic.getTitle());
        int x = statistic.getProgress();

        if (statistic.getTitle().startsWith("Distance")) {
            thumbnailImageView.setImageResource(R.drawable.foot);
            subtitleTextView.setText(x + " meters");
        } else if (statistic.getTitle().startsWith("Number")) {
            thumbnailImageView.setImageResource(R.drawable.puzzle);
            subtitleTextView.setText(Integer.toString(x));
        } else if (statistic.getTitle().startsWith("Fastest")) {
            thumbnailImageView.setImageResource(R.drawable.clock);
            if(x == 0){
                subtitleTextView.setText("No puzzles solved");
            }else {
                subtitleTextView.setText(x / 60 + " min " + (x - ((x / 60) * 60)) + " s");
            }
        } else if (statistic.getTitle().startsWith("Average")) {
            thumbnailImageView.setImageResource(R.drawable.clock);
            if(x == 0){
                subtitleTextView.setText("No puzzles solved");
            }else {
                subtitleTextView.setText(x / 60 + " min " + (x - ((x / 60) * 60)) + " s");
            }
        } else if (statistic.getTitle().startsWith("Placemarks")) {
            thumbnailImageView.setImageResource(R.drawable.placem);
            subtitleTextView.setText(Integer.toString(x));
        }

        return rowView;
    }
}