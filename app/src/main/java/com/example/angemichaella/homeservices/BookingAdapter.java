package com.example.angemichaella.homeservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {

    public static final int HO = 0;
    public static final int SP = 1;

    int observer; //whos looking at this  list view? could be a homeowner or could be a service provider


    public BookingAdapter(Context context, ArrayList<Booking> bookings, int observer){
        super(context, R.layout.av_row_layout, bookings); //context, list item template layout, list of items 2be displayed
        this.observer = observer;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View myRowView = inflator.inflate(R.layout.booking_row, parent, false);

        Booking b = getItem(position);

        TextView provider = (TextView) myRowView.findViewById(R.id.providerTv);
        TextView srvName = (TextView) myRowView.findViewById(R.id.serviceNameTv);
        TextView rating = (TextView) myRowView.findViewById(R.id.ratingTv);
        TextView date = (TextView) myRowView.findViewById(R.id.bookingDatetv);
        TextView feedback = (TextView) myRowView.findViewById(R.id.feedbackTV);
        ImageView completeIv = (ImageView) myRowView.findViewById(R.id.completeIV);

        if(observer == HO){
            provider.setText("Provided by "+ b.getServiceProviderName());

        }else{
            provider.setText("Provided to "+ b.getHomeOwnerName());
            if(b.getComment() != null){
                feedback.setVisibility(View.VISIBLE);
                feedback.setText('"'+b.getComment().trim() + '"');
            }
        }

        srvName.setText(b.getServiceName());
        date.setText(b.getDate());

        if(b.getRating() != -1){
            completeIv.setVisibility(View.VISIBLE);
            rating.setText("Rating: "+ b.getRating());
        }
       

        return myRowView;

    }
}
