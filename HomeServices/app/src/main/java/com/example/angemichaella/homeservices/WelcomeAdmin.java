package com.example.angemichaella.homeservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WelcomeAdmin extends AppCompatActivity {

    ListView users_list;

    List<User> users;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        users_list = (ListView) findViewById(R.id.users_list);

        users = new ArrayList<>(0);

        String username= getIntent().getStringExtra("USER_NAME");


        TextView textViewWelcomeAdmin = (TextView) findViewById(R.id.textViewWelcomeAdmin);
        textViewWelcomeAdmin.setText("Welcome, " + username);

    }

    @Override
    protected void onStart(){
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                users.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User product = postSnapshot.getValue(User.class);
                    users.add(product);
                }
                UserList userAdapter = new UserList(WelcomeAdmin.this, users);
                users_list.setAdapter(userAdapter);
            }
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

}
