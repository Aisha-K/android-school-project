package com.example.angemichaella.homeservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angemichaella.homeservices.Availability;
import com.example.angemichaella.homeservices.R;
import com.example.angemichaella.homeservices.ServiceProvider;

import java.util.ArrayList;

public class ServiceProviderListAdapter extends ArrayAdapter<ServiceProvider> {

    String serviceId; //the services these providers provide

    public ServiceProviderListAdapter(Context context, ArrayList<ServiceProvider> providers, String serviceId){
        super(context, R.layout.serviceprovider_row, providers); //context, list item template layout, list of items 2be displayed
        this.serviceId = serviceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View myRowView = inflator.inflate(R.layout.serviceprovider_row, parent, false);

        ServiceProvider currSp = getItem(position);

        TextView name = (TextView) myRowView.findViewById(R.id.spUsername);
        TextView rating = (TextView) myRowView.findViewById(R.id.spRatingTV);
        ImageView licensing = (ImageView) myRowView.findViewById(R.id.licensedIcon);


        name.setText(currSp.getUsername());
        //if(currSp.isRated(serviceId)){
        rating.setText(Integer.toString((position % 5)) + " star rating");//rating.setText(currSp.getServiceRating(serviceId) + " STAR RATING");
        //}else{
          //  rating.setVisibility(View.GONE);
        //}

        if(!currSp.isLicensed()){
            licensing.setVisibility(View.GONE);
        }

        return myRowView;

    }


}
