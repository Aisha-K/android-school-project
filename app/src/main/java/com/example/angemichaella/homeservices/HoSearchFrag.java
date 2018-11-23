package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HoSearchFrag extends Fragment {

    protected ArrayList<ServiceProvider> filteredProviders = new ArrayList<>();
    protected ArrayList<ServiceProvider> providers = new ArrayList<>();

    DatabaseReference users =  FirebaseDatabase.getInstance().getReference("users");

    ServiceProviderListAdapter adptr;
    ListView providersListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpProvidersList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ho_search, container, false);
        providersListView = (ListView)view.findViewById(R.id.providersLV);
        return view;
    }



    public void setUpProvidersList(){
        Query query = users.orderByChild("type").equalTo("ServiceProvider"); //orders list alphabetically based on the service name
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    providers.add(postSnapshot.getValue(ServiceProvider.class));//adding all the service providers to our list.
                }


                adptr = new ServiceProviderListAdapter(getActivity(), providers, "serviceId"); //can setup the adapter now that the list is built
                providersListView.setAdapter(adptr);
                                providersListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                                ServiceProvider clickedSp = (ServiceProvider) parent.getItemAtPosition(pos);

                                Toast.makeText(getActivity(),clickedSp.getUsername(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

            }

            public void onCancelled(DatabaseError databaseError){
            }
        });
    }


    public ArrayList<ServiceProvider> filterByAvailability(ArrayList<ServiceProvider> sproviders, ArrayList<Availability> avls) {
        ArrayList<ServiceProvider> filteredProviders = new ArrayList<>();

        for (ServiceProvider s : sproviders) {
            if(s.isAvailableSometimeDuring(avls)){
                filteredProviders.add(s);
            }
        }

        return filteredProviders;
    }




    @Override
    //updates list of service providers on data change
    public void onStart(){

        super.onStart();
        Query query = users.orderByChild("type").equalTo("ServiceProvider"); //orders list alphabetically based on the service name
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                providers.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    providers.add(postSnapshot.getValue(ServiceProvider.class));//adding all the service providers to our list.
                }

            }

            public void onCancelled(DatabaseError databaseError){
            }
        });

    }
}
