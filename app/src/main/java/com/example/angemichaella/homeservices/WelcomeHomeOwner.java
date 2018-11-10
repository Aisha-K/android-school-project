package com.example.angemichaella.homeservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeHomeOwner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_home_owner);

        String username= getIntent().getStringExtra("USER_NAME");


        TextView textViewWelcomeHomeOwner = (TextView) findViewById(R.id.textViewWelcomeHomeOwner);
        textViewWelcomeHomeOwner.setText("Welcome, " + username);
    }
}
