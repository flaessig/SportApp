package com.example.franc.myapplication;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Created by franc on 10/6/2017.
 */

public interface MyDatabank {
    void addEntry(Entry entry);
    Entry getEntry(int id);
    ArrayList<Entry> returnAll();
    ArrayList<Entry> returnSelected(EnumSet<ActivityType> set);
}
