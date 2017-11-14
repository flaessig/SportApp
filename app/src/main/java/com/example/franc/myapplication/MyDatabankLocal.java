package com.example.franc.myapplication;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Created by franc on 10/6/2017.
 */

public class MyDatabankLocal implements MyDatabank {
    ArrayList<Entry> data = new ArrayList<>();
    int idCount = 0;

    @Override
    public void addEntry(Entry entry) {
        entry.setID(idCount++);
        data.add(entry);
    }

    //TODO: add a method to return only entries with right criteria

    @Override
    public Entry getEntry(int id) {
        return data.get(id);
    }

    @Override
    public ArrayList<Entry> returnAll() {
        return data;
    }

    @Override
    public ArrayList<Entry> returnSelected(EnumSet<ActivityType> set) {
        ArrayList<Entry> newList = new ArrayList<Entry>();
        int i = 0;
        for (Entry entry : data) {
            if(entry.hasTypeIn(set)) {
                newList.add(entry);
                entry.setID(i);
                i++;
            }
        }
        return newList;
    }
}
