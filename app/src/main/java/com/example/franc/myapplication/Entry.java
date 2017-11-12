package com.example.franc.myapplication;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by franc on 10/6/2017.
 */

public class Entry extends Object {
    private String title;
    private Calendar time;
    private String organizer;
    private double lat;
    private double lng;
    private int ID;
    private String description;
    private int numberMax;
    private int numberJoined;
    private String cost;

    public Entry(String title, double lat, double lng) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        description = "This is a description.";
        time = new GregorianCalendar();
        organizer = "organizer";
        numberJoined = 4;
        numberMax = 20;
        cost = "free";
    }

    public void setID(int x) {
        ID = x;
    }

    public int getID() { return ID; };
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getOrganizer() {return organizer;}
    public String getTimeString() {return time.getTime().toString();}
    public double getLat() {
        return lat;
    }
    public double getLng() { return lng; }
    public String getSpots() {
        String spots = "Participants: " + numberJoined + " / " + numberMax;
        return spots;
    }
    public String getCost() {
        String costString = "Cost: " + cost;
        return costString;
    }

    @Override
    public String toString()    {
        return getTitle();
    }
}
