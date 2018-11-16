package com.example.angemichaella.homeservices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SpMyServicesFragment extends Fragment {
    String id;
    String username;
    DatabaseReference spNode;   //node in database where this service provider is stored

    private Button addMyServiceBtn;
    protected ArrayList<Service> myServices;
    DatabaseReference databaseServices;
    ListView myServiceListView;

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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_myservices, null);

        //initialize attributes
        addMyServiceBtn = (Button)view.findViewById(R.id.addServiceBtn);
        myServices = new ArrayList<Service>();

        //Reference to Service Providers services?
        databaseServices = FirebaseDatabase.getInstance().getReference("My Services");
        myServiceListView = (ListView)view.findViewById(R.id.serviceListView);


        //updates list of services when data changed
        Query query = databaseServices.orderByChild("serviceName"); //orders list alphabetically based on the service name
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                myServices.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service currService = postSnapshot.getValue(Service.class); //retrieving child node

                    myServices.add(currService);                          //adding service from database to list
                }
                ServiceAdapter adtr = new ServiceAdapter(getActivity(), myServices);
                myServiceListView.setAdapter(adtr);

            }
            public void onCancelled(DatabaseError databaseError){
            }
        });



        addMyServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddServiceDialog d = new AddServiceDialog();
                d.show(getActivity().getSupportFragmentManager(), "add service dialog");
            }
        });

        //make new service popup


        myServiceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Service");
                final Service clickedServ = (Service)myServiceListView.getItemAtPosition(index);
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
        //inflates fragment layout
        return inflater.inflate(R.layout.fragment_sp_myservices, container, false);
    }

    private void newServicePopUp(){

        Bundle args = new Bundle();
        args.putString("dialog_title", "Add Service");
        args.putString("srv_name", "");
        args.putString("srv_rate", "");
        args.putString("srv_id", "creatingNewUser");
        args.putString("srv_type", "indoor");

        EditServiceDialog d = new EditServiceDialog();
        d.setArguments(args);
        d.show(getActivity().getSupportFragmentManager(), "new service dialog");

    }


    //deletes a service from the database
    private void deleteService(String ServiceId){
        DatabaseReference dR= databaseServices.child(ServiceId);
        dR.removeValue();

        Toast.makeText(getActivity(), "Service Deleted", Toast.LENGTH_LONG).show();

    }

}


