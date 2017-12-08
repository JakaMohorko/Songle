package com.example.s1552344.myapplication;

/**
 * Object class used for feeding data into the Gameplay Statistics
 * list view
 */

public class Statistic {
    private String title;
    private int progress;

    public Statistic(String title, int progress) {
        this.title = title;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
