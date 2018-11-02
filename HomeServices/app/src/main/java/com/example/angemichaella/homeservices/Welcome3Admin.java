package com.example.angemichaella.homeservices;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Welcome3Admin extends AppCompatActivity {

    private ViewPagerAdapter tabPageAdptr;
    private ViewPager viewPgr;

    List<User> users;
    List<Service> services;

    DatabaseReference databaseUsers;
    DatabaseReference databaseServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome3_admin);

        //initializing databases
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseServices = FirebaseDatabase.getInstance().getReference("services");


        //initializing lists
        services = new ArrayList<>();
        users = new ArrayList<>();

        //tab layout stuff
        viewPgr = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPgr);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPgr);
    }

    private void setupViewPager(ViewPager vp){
        tabPageAdptr = new ViewPagerAdapter(getSupportFragmentManager());

        tabPageAdptr.addFragment(new UserTab(), "users");
        tabPageAdptr.addFragment(new ServiceTab(), "services");
        vp.setAdapter(tabPageAdptr);
    }

}
