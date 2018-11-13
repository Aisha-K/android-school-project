package com.example.angemichaella.homeservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;

public class WelcomeServiceProvider extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_service_provider);

        String username= getIntent().getStringExtra("USER_NAME");


        TextView textViewWelcomeServiceProvider = (TextView) findViewById(R.id.textViewWelcomeServiceProvider);
        textViewWelcomeServiceProvider.setText("Welcome, " + username);
    }


}
