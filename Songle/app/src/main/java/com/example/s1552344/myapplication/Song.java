package com.example.s1552344.myapplication;

import java.io.Serializable;

/**
 * Class for song data.
 * objects of type Song get a title, artist, number and link assigned to them.
 */

public class Song implements Serializable {

    private String Title;
    private String Artist;
    private String Number;
    private String Link;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
