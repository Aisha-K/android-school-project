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

public class AvAdapter extends ArrayAdapter<Availability> {
    List<Availability> availabilities;
    int size;

    public AvAdapter(Context context, List<Availability> availabilities){
        super(context, R.layout.av_row_layout, availabilities); //context, list item template layout, list of items 2be displayed
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View myRowView = inflator.inflate(R.layout.av_row_layout, parent, false);

        Availability currentAv = getItem(position);

        TextView avDay = (TextView) myRowView.findViewById(R.id.avDay);
        TextView avTime = (TextView) myRowView.findViewById(R.id.avTime);
        ImageView itemPic = (ImageView)myRowView.findViewById(R.id.avIcon);


        avDay.setText(currentAv.day());
        avTime.setText(currentAv.time());

        return myRowView;

    }
}