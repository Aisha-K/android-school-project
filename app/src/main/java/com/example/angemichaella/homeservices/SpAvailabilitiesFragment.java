package com.example.angemichaella.homeservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class SpAvailabilitiesFragment extends Fragment{

    String id;
    String username;


    private Button addAvBtn;
    protected ArrayList<Availability> availabilities = new ArrayList<Availability>();

    DatabaseReference spNode;
    ListView avListView;

    ServiceProvider sp;

    //constructor that allows passing arguments from main activity
    public static SpAvailabilitiesFragment newInstance(String username, String id ) {
        SpAvailabilitiesFragment myFrag = new SpAvailabilitiesFragment();

        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("ID", id);
        myFrag.setArguments(args);

        return myFrag;
    }

    public void updateSp(){
        spNode.removeValue();
        spNode.setValue(sp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            id = getArguments().getString("ID");
            spNode = FirebaseDatabase.getInstance().getReference("users").child( id );
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_availabilities, container, false);


        //initialize attributes
        addAvBtn = (Button)view.findViewById(R.id.addAvBtn);
        spNode = FirebaseDatabase.getInstance().getReference("users").child(id);


        avListView = (ListView)view.findViewById(R.id.avListView);


        //updates list of availabilities when data changed
        Query query = spNode;
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                availabilities.clear();

                sp = dataSnapshot.getValue(ServiceProvider.class);

                // get the new list of availabilities
                if(sp.hasAvailabilities()==true){
                    availabilities = sp.getAvailabilities();
                }

                sp.addAvailability(new Availability(Day.MONDAY, new Time(2,5,1), new Time(3,6,1)));
                sp.addAvailability(new Availability(Day.MONDAY, new Time(3,5,1), new Time(4,5,1)));
                sp.addAvailability(new Availability(Day.MONDAY, new Time(3,6,0), new Time(3,8,0)));

                updateSp();

                AvAdapter adtr = new AvAdapter(getActivity(), availabilities);
                try{
                    if(!availabilities.isEmpty()){
                        avListView.setAdapter(adtr);
                    }
                } catch (Exception e){

                }

            }
            public void onCancelled(DatabaseError databaseError){
            }
        });


        //when new availability button is clicked, will call function new Availability
        addAvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAvPopUp();
            }
        });

        /*
        //example of when availability is clicked
        avListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                        Availability clickedAv = (Availability) parent.getItemAtPosition(pos);

                        Bundle args = new Bundle();
                        args.putString("dialog_title", "Edit Availability");
                        args.putString("srv_name", clickedSrv.name());
                        args.putString("srv_rate", Double.toString(clickedSrv.rate()));
                        args.putString("srv_id", clickedSrv.id());
                        args.putString("srv_type", clickedSrv.type());

                        EditServiceDialog d = new EditServiceDialog();
                        d.setArguments(args);
                        d.show(getActivity().getSupportFragmentManager(), "edit availability dialog");
                    }
                }
        );

        serviceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Service");
                final Service clickedServ = (Service)serviceListView.getItemAtPosition(index);
                builder.setMessage("Are you sure you want to delete the " + '"'+ clickedServ.name()+ '"' +" service?");


                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                        deleteService(clickedServ.id());
                        dialog.dismiss();//user clicked create

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        */

        return view;
    }

    private void newAvPopUp(){

        AddAvailabilityDialog d = new AddAvailabilityDialog();
        d.show(getActivity().getSupportFragmentManager(), "new availability dialog");

    }


    //adds an availability to the database if availability doesn't already exist
    protected void newAv(final Day day, final Time from, final Time to){

        final Availability newAv = new Availability(day, from, to);

        //finding if service already exists by finding if it
        Query query = spNode;
        //addlistenner allows us to retrieve the data using datasnapshot
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sp = dataSnapshot.getValue(ServiceProvider.class);

                availabilities = sp.getAvailabilities();

                if (!availabilities.contains(newAv)) { // exact same availability doesn't already exist
                    sp.addAvailability(newAv);
                    updateSp();

                    Toast.makeText( getActivity() , "Availability Added", Toast.LENGTH_LONG).show();
                } else {
                    //availability
                    Toast.makeText( getActivity() , "Availability Already Exists", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    
    
        
    public void addAvailabilities(List<Availability> avls){
        //do what needs to be done with these
    }

    /*
    //deletes an availability from the database
    private void deleteAv(String ServiceId){
        DatabaseReference dR= databaseServices.child(ServiceId);
        dR.removeValue();

        Toast.makeText(getActivity(), "Service Deleted", Toast.LENGTH_LONG).show();
    }

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
