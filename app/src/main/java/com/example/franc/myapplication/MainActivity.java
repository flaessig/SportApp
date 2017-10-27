package com.example.franc.myapplication;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    MyDatabank databank;
    SupportMapFragment mapFragment;
    BlankFragment bfrag = new BlankFragment();
    Fragment currentFragment;
    ArrayList<Entry> places;
    private HashMap<Marker, Entry> markerMap;
    private boolean viewDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //instantiate activity with toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Events");

        //actionbutton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //drawerlayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //map
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);





        databank = new MyDatabankLocal();
        databank.addEntry(new Entry("my hood",47.904, 8.0457));
        databank.addEntry(new Entry("place1", 47.3914, 8.0452));
        databank.addEntry(new Entry("place2",47.3903, 8.0487));
        databank.addEntry(new Entry("place3",47.3900, 8.0450));

        //fragments
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.myownframe, bfrag);
        fragmentTransaction.add(R.id.myownframe, mapFragment);
        fragmentTransaction.hide(bfrag);
        fragmentTransaction.commit();
        currentFragment = mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        places = databank.returnAll();
        entriesToMap(mMap, places);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(47.3904, 8.0457)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMarkerClickListener(this);
    }

    protected void entriesToMap(GoogleMap mMap, ArrayList<Entry> entries) {
        markerMap = new HashMap<>();
        for(Entry entry : entries) {
            entryToMap(mMap, entry);
        }
    }

    protected void entryToMap(GoogleMap mMap, Entry entry) {

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(entry.getLat(), entry.getLng()))
                .title(entry.getTitle())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        markerMap.put(marker, entry);
    }

    protected void changeFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(currentFragment);
        fragmentTransaction.show(frag);
        currentFragment = frag;
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filters) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if  (id == R.id.nav_myevents) {
            changeFragment(bfrag);
            getSupportActionBar().setTitle("My Events");

        } else if (id == R.id.nav_joinedevents) {
            changeFragment(bfrag);
            getSupportActionBar().setTitle("Joined Events");

        } else if (id == R.id.nav_search) {
            changeFragment(mapFragment);
            getSupportActionBar().setTitle("Find Events");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(!viewDisplayed) {
            View frameLayout = findViewById(R.id.myownframe);
            //LinearLayout layout = (LinearLayout) findViewById(R.id.info);

            ListView valueTV = new ListView(this);
            valueTV.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300 /*ViewGroup.LayoutParams.WRAP_CONTENTViewGroup.LayoutParams.WRAP_CONTENT*/));
            ((FrameLayout) frameLayout).addView(valueTV);
            String[] values = {"hello", "stranger", "boss", "more", "words", "needed", "please", "booss", "give", "workds"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);
            valueTV.setAdapter(adapter);
            valueTV.setBackgroundColor(Color.WHITE);
            //view.setVisibility(view.INVISIBLE);
            viewDisplayed = true;
        }
        return false;
    }
}
