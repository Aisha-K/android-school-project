package com.example.angemichaella.homeservices;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ServiceProviderNav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_nav);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        //item clicked in navigation drawer, e.g. My services, Profile, or Availabilities
        NavigationView navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle( this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){   //activity started for the first time
        //initial fragment displayed when first opened
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, //puts fragment infragment container
                new SpProfileFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_profile);
        }

    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){   //when back pressed if navigation drawer open, will close drawer only
                drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //opens appropriate fragment base don which item clicked
        switch(menuItem.getItemId()){
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, //puts fragment infragment container
                        new SpProfileFragment()).commit();
                break;
            case R.id.nav_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SpMyServicesFragment()).commit();
                break;
            case R.id.nav_availabilities:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SpAvailabilitiesFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;    //true = item selected
    }
}
