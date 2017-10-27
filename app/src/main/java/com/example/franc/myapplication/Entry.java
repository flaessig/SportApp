package com.example.franc.myapplication;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by franc on 10/6/2017.
 */

public class Entry extends Object {
    private String title;
    private double lat;
    private double lng;
    private int ID;

    public Entry(String title, double lat, double lng) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
    }

    public void setID(int x) {
        ID = x;
    }

    public int getID() { return ID; };

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() { return lng; }
}
