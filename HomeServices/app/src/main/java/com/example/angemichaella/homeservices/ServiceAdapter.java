package com.example.angemichaella.homeservices;

import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.*;
import android.content.DialogInterface;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.*;
import android.widget.Toast;
import java.util.List;

public class ServiceAdapter extends ArrayAdapter<Service> {
    List<Service> services;
    int size;

    public ServiceAdapter(Context context, List<Service> services){
        super(context, R.layout.service_row_layout, services); //context, list item template layout, list of items 2be displayed
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View myRowView = inflator.inflate(R.layout.service_row_layout, parent, false);

        Service currentService = getItem(position);

        TextView serviceName = (TextView) myRowView.findViewById(R.id.serviceName);
        TextView serviceRate = (TextView) myRowView.findViewById(R.id.serviceRate);
        TextView type = (TextView) myRowView.findViewById(R.id.type);
        ImageView itemPic = (ImageView)myRowView.findViewById(R.id.serviceIcon);



        double rate = currentService.rate();
        rate = rate*100;

        double fract = rate - (int)rate;
        if(fract >=0.5){
            rate+= 1-fract;
        }
        int roundedRate = (int)rate;
        rate = roundedRate/100.00;
        String r = "$"+rate+"/hr";

        serviceRate.setText(r);
        serviceName.setText(currentService.name());
        type.setText(currentService.type());


        Resources res = getContext().getResources();
        int resID = res.getIdentifier(currentService.iconName, "drawable", getContext().getPackageName());
        itemPic.setImageResource(resID);

        return myRowView;


    }
}