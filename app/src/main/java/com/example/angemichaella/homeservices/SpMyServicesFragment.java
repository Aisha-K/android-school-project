package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SpMyServicesFragment extends Fragment {
    String id;
    String username;
    DatabaseReference spNode;   //node in database where this service provider is stored

    //constructor that allows passing arguments from main activity
    public static SpMyServicesFragment newInstance(String username, String id ) {
        SpMyServicesFragment myFrag = new SpMyServicesFragment();

        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("ID", id);
        myFrag.setArguments(args);

        return myFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        username = getArguments().getString("username");
        id=getArguments().getString("ID");
        spNode=FirebaseDatabase.getInstance().getReference("Users").child( id );

        //inflates fragment layout
        return inflater.inflate(R.layout.fragment_sp_myservices, container, false);


    }
}
