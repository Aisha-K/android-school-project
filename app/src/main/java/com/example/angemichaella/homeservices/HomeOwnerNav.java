package com.example.angemichaella.homeservices;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Main navigation activity page that a home owner is sent to after signing in
 */
public class HomeOwnerNav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RatingDialog.RatingDialogListener {
    private DrawerLayout drawer;
    private TextView nameTV;
    private ImageView iconIV;
    String username;
    String id;
    List<Service> services;
    DatabaseReference bookingsDb;
    HoBookingsFragment bookingFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        //item clicked in navigation drawer, e.g. My services, Profile, or Availabilities
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //get intents and initialize variables
        username = getIntent().getStringExtra("USER_NAME");
        id = getIntent().getStringExtra("USER_ID");
        services = new ArrayList<>();

        //setting texts in navigation drawer header
        View hView = navigationView.getHeaderView(0);
        nameTV = hView.findViewById(R.id.navSpNameTV);
        nameTV.setText(username);
        nameTV = hView.findViewById(R.id.navAccType);
        String navText="Home Owner";
        nameTV.setText(navText);

        //setting image in header
        iconIV=hView.findViewById(R.id.navIcon);
        iconIV.setImageResource(R.drawable.houserepair);

        //setting menu item texts and icons in navigation drawer to reuse the already made layour of sp
        Menu menu= navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_bookings);
        item.setTitle("Search for Services");
        item.setIcon(R.drawable.ic_search);
        item = menu.findItem(R.id.nav_profile);
        item.setTitle("My Bookings");
        item.setIcon(R.drawable.ic_clock_time);
        item = menu.findItem(R.id.nav_services);
        item.setTitle("Settings");
        item = menu.findItem(R.id.nav_availabilities);
        item.setVisible(false);


        if (savedInstanceState == null) {   //activity started for the first time
            //initial fragment displayed when first opened
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, //puts fragment in fragment container
                    new HoBookingsFragment());
            ft.commit();
        }

        bookingsDb = FirebaseDatabase.getInstance().getReference("bookings");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {   //when back pressed if navigation drawer open, will close drawer only
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {




        //opens appropriate fragment based on which item clicked
        switch (menuItem.getItemId()) {
            case R.id.nav_bookings: //search for services fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HoSearchFrag()).commit();
                break;
            case R.id.nav_profile:  //my bookings fragment
                bookingFrag = new HoBookingsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        bookingFrag).commit();
                break;
            case R.id.nav_services: //settings fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HoSignOutFrag()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;    //true = item selected
    }

    public void receiveRatingUpdate(String bookingId, final double rating, final String comment){
        bookingFrag = new HoBookingsFragment();
        bookingFrag.update(bookingId, rating, comment);

    }



}
