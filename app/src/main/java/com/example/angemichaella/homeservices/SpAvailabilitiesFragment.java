package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpAvailabilitiesFragment extends Fragment{

    FloatingActionButton addAvailabilityBtn;

    String id;
    String username;

    DatabaseReference spNode; //node in database where this service provider is stored
    ServiceProvider sp;// actual object

    private LinearLayout availabilities_list;
    private LinearLayout non_empty_list;
    private LinearLayout empty_list;

    // list of availabilities
    ArrayList<Availability> avList = new ArrayList<Availability>();



    //constructor that allows passing arguments from main activity
    public static SpAvailabilitiesFragment newInstance(String username, String id ) {
        SpAvailabilitiesFragment myFrag = new SpAvailabilitiesFragment();

        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("ID", id);
        myFrag.setArguments(args);

        return myFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            id = getArguments().getString("ID");
            spNode = FirebaseDatabase.getInstance().getReference("users").child( id );
            setAvList();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_sp_availabilities, container, false);
        non_empty_list = (LinearLayout) v.findViewById(R.id.non_empty_list);
        empty_list = (LinearLayout) v.findViewById(R.id.empty_list);

        non_empty_list.setVisibility(View.GONE);
        empty_list.setVisibility(View.VISIBLE);

        addAvailabilityBtn= v.findViewById(R.id.floatingAddButton);

        //availability button on click listener
        addAvailabilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAvailabilityPopUp();
            }
        });


        return v;
    }


    private void setAvList(){
       spNode.addValueEventListener(new ValueEventListener(){
           @Override
           public void onDataChange(DataSnapshot dataSnapshot){

               sp = dataSnapshot.getValue(ServiceProvider.class);

               // check if list of availabilities is empty or no and display appropriate layout

               if(sp.hasAvailabilities()){
                   setNonEmptyList();
               }
               else {
                   setEmptyList();
               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError){
               System.out.println("The read failed: " + databaseError.getCode());
           }
       });
    }

    private void setNonEmptyList(){

        View v = getView();

        avList = sp.getAvailabilities();

    }

    private void setEmptyList(){
        View v = getView();

    }


    private void newAvailabilityPopUp(){
        AddAvailabilityDialog d = new AddAvailabilityDialog();
        d.show(getActivity().getSupportFragmentManager(), "hi");
    }

    // add availability to the database if availability doesn't already exists
    protected void newAvailability(final Day d, final Time f, final Time t){

        // temp availability instance to be probably added to the database
        Availability temp = new Availability(d,f,t);


        if(!avList.contains(temp)){ // if the availability doesn't already exist in the database

            Toast.makeText( getActivity() , "Availability added", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(), "Availability already exists",Toast.LENGTH_LONG).show();
        }
    }

    /*
    //updates a service in the database with new info
    protected void editService( final String ServiceId, String oldname, final String newName, final double newPrice, final boolean isOutdoor){
        //if the name was not changed, we do not need to search wether the new name is already in use
        if(oldname.equals(newName)){
            DatabaseReference dR = databaseServices.child(ServiceId);
            dR.removeValue();
            Service service = new Service(newName, isOutdoor, newPrice, ServiceId);
            dR.setValue(service);
        }
        else {
            //finding if service name already exists
            Query query = databaseServices.orderByChild("serviceName").equalTo(newName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //username exists
                        Toast.makeText(getActivity(), "Service already exists, try again with a new name", Toast.LENGTH_LONG).show();
                    } else { //service does not exist, create service
                        DatabaseReference dR = databaseServices.child(ServiceId);
                        dR.removeValue();
                        Service service = new Service(newName, isOutdoor, newPrice, ServiceId);
                        dR.setValue(service);

                        Toast.makeText(getActivity(), "Service Updated", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        }

    }

    */

}
