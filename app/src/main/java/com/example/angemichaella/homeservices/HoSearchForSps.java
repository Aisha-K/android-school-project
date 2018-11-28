package com.example.angemichaella.homeservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private String homeOwnerName;

    DatabaseReference users =  FirebaseDatabase.getInstance().getReference("users");

    private Button addFilterBtn;
    private LinearLayout emptyLayout;
    private LinearLayout btnLayout;

    ServiceProviderListAdapter adptr;
    ListView providersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up view components
        setContentView(R.layout.activity_ho_search_for_sps);
        providersListView = (ListView)findViewById(R.id.providersLV);
        serviceTitle = (TextView)findViewById(R.id.nameOfServiceFilter);
        addFilterBtn = (Button) findViewById(R.id.addFilterBtn);
        emptyLayout = (LinearLayout) findViewById(R.id.emptySearchLyt);
        btnLayout = (LinearLayout) findViewById(R.id.btnlyt);

        //getting info from past activity
        chosenServiceId  = getIntent().getStringExtra("srv_id");
        chosenServiceName = getIntent().getStringExtra("srv_name");
        homeOwnerName = getIntent().getStringExtra("ho_name");

        serviceTitle.setText(chosenServiceName);

    }




    //returns subset of service providers sps who offer service s
    public ArrayList<ServiceProvider> filterByService(List<ServiceProvider> sps, String serviceId){
        ArrayList<ServiceProvider> filteredProviders = new ArrayList<>();

        for (ServiceProvider s : sps) {
            if(s.offersService(serviceId)){
                filteredProviders.add(s);
            }
        }

        return filteredProviders;
    }



    /*
    Sets up list view
     */
    public void setUpProvidersList(){
                if(filteredProviders.isEmpty()){
                    showEmptySearchView();
                }else{
                    removeEmptySearchView();
                }
                adptr = new ServiceProviderListAdapter(HoSearchForSps.this, filteredProviders); //can setup the adapter now that the list is built
                providersListView.setAdapter(adptr);



                providersListView.setOnItemClickListener(//onclick of item in list view
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                                ServiceProvider clickedSp = (ServiceProvider) parent.getItemAtPosition(pos);
                                Intent intent = (new Intent(HoSearchForSps.this, SpProfileBookings.class)); //goes to bookings page
                                intent.putExtra( "SP_NAME", clickedSp.getUsername());
                                intent.putExtra( "SP_ID", clickedSp.getUserId());
                                intent.putExtra( "SRV_NAME", chosenServiceName);
                                intent.putExtra( "USER_NAME", homeOwnerName);

                                startActivity(intent);

                            }
                        }
                );

                addFilterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//shows filter popup
                        HoFilterBottomSheet filterDialog = new HoFilterBottomSheet();
                        filterDialog.show(getSupportFragmentManager(), "filterDialog");
                    }
                });

    }



    /*
    Interface method for communicating the chosen filters with this activity
    This is the method called after a user is done from the filter dialog

    ratingLowerbound: the rating threshold for service providers the user wants to see
    example i only want SPs who have 3 stars or higher

    avls: the availability threshold, only show SPs who are avialabile at any of the times listed in avls

    IF RATINGLOWERBOUND == -1, THE USER IS NOT FILTERING BY RATING
    IF AVLS == NULL, THE USER IS NOT FILTERING BY AVILABILITY
     */
    public void choseFilters(double ratingLowerBound,  List<Availability> avls){

        filteredProviders=providers; //clears past filtered list be resetting to orig providers


         //actual functionality
        if(ratingLowerBound != -1){ //rating filter chosen
            ArrayList<ServiceProvider> ratingFilteredProviders = new ArrayList<>(); //new filtered list

            for (ServiceProvider s : filteredProviders) {
                if(s.getCurrAvgRating() >= ratingLowerBound){
                    ratingFilteredProviders.add(s);
                }
            }
            filteredProviders=ratingFilteredProviders;  //update list used in list view and avl filtering
        }
        //if no rating chosen, filteredProviders stays the same

        if(avls != null){   //filter sps by avls specified

            ArrayList<ServiceProvider> avlFilteredProviders = new ArrayList<>();    //new filtered list
            for (ServiceProvider s : filteredProviders) {
                if(s.isAvailableSometimeDuring(avls)){
                    avlFilteredProviders.add(s);
                }
            }
            filteredProviders=avlFilteredProviders; //update list used in list view
        }
        //if no avls chosen, filteredProviders stays the same
        setUpProvidersList();
    }


    @Override
    //updates list of service providers on data change
    public void onStart(){

        super.onStart();
        Query query = users.orderByChild("type").equalTo("ServiceProvider"); //orders list alphabetically based on the service name

        query.addValueEventListener(new ValueEventListener(){  //singleEvetListener would make it update only when refreshed
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                providers.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    providers.add(postSnapshot.getValue(ServiceProvider.class));//adding all the service providers to our list.
                    providers = filterByService(providers, chosenServiceId); //filters the list of all Providers by the chosenServiceId
                    filteredProviders=providers;

                }

                setUpProvidersList();

            }

            public void onCancelled(DatabaseError databaseError){
            }
        });

    }

    private void showEmptySearchView(){
        emptyLayout.setVisibility(View.VISIBLE);
        btnLayout.setVisibility(View.GONE);
    }

    private void removeEmptySearchView(){
        emptyLayout.setVisibility(View.GONE);
        btnLayout.setVisibility(View.VISIBLE);
    }




}
