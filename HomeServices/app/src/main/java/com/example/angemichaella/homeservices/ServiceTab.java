package com.example.angemichaella.homeservices;

import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *  factory method to
 * create an instance of this fragment.
 */
public class ServiceTab extends Fragment {

    private Button addServiceBtn;
    private ArrayList<Service> services;
    DatabaseReference databaseServices;
    ListView serviceListView;
    //View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_tab, container, false);
        addServiceBtn = (Button)view.findViewById(R.id.addServiceBtn);
        services = new ArrayList<Service>();
        databaseServices = FirebaseDatabase.getInstance().getReference("Services");
        serviceListView = (ListView)view.findViewById(R.id.serviceListView);


        databaseServices.addValueEventListener(new ValueEventListener(){
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
                newService();
            }
        });


        //example of when a service is clicked
        serviceListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                        String srvName = String.valueOf(((Service)parent.getItemAtPosition(pos)).name());
                        String srvId= String.valueOf(((Service)parent.getItemAtPosition(pos)).serviceId);
                        Toast.makeText(getActivity(), srvName, Toast.LENGTH_LONG).show();
                    }
                }
        );

        return view;
    }

//    @Override
//    public void onStart(){
//        super.onStart();
//
//    }

    //adds a service to the database if name not already taken
    private void newService(){

        //test case
        Service s3= new Service("Plumbing", false, 30.0, "d4");
        databaseServices.child(s3.serviceId).setValue(s3);
    }

    //shows pop up which allows user to either edit or delete the service that was clicked on
    //this method calls the deleteService() if delete button clicked
    // or  editService() if update button clicked with appropriate text entered
    private void showEditOrDeletePopUp( String serviceName, String serviceId){

    }

    //deletes a service from the database
    private void deleteService( String ServiceId){

    }

    //updates a service in the database with new info
    private void editService(String ServiceId, String newName, double newPrice){

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}