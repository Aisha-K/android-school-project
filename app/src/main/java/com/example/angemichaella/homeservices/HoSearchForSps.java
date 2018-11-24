package com.example.angemichaella.homeservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HoSearchForSps extends AppCompatActivity implements HoFilterBottomSheet.FilterSpListener {

    protected ArrayList<ServiceProvider> filteredProviders = new ArrayList<>();
    protected ArrayList<ServiceProvider> providers = new ArrayList<>();
    protected TextView serviceTitle;

    private String chosenServiceId;
    private String chosenServiceName;

    DatabaseReference users =  FirebaseDatabase.getInstance().getReference("users");

    private Button addFilterBtn;

    ServiceProviderListAdapter adptr;
    ListView providersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_search_for_sps);
        providersListView = (ListView)findViewById(R.id.providersLV);
        serviceTitle = (TextView)findViewById(R.id.nameOfServiceFilter);
        addFilterBtn = (Button) findViewById(R.id.addFilterBtn);


        chosenServiceId  = getIntent().getStringExtra("srv_id");
        chosenServiceName = getIntent().getStringExtra("srv_name");

        serviceTitle.setText(chosenServiceName);

        setUpProvidersList();

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



    public ArrayList<ServiceProvider> filterByService(List<ServiceProvider> sps, String serviceId){
        ArrayList<ServiceProvider> filteredProviders = new ArrayList<>();

        for (ServiceProvider s : sps) {
            if(s.offersService(serviceId)){
                filteredProviders.add(s);
            }
        }

        return filteredProviders;
    }

    public void setUpProvidersList(){
        Query query = users.orderByChild("type").equalTo("ServiceProvider"); //orders list alphabetically based on the service name
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    providers.add(postSnapshot.getValue(ServiceProvider.class));//adding all the service providers to our list.
                }

                filteredProviders = filterByService(providers, chosenServiceId);

                adptr = new ServiceProviderListAdapter(HoSearchForSps.this, filteredProviders, "serviceId"); //can setup the adapter now that the list is built
                providersListView.setAdapter(adptr);
                providersListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                                ServiceProvider clickedSp = (ServiceProvider) parent.getItemAtPosition(pos);
                                Toast.makeText(HoSearchForSps.this,clickedSp.getUsername(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

                addFilterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HoFilterBottomSheet filterDialog = new HoFilterBottomSheet();
                        filterDialog.show(getSupportFragmentManager(), "filterDialog");
                    }
                });

            }

            public void onCancelled(DatabaseError databaseError){
            }
        });
    }


    public void choseFilters(double ratingLowerBound,  List<Availability> avls){
        //Tester Toast:
        String msg = "Filtering by times\n";
        if(avls!= null){
            for(Availability a: avls){
                msg+= a.toString()+"\n";
            }
        }

         msg+= "And by Ratings above "+ ratingLowerBound;

         Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

         //actual functionality
        if(ratingLowerBound != -1){
            //filter Sps by rating specified
        }

        if(avls != null){
            //filter sps by avls specified
        }
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
