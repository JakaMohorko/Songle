package com.example.s1552344.myapplication;

/**
 * Created by s1552344 on 06/12/17.
 */

public class Achievement {
    private String title;
    private String progress;
    private String status;

    public Achievement (String title, String progress, String status){
        this.title = title;
        this.progress = progress;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
