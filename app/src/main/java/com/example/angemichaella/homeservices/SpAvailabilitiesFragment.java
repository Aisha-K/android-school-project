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
    AvAdapter avAd;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            id = getArguments().getString("ID");
            spNode = FirebaseDatabase.getInstance().getReference("users").child( id );
            setUpSPFromDatabase();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_availabilities, container, false);


        //initialize attributes
        avListView = (ListView)view.findViewById(R.id.avListView);

        //when new availability button is clicked, will call function new Availability
        addAvBtn = (Button)view.findViewById(R.id.addAvBtn);
        addAvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAvPopUp();
            }
        });



        //when availability is clicked
        avListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                        Availability clickedAv = (Availability) parent.getItemAtPosition(pos);

                        // put the values from the clickedAv in the dialog

                        /*
                        Bundle args = new Bundle();
                        args.putString("dialog_title", "Edit Availability");
                        args.putString("srv_name", clickedSrv.name());
                        args.putString("srv_rate", Double.toString(clickedSrv.rate()));
                        args.putString("srv_id", clickedSrv.id());
                        args.putString("srv_type", clickedSrv.type());

                        AddAvailabilityDialog d = new AddAvailabilityDialog();
                        d.setArguments(args);
                        d.show(getActivity().getSupportFragmentManager(), "edit availability dialog");

                        */
                    }
                }
        );

        avListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Service");
                final Availability clickedAv= (Availability) avListView.getItemAtPosition(index);
                builder.setMessage("Are you sure you want to delete this service?");


                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                        deleteAv(clickedAv);
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

        return view;
    }

    private void newAvPopUp(){

        AddAvailabilityDialog d = new AddAvailabilityDialog();
        d.show(getActivity().getSupportFragmentManager(), "new availability dialog");

    }

    public void addAvailabilities(ArrayList<Availability> avList){

        for(Availability a: avList){

            int overlapsWithIx = sp.overlapsWith(a);
            if(sp.avAlreadyExists(a)){
                Toast.makeText(getActivity(), "Availability " + a + " already exists" , Toast.LENGTH_LONG).show();
            } else if(overlapsWithIx != -1){//replaces overlapping with merged availability
                sp.getAvailabilities().set( overlapsWithIx, a.mergeWith(sp.getAvailabilities().get(overlapsWithIx)) );

                Toast.makeText(getActivity(), "Extending existing  availability... " , Toast.LENGTH_LONG).show();
            }else{
                sp.addAvailability(a);
            }
        }

        spNode.setValue(sp);
    }


    //deletes an availability from the database
    private void deleteAv(Availability delAv){

        availabilities = sp.getAvailabilities();

        for(Availability av : availabilities){
            if(av.toString().equals(delAv.toString())){
                sp.removeAvailabitiy(delAv);
            }
        }

        spNode.setValue(sp);

        Toast.makeText(getActivity(), "Availability Deleted", Toast.LENGTH_LONG).show();
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

    public void setUpSPFromDatabase()
    {
        //updates list of availabilities when data changed

        spNode.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                sp = dataSnapshot.getValue(ServiceProvider.class);

                if(sp.hasAvailabilities()){
                    availabilities = sp.getAvailabilities();
                }

                if(getActivity()!= null) {
                    avAd = new AvAdapter(getActivity(), availabilities);
                    avListView.setAdapter(avAd);
                }
            }
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }


    /*
     * For real time updating the avl list
     */
    @Override
    public void onStart(){
        super.onStart();
        spNode.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(getActivity()!= null) {
                    avAd = new AvAdapter(getActivity(), availabilities);
                    avListView.setAdapter(avAd);
                }
            }
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

}
