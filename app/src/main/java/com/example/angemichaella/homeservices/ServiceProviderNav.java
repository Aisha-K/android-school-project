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
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderNav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddAvailabilityDialog.AddAvailabilityListener{
    private DrawerLayout drawer;
    String username;
    String id;
    
    SpProfileFragment fragment1;
    SpMyServicesFragment fragment2;
    SpAvailabilitiesFragment frag3;

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

        //get intents
        username= getIntent().getStringExtra("USER_NAME");
        id= getIntent().getStringExtra("USER_ID");

        if(savedInstanceState==null){   //activity started for the first time
        //initial fragment displayed when first opened
            Bundle args = new Bundle();
            args.putString("username", username);
            args.putString("ID", id);

            SpProfileFragment fragment1=  SpProfileFragment.newInstance(username, id); //creating fragment instance while passing var
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, //puts fragment in fragment container
                    fragment1);
            fragment1.setArguments(args);
            ft.commit();

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


        //opens appropriate fragment based on which item clicked
        switch(menuItem.getItemId()){
            case R.id.nav_profile:
                fragment1=  SpProfileFragment.newInstance(username,id); //creating fragment instance while passing var
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment1).commit();
                break;
            case R.id.nav_services:
                fragment2= SpMyServicesFragment.newInstance(username, id);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment2).commit();
                break;
            case R.id.nav_availabilities:
                frag3= SpAvailabilitiesFragment.newInstance(username, id);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        frag3).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;    //true = item selected
    }

    @Override
    /**
     * heres where you can do what needs to be done by the availabilities that have been added
     */
    public void receiveAvailability(List<Day> days, Time from, Time to){

        ArrayList<Availability> avls = new ArrayList<>(); // holds the availabilities chosen from the dialog in a list

        String avlsString = "Adding Availabilities:";//for testing toast can be removed
        for(int i = 0; i< days.size(); i++){
            avls.add(new Availability(days.get(i), from, to)); //adding each availability
            avlsString += "\n" + avls.get(i); //just for testing toast can be removed
        }
        
        frag3.addAvailabilities(avls);//sends avls over to avl fragent

        Toast.makeText( this ,avlsString, Toast.LENGTH_LONG).show(); //can be removed, just for display for now
    }

}
