package com.example.franc.myapplication;

import java.util.ArrayList;

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

    @Override
    public Entry getEntry(int id) {
        return data.get(id);
    }

    @Override
    public ArrayList<Entry> returnAll() {
        return data;
    }
}
