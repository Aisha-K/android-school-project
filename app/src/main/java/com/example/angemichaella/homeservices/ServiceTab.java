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



public class ServiceTab extends Fragment{

    private Button addServiceBtn;
    protected ArrayList<Service> services;
    DatabaseReference databaseServices;
    ListView serviceListView;


    //View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_tab, container, false);


        //initialize attributes
        addServiceBtn = (Button)view.findViewById(R.id.addServiceBtn);
        services = new ArrayList<Service>();
        databaseServices = FirebaseDatabase.getInstance().getReference("Services");
        serviceListView = (ListView)view.findViewById(R.id.serviceListView);


        //updates list of services when data changed
        Query query = databaseServices.orderByChild("serviceName"); //orders list alphabetically based on the servie name
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                services.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service currService = postSnapshot.getValue(Service.class); //retrieving child node
                    //if(!currService.type().equals("indoor")) {
                    services.add(currService);                          //adding service from database to list
                    //}
                }
                ServiceAdapter adtr = new ServiceAdapter(getActivity(), services);
                serviceListView.setAdapter(adtr);

            }
            public void onCancelled(DatabaseError databaseError){
            }
        });


        //when new serivce button is clicked, will call function new Service
        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newServicePopUp();
            }
        });


        //example of when a service is clicked
        serviceListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                        Service clickedSrv = (Service) parent.getItemAtPosition(pos);

                        Bundle args = new Bundle();
                        args.putString("dialog_title", "Edit Service");
                        args.putString("srv_name", clickedSrv.name());
                        args.putString("srv_rate", Double.toString(clickedSrv.rate()));
                        args.putString("srv_id", clickedSrv.id());
                        args.putString("srv_type", clickedSrv.type());

                        EditServiceDialog d = new EditServiceDialog();
                        d.setArguments(args);
                        d.show(getActivity().getSupportFragmentManager(), "edit service dialog");
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

        return view;
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


    //adds a service to the database if service name not already taken
    protected void newService(final String serviceName, final double rate, final boolean isOutdoor){
        //finding if service already exists by finding if it
        Query query = databaseServices.orderByChild("serviceName").equalTo(serviceName);
        //addlistenner allows us to retrieve the data using datasnapshot
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //username exists
                    Toast.makeText( getActivity() , "Service Already Exists", Toast.LENGTH_LONG).show();
                }
                else{ //service does not exist, create service
                    String id = databaseServices.push().getKey();
                    Service newService= new Service(serviceName, isOutdoor, rate , id);
                    databaseServices.child(id).setValue(newService);
                    Toast.makeText( getActivity() , "Service Created", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    //deletes a service from the database
    private void deleteService(String ServiceId){
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

}