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
 * Used to feed rows into our Achievements list view
 */
public class AchievementsListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Achievement> mAchievs;


    public AchievementsListAdapter(Context context, ArrayList<Achievement> achievs) {
        setmContext(context);
        setmAchievs(achievs);
        setmInflater((LayoutInflater) getmContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public int getCount() {
        return getmAchievs().size();
    }

    @Override
    public Object getItem(int position) {
        return getmAchievs().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = getmInflater().inflate(R.layout.list_view_settings, parent, false);

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

        // Get achievement currently being parsed
        Achievement achiev = (Achievement) getItem(position);

        //set all the necessary values to be displayed, according to
        //which achievement is currently being processed
        titleTextView.setText(achiev.getTitle());
        subtitleTextView.setText(achiev.getStatus());
        detailTextView.setText(achiev.getProgress());

        if (achiev.getTitle().startsWith("Collect")) {
            thumbnailImageView.setImageResource(R.drawable.placem);
        } else if (achiev.getTitle().startsWith("Complete a")) {
            thumbnailImageView.setImageResource(R.drawable.clock);
        } else if (achiev.getTitle().startsWith("Complete")) {
            thumbnailImageView.setImageResource(R.drawable.puzzle);
        } else if (achiev.getTitle().startsWith("Walk")) {
            thumbnailImageView.setImageResource(R.drawable.foot);
        }

        return rowView;
    }

    //getters and setters
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

    public ArrayList<Achievement> getmAchievs() {
        return mAchievs;
    }

    public void setmAchievs(ArrayList<Achievement> mAchievs) {
        this.mAchievs = mAchievs;
    }
}