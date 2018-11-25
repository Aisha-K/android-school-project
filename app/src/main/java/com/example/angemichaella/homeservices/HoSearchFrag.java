package com.example.angemichaella.homeservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HoSearchFrag extends Fragment {
    protected ArrayList<Service> services;
    DatabaseReference databaseServices;
    ListView serviceListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ho_search, container, false);

        //initialize var
        services = new ArrayList<Service>();
        databaseServices = FirebaseDatabase.getInstance().getReference("Services");
        serviceListView = (ListView)view.findViewById(R.id.hoServiceSearchLV);


        //
        serviceListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                        Service clickedSrv = (Service) parent.getItemAtPosition(pos);

                        goToSpSearch(clickedSrv);
                        /*Bundle args = new Bundle();
                        args.putString("srv_id", clickedSrv.id());*/
                    }
                }
        );

        return view;
    }


    @Override
    public void onStart(){
        super.onStart();

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

    }


    public void goToSpSearch(Service chosenService){
        Intent intent  = (new Intent(getActivity(), HoSearchForSps.class));
        intent.putExtra( "srv_id", chosenService.id());
        intent.putExtra( "srv_name", chosenService.name());
        intent.putExtra( "ho_name", ((HomeOwnerNav)getActivity()).username);
        startActivity(intent);

    }

}