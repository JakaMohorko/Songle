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
 * Used to feed rows into our GameplayStatistics list view
 */

public class StatisticsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Statistic> mStatistics;

    public StatisticsAdapter(Context context, ArrayList<Statistic> statistics) {
        setmContext(context);
        setmStatistics(statistics);
        setmInflater((LayoutInflater) getmContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public int getCount() {
        return getmStatistics().size();
    }

    @Override
    public Object getItem(int position) {
        return getmStatistics().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = getmInflater().inflate(R.layout.list_view_settings2, parent, false);

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

        // get current statistics element
        Statistic statistic = (Statistic) getItem(position);

        // set necessary row data according to what Statistic we're
        // currently handling
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

            if (x == 0) {
                subtitleTextView.setText("No puzzles solved");
            } else {
                subtitleTextView.setText(x / 60 + " min " + (x - ((x / 60) * 60)) + " s");
            }

        } else if (statistic.getTitle().startsWith("Average")) {
            thumbnailImageView.setImageResource(R.drawable.clock);
            if (x == 0) {
                subtitleTextView.setText("No puzzles solved");
            } else {
                subtitleTextView.setText(x / 60 + " min " + (x - ((x / 60) * 60)) + " s");
            }

        } else if (statistic.getTitle().startsWith("Placemarks")) {
            thumbnailImageView.setImageResource(R.drawable.placem);
            subtitleTextView.setText(Integer.toString(x));
        }

        return rowView;
    }

    //Getters and Setters
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

    public ArrayList<Statistic> getmStatistics() {
        return mStatistics;
    }

    public void setmStatistics(ArrayList<Statistic> mStatistics) {
        this.mStatistics = mStatistics;
    }
}