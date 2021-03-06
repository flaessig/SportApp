package com.example.franc.myapplication;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.EnumSet;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, AdapterView.OnItemClickListener {

    private GoogleMap mMap;
    MyDatabank databank;
    SupportMapFragment mapFragment;
    ListView list;
    BlankFragment bfrag = new BlankFragment();
    FilterFragment filters = new FilterFragment();
    Fragment currentFragment;
    NewActivityFragment newAct;
    ArrayList<Entry> currentEntries;
    private HashMap<Marker, Entry> markerMap;
    private boolean listViewDisplayed = false;
    FloatingActionButton fab;
    Button applyFiltersButton;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //instantiate activity with toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Events");

        //buttons
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(newAct);
                getSupportActionBar().setTitle("Create Activity");
            }
        });
        applyFiltersButton = (Button) findViewById(R.id.apply_filters_button);
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                applyFilters();
            }
        });
        applyFiltersButton.setVisibility(View.INVISIBLE);

        //drawerlayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //settings
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myString = prefs.getString("key2",null);


        //map
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);


        //newactivity fragment
        newAct = new NewActivityFragment();


        databank = new MyDatabankLocal();
        databank.addEntry(new Entry("Jogging",47.3904, 8.0457));
        databank.addEntry(new Entry("Cycling", 47.3914, 8.0452));
        databank.addEntry(new Entry("Swimming",47.3903, 8.0487));
        databank.addEntry(new Entry("Leg Bootcamp",47.3900, 8.0450));
        databank.addEntry(new Entry("Leg Day at the Gym",47.3900, 8.0490));
        databank.addEntry(new Entry("Grow a biceps with Tim",47.3900, 8.0480));
        databank.addEntry(new Entry("Vita Parkour Lauf",47.3920, 8.0450));

        //fragments
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.myownframe, bfrag);
        fragmentTransaction.add(R.id.myownframe, mapFragment);
        fragmentTransaction.add(R.id.myownframe, filters);
        fragmentTransaction.add(R.id.myownframe, newAct);
        fragmentTransaction.hide(bfrag);
        fragmentTransaction.hide(filters);
        fragmentTransaction.hide(newAct);
        fragmentTransaction.commit();
        currentFragment = mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentEntries = databank.returnAll();

        //sets id to position in list
        for (int i = 0; i < currentEntries.size(); i++) {
            currentEntries.get(i).setID(i);
        }

        entriesToMap(mMap, currentEntries);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(47.3904, 8.0457)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        setupList();
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
        list.setVisibility(View.INVISIBLE);
        fragmentTransaction.show(frag);
        currentFragment = frag;
        fragmentTransaction.commit();

        if(!frag.equals(mapFragment)) {
            fab.setVisibility(View.INVISIBLE);
        } else {
            getSupportActionBar().setTitle("Find Activities");
            fab.setVisibility(View.VISIBLE);
        }
        if(!frag.equals(filters)) {
            applyFiltersButton.setVisibility(View.INVISIBLE);
        } else {
            applyFiltersButton.setVisibility(View.VISIBLE);
        }
        listViewDisplayed = false;
    }

    // list view initialization
    protected void setupList() {
        View frameLayout = findViewById(R.id.myownframe);

        list = new ListView(this);
        android.widget.ListView.LayoutParams params = new android.widget.ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300 /*ViewGroup.LayoutParams.WRAP_CONTENTViewGroup.LayoutParams.WRAP_CONTENT*/);
        list.setLayoutParams(params);
        ((FrameLayout) frameLayout).addView(list);
        EntryAdapter myAdapter = new EntryAdapter(this, currentEntries);

        list.setAdapter(myAdapter);
        list.setBackgroundColor(Color.WHITE);
        list.setVisibility(View.INVISIBLE);
        list.setOnItemClickListener(this);

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!currentFragment.equals(mapFragment)){
            changeFragment(mapFragment);
        } else {
            super.onBackPressed();
        }
    }

    //adds elements to toolbar (actionbar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    // handle toolbar click (options)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filters) {
            changeFragment(filters);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // handle navigation drawer click
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if  (id == R.id.nav_myevents) {
            changeFragment(bfrag);
            getSupportActionBar().setTitle("My Activities");

        } else if (id == R.id.nav_joinedevents) {
            changeFragment(bfrag);
            getSupportActionBar().setTitle("Joined Activities");

        } else if (id == R.id.nav_search) {
            changeFragment(mapFragment);
            //setupList();
            getSupportActionBar().setTitle("Find Activities");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // handle click on marker
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(!listViewDisplayed) {
            list.setVisibility(View.VISIBLE);
            listViewDisplayed = true;
        }
        Entry currEntry = markerMap.get(marker);
        list.smoothScrollToPosition(currEntry.getID());

        return false;
    }
    // handle touch on map
    @Override
    public void onMapClick (LatLng point) {
        if(listViewDisplayed) {
            list.setVisibility(View.INVISIBLE);
            listViewDisplayed = false;
        }
    }

    // handle click in list
    @Override
    public void onItemClick (AdapterView<?> arg0, View arg1,int position, long arg3) {
        Entry currEntry = currentEntries.get(position);
        Toast.makeText(this, currEntry.getTitle(), Toast.LENGTH_LONG).show();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(currEntry.getLat(), currEntry.getLng())));
    }

    public void applyFilters() {
        boolean swimmingEnabled = prefs.getBoolean("keySwimming", false);
        boolean runningEnabled = prefs.getBoolean("keyRunning", false);
        boolean bootcampEnabled = prefs.getBoolean("keyBootcamp", false);

        EnumSet<ActivityType> activityTypes = EnumSet.noneOf(ActivityType.class);

        if(swimmingEnabled) activityTypes.add(ActivityType.SWIMMING);
        if(runningEnabled) activityTypes.add(ActivityType.RUNNING);
        if(bootcampEnabled) activityTypes.add(ActivityType.BOOTCAMP);

        currentEntries = databank.returnSelected(activityTypes);
        mMap.clear();
        entriesToMap(mMap, currentEntries);

        //TODO: complete method

        Toast.makeText(this, "Filters applied", Toast.LENGTH_LONG).show();
        changeFragment(mapFragment);
    }

}
