package com.example.angemichaella.homeservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseUsers;    //reference to the database
    EditText editTextName;
    EditText editTextPassword;
    Button buttonCreateHomeOwner;
    Button buttonCreateServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing variables
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
        editTextName = (EditText) findViewById(R.id.txt_un);
        editTextPassword = (EditText) findViewById(R.id.txt_pw);
    }


}
