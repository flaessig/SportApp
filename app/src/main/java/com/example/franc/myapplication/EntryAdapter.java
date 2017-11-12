package com.example.franc.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by franc on 11/10/2017.
 */

public class EntryAdapter extends ArrayAdapter <Entry> {
    public EntryAdapter(Context context, ArrayList<Entry> entries) {
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Entry entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_entry, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvSpots = (TextView) convertView.findViewById(R.id.tvSpots);
        TextView tvCost = (TextView) convertView.findViewById(R.id.tvCost);
        // Populate the data into the template view using the data object
        tvTitle.setText(entry.getTitle());
        tvTime.setText(entry.getTimeString());
        tvSpots.setText(entry.getSpots());
        tvCost.setText(entry.getCost());
        // Return the completed view to render on screen
        return convertView;
    }

}
